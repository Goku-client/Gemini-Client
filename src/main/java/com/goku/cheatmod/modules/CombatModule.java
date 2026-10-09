package com.goku.cheatmod.modules;

import com.goku.cheatmod.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CombatModule {

    public static void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        LivingEntity target = getClosestTarget(client);
        if (target == null) return;

        if (ModConfig.aimbotEnabled) {
            lookAtEntity(client.player, target);
        }

        if (ModConfig.autoAttackEnabled && client.player.getAttackCooldownProgress(0.5f) >= 1.0f) {
            if (client.player.distanceTo(target) <= 4.5f) {
                client.interactionManager.attackEntity(client.player, target);
                client.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    private static LivingEntity getClosestTarget(MinecraftClient client) {
        LivingEntity closest = null;
        double minDistance = 10.0;

        for (Entity e : client.world.getEntities()) {
            if (e == client.player || !(e instanceof LivingEntity living)) continue;
            if (!living.isAlive()) continue;

            if ((e instanceof PlayerEntity && ModConfig.showPlayers) ||
                (e instanceof HostileEntity && ModConfig.showMobs)) {

                double dist = client.player.distanceTo(e);
                if (dist < minDistance) {
                    minDistance = dist;
                    closest = living;
                }
            }
        }
        return closest;
    }

    private static void lookAtEntity(PlayerEntity player, Entity target) {
        Vec3d ePos = target.getEyePos();
        Vec3d pPos = player.getEyePos();

        double dx = ePos.x - pPos.x;
        double dy = ePos.y - pPos.y;
        double dz = ePos.z - pPos.z;

        double dh = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(dy, dh)));

        player.setYaw(MathHelper.wrapDegrees(yaw));
        player.setPitch(MathHelper.wrapDegrees(pitch));
    }
}
