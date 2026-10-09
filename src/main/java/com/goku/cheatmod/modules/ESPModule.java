package com.goku.cheatmod.modules;

import com.goku.cheatmod.config.ModConfig;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

public class ESPModule {

    public static void renderESP(WorldRenderContext context) {
        if (!ModConfig.espEnabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        for (Entity e : client.world.getEntities()) {
            if (e == client.player) continue;

            if (e instanceof PlayerEntity && ModConfig.showPlayers) {
                drawOutline(context, e.getBoundingBox(), ModConfig.playerEspColor);
            } else if (e instanceof HostileEntity && ModConfig.showMobs) {
                drawOutline(context, e.getBoundingBox(), ModConfig.entityEspColor);
            }
        }

        if (ModConfig.showContainers) {
            for (BlockEntity be : client.world.blockEntityList) {
                if (be instanceof ChestBlockEntity || be instanceof HopperBlockEntity) {
                    Box box = new Box(be.getPos());
                    drawOutline(context, box, ModConfig.containerEspColor);
                }
            }
        }
    }

    private static void drawOutline(WorldRenderContext context, Box box, int color) {
        // Outline rendering logic inside render tick
    }
}
