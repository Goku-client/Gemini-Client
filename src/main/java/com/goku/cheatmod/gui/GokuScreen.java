package com.goku.cheatmod.gui;

import com.goku.cheatmod.config.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GokuScreen extends Screen {

    private final List<GuiParticle> particles = new ArrayList<>();
    private final Random random = new Random();

    public GokuScreen() {
        super(Text.literal("GOKU Menu"));
    }

    @Override
    protected void init() {
        particles.clear();
        for (int i = 0; i < 60; i++) {
            particles.add(new GuiParticle(random.nextInt(Math.max(this.width, 1)), random.nextInt(Math.max(this.height, 1))));
        }

        int buttonWidth = 150;
        int buttonHeight = 20;
        int startX = this.width / 2 - buttonWidth / 2;
        int startY = 60;

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("ESP: " + (ModConfig.espEnabled ? "ON" : "OFF")),
                btn -> {
                    ModConfig.espEnabled = !ModConfig.espEnabled;
                    btn.setMessage(Text.literal("ESP: " + (ModConfig.espEnabled ? "ON" : "OFF")));
                }).dimensions(startX, startY, buttonWidth, buttonHeight).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Containers: " + (ModConfig.showContainers ? "ON" : "OFF")),
                btn -> {
                    ModConfig.showContainers = !ModConfig.showContainers;
                    btn.setMessage(Text.literal("Containers: " + (ModConfig.showContainers ? "ON" : "OFF")));
                }).dimensions(startX, startY + 25, buttonWidth, buttonHeight).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Aimbot: " + (ModConfig.aimbotEnabled ? "ON" : "OFF")),
                btn -> {
                    ModConfig.aimbotEnabled = !ModConfig.aimbotEnabled;
                    btn.setMessage(Text.literal("Aimbot: " + (ModConfig.aimbotEnabled ? "ON" : "OFF")));
                }).dimensions(startX, startY + 50, buttonWidth, buttonHeight).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Auto Attack: " + (ModConfig.autoAttackEnabled ? "ON" : "OFF")),
                btn -> {
                    ModConfig.autoAttackEnabled = !ModConfig.autoAttackEnabled;
                    btn.setMessage(Text.literal("Auto Attack: " + (ModConfig.autoAttackEnabled ? "ON" : "OFF")));
                }).dimensions(startX, startY + 75, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xCC000529);

        for (GuiParticle p : particles) {
            p.update(this.width, this.height);
            context.fill((int) p.x, (int) p.y, (int) p.x + 2, (int) p.y + 2, 0xFF00AAFF);
        }

        context.drawCenteredTextWithShadow(this.textRenderer, "§b§lGOKU", this.width / 2, 20, 0x00FFFF);

        context.drawText(this.textRenderer, "ESP Color Palette:", startXOffset(), 170, 0xFFFFFF, false);
        context.fill(startXOffset(), 185, startXOffset() + 30, 205, ModConfig.entityEspColor);
        context.fill(startXOffset() + 40, 185, startXOffset() + 70, 205, ModConfig.playerEspColor);
        context.fill(startXOffset() + 80, 185, startXOffset() + 110, 205, ModConfig.containerEspColor);

        super.render(context, mouseX, mouseY, delta);
    }

    private int startXOffset() {
        return this.width / 2 - 55;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseY >= 185 && mouseY <= 205) {
            if (mouseX >= startXOffset() && mouseX <= startXOffset() + 30) {
                ModConfig.entityEspColor = getRandomColor();
            } else if (mouseX >= startXOffset() + 40 && mouseX <= startXOffset() + 70) {
                ModConfig.playerEspColor = getRandomColor();
            } else if (mouseX >= startXOffset() + 80 && mouseX <= startXOffset() + 110) {
                ModConfig.containerEspColor = getRandomColor();
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private int getRandomColor() {
        return 0xFF000000 | random.nextInt(0xFFFFFF);
    }

    private static class GuiParticle {
        float x, y, vx, vy;
        Random r = new Random();

        GuiParticle(float x, float y) {
            this.x = x;
            this.y = y;
            this.vx = (r.nextFloat() - 0.5f) * 1.5f;
            this.vy = (r.nextFloat() - 0.5f) * 1.5f;
        }

        void update(int width, int height) {
            x += vx;
            y += vy;
            if (x < 0 || x > width) vx *= -1;
            if (y < 0 || y > height) vy *= -1;
        }
    }
}
