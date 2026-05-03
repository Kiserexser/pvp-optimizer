package com.pvpoptimizer.hud;

import com.pvpoptimizer.PvpOptimizerMod;
import com.pvpoptimizer.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import java.util.ArrayList;
import java.util.List;

public class PvpHud {

    private final List<Long> leftClickTimes = new ArrayList<>();
    private final List<Long> rightClickTimes = new ArrayList<>();
    private boolean toggleSprinting = false;

    public void tick(MinecraftClient client) {
        if (client.player == null) return;
        long now = System.currentTimeMillis();
        leftClickTimes.removeIf(t -> now - t > 1000);
        rightClickTimes.removeIf(t -> now - t > 1000);

        ModConfig cfg = PvpOptimizerMod.CONFIG;
        if (cfg.toggleSprintEnabled && toggleSprinting && client.player != null) {
            if (!client.player.isSprinting() && client.player.forwardSpeed > 0) {
                client.player.setSprinting(true);
            }
        }
    }

    public void registerLeftClick() {
        leftClickTimes.add(System.currentTimeMillis());
    }

    public void registerRightClick() {
        rightClickTimes.add(System.currentTimeMillis());
    }

    public void setToggleSprint(boolean active) { this.toggleSprinting = active; }
    public boolean isToggleSprinting() { return toggleSprinting; }

    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.options.hudHidden) return;

        ModConfig cfg = PvpOptimizerMod.CONFIG;
        TextRenderer tr = client.textRenderer;
        ClientPlayerEntity player = client.player;

        int x = cfg.hudX;
        int y = cfg.hudY;
        int lineH = tr.fontHeight + 2;
        int green = 0xFF00FF80;
        int yellow = 0xFFFFFF00;
        int red = 0xFFFF4444;
        int white = 0xFFFFFFFF;
        int gray = 0xFFAAAAAA;
        int cyan = 0xFF00FFFF;

        if (cfg.showFps) {
            int fps = client.getCurrentFps();
            int c = fps >= 500 ? green : fps >= 100 ? yellow : red;
            context.drawText(tr, "FPS: " + fps, x, y, c, true);
            y += lineH;
        }
        if (cfg.showCps) {
            context.drawText(tr, "CPS: " + leftClickTimes.size() + " | " + rightClickTimes.size(), x, y, cyan, true);
            y += lineH;
        }
        if (cfg.showPing && client.getNetworkHandler() != null) {
            var entry = client.getNetworkHandler().getPlayerListEntry(player.getUuid());
            if (entry != null) {
                int ping = entry.getLatency();
                int c = ping <= 50 ? green : ping <= 100 ? yellow : red;
                context.drawText(tr, "Ping: " + ping + "ms", x, y, c, true);
                y += lineH;
            }
        }
        if (cfg.showReach) {
            context.drawText(tr, "Reach: 3.00b", x, y, white, true);
            y += lineH;
        }
        if (cfg.toggleSprintEnabled) {
            context.drawText(tr, toggleSprinting ? "SPRINT: ON" : "SPRINT: OFF", x, y, toggleSprinting ? green : red, true);
            y += lineH;
        }
        if (cfg.showCoords) {
            context.drawText(tr, "XYZ: " + (int)player.getX() + " / " + (int)player.getY() + " / " + (int)player.getZ(), x, y, gray, true);
            y += lineH;
        }
        if (cfg.showDirection) {
            Direction dir = player.getHorizontalFacing();
            String d = switch (dir) { case NORTH -> "N (-Z)"; case SOUTH -> "S (+Z)"; case EAST -> "E (+X)"; case WEST -> "W (-X)"; default -> dir.name(); };
            context.drawText(tr, "Dir: " + d, x, y, white, true);
            y += lineH;
        }
        if (cfg.showArmor) {
            PlayerInventory inv = player.getInventory();
            int armor = 0;
            for (int i = 36; i < 40; i++) { if (!inv.getStack(i).isEmpty()) armor++; }
            context.drawText(tr, "Armor: " + armor + "/4", x, y, armor == 4 ? green : armor >= 2 ? yellow : red, true);
            y += lineH;
        }
        if (cfg.sprintResetIndicator) {
            context.drawText(tr, player.isSprinting() ? "SPRINT" : "RESET", x, y, player.isSprinting() ? green : yellow, true);
            y += lineH;
        }
        if (cfg.showPotionTimer) {
            for (var effect : player.getStatusEffects()) {
                int secs = effect.getDuration() / 20;
                String name = effect.getEffectType().getName().getString();
                context.drawText(tr, name + " " + (effect.getAmplifier()+1) + " (" + secs + "s)", x, y, white, true);
                y += lineH;
            }
        }
    }
}
