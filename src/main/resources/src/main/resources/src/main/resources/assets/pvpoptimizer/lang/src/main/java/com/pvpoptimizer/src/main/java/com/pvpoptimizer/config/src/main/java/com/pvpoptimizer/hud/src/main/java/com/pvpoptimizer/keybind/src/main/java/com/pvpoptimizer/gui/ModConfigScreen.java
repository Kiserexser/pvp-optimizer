package com.pvpoptimizer.gui;

import com.pvpoptimizer.PvpOptimizerMod;
import com.pvpoptimizer.config.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public class ModConfigScreen extends Screen {

    private final Screen parent;
    private ModConfig cfg;
    private static final int PANEL_W = 420;
    private static final int PANEL_H = 420;
    private static final int COL_W = 190;
    private static final int ITEM_H = 22;
    private static final int PADDING = 10;
    private int panelX, panelY;
    private final List<ToggleEntry> toggles = new ArrayList<>();
    private TextFieldWidget renderDistField;
    private TextFieldWidget particleField;

    public ModConfigScreen(Screen parent) {
        super(Text.literal("PvP Optimizer Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        cfg = PvpOptimizerMod.CONFIG;
        toggles.clear();
        panelX = (this.width - PANEL_W) / 2;
        panelY = (this.height - PANEL_H) / 2;
        int col1 = panelX + PADDING;
        int col2 = panelX + PANEL_W / 2 + PADDING / 2;
        int top = panelY + 56;

        addToggle("Fog Disable", col1, top, () -> cfg.disableFog, v -> cfg.disableFog = v);
        addToggle("Clouds Disable", col2, top, () -> cfg.disableClouds, v -> cfg.disableClouds = v); top += ITEM_H + 4;
        addToggle("Entity Culling", col1, top, () -> cfg.entityCullingEnabled, v -> cfg.entityCullingEnabled = v);
        addToggle("Particle Limit", col2, top, () -> cfg.particleLimitEnabled, v -> cfg.particleLimitEnabled = v); top += ITEM_H + 4;
        addToggle("Disable Weather", col1, top, () -> cfg.disableWeather, v -> cfg.disableWeather = v);
        addToggle("Disable Animations", col2, top, () -> cfg.disableEntityAnimations, v -> cfg.disableEntityAnimations = v); top += ITEM_H + 4;
        addToggle("Toggle Sprint", col1, top, () -> cfg.toggleSprintEnabled, v -> cfg.toggleSprintEnabled = v);
        addToggle("Hitboxes", col2, top, () -> cfg.hitboxesEnabled, v -> { cfg.hitboxesEnabled = v; if (this.client != null) this.client.getEntityRenderDispatcher().setRenderHitboxes(v); }); top += ITEM_H + 4;
        addToggle("Show FPS", col1, top, () -> cfg.showFps, v -> cfg.showFps = v);
        addToggle("Show CPS", col2, top, () -> cfg.showCps, v -> cfg.showCps = v); top += ITEM_H + 4;
        addToggle("Show Ping", col1, top, () -> cfg.showPing, v -> cfg.showPing = v);
        addToggle("Show Reach", col2, top, () -> cfg.showReach, v -> cfg.showReach = v); top += ITEM_H + 4;
        addToggle("Show Armor", col1, top, () -> cfg.showArmor, v -> cfg.showArmor = v);
        addToggle("Show Coords", col2, top, () -> cfg.showCoords, v -> cfg.showCoords = v); top += ITEM_H + 4;
        addToggle("Show Direction", col1, top, () -> cfg.showDirection, v -> cfg.showDirection = v);
        addToggle("Potion Timer", col2, top, () -> cfg.showPotionTimer, v -> cfg.showPotionTimer = v); top += ITEM_H + 4;
        addToggle("Sprint Reset", col1, top, () -> cfg.sprintResetIndicator, v -> cfg.sprintResetIndicator = v);
        addToggle("Low HP Effect", col2, top, () -> cfg.lowHealthEffect, v -> cfg.lowHealthEffect = v);

        int sepY = panelY + PANEL_H - 110;
        renderDistField = new TextFieldWidget(this.textRenderer, col1, sepY + 18, COL_W, 18, Text.literal("Render Distance"));
        renderDistField.setMaxLength(2);
        renderDistField.setText(String.valueOf(cfg.renderDistance));
        renderDistField.setChangedListener(val -> { try { int v = Integer.parseInt(val.trim()); if (v >= 2 && v <= 16) cfg.renderDistance = v; } catch (NumberFormatException ignored) {} });
        this.addSelectableChild(renderDistField);

        particleField = new TextFieldWidget(this.textRenderer, col2, sepY + 18, COL_W, 18, Text.literal("Max Particles"));
        particleField.setMaxLength(3);
        particleField.setText(String.valueOf(cfg.maxParticles));
        particleField.setChangedListener(val -> { try { int v = Integer.parseInt(val.trim()); if (v >= 0 && v <= 500) cfg.maxParticles = v; } catch (NumberFormatException ignored) {} });
        this.addSelectableChild(particleField);

        int btnY = panelY + PANEL_H - 36;
        int cx = panelX + PANEL_W / 2;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save & Close"), b -> { cfg.save(); if (this.client != null) this.client.setScreen(parent); }).dimensions(cx - 134, btnY, 130, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> { if (this.client != null) this.client.setScreen(parent); }).dimensions(cx + 4, btnY, 130, 20).build());
    }

    private void addToggle(String name, int x, int y, java.util.function.BooleanSupplier getter, java.util.function.Consumer<Boolean> setter) {
        ToggleEntry entry = new ToggleEntry(name, x, y, getter, setter);
        toggles.add(entry);
        ButtonWidget btn = ButtonWidget.builder(entry.getLabel(), b -> { entry.toggle(); b.setMessage(entry.getLabel()); }).dimensions(x, y, COL_W, ITEM_H).build();
        this.addDrawableChild(btn);
        entry.button = btn;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);
        ctx.fill(panelX - 2, panelY - 2, panelX + PANEL_W + 2, panelY + PANEL_H + 2, 0xFF00FFAA);
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + PANEL_H, 0xE0101010);
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + 34, 0xFF1A1A2E);
        ctx.drawCenteredTextWithShadow(this.textRenderer, "PvP Optimizer Settings", panelX + PANEL_W / 2, panelY + 10, 0xFF00FFAA);
        ctx.drawTextWithShadow(this.textRenderer, "Optimizations", panelX + PADDING, panelY + 40, 0xFFAAAAAA);
        int sepY = panelY + PANEL_H - 120;
        ctx.fill(panelX + PADDING, sepY, panelX + PANEL_W - PADDING, sepY + 1, 0xFF333333);
        ctx.drawTextWithShadow(this.textRenderer, "Render Distance (2-16):", panelX + PADDING, sepY + 6, 0xFFAAAAAA);
        ctx.drawTextWithShadow(this.textRenderer, "Max Particles (0-500):", panelX + PANEL_W / 2 + PADDING / 2, sepY + 6, 0xFFAAAAAA);
        if (renderDistField != null) { renderDistField.setY(sepY + 18); renderDistField.render(ctx, mouseX, mouseY, delta); }
        if (particleField != null) { particleField.setY(sepY + 18); particleField.render(ctx, mouseX, mouseY, delta); }
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }

    private static class ToggleEntry {
        final String name; final int x, y;
        final java.util.function.BooleanSupplier getter;
        final java.util.function.Consumer<Boolean> setter;
        ButtonWidget button;
        ToggleEntry(String name, int x, int y, java.util.function.BooleanSupplier getter, java.util.function.Consumer<Boolean> setter) {
            this.name = name; this.x = x; this.y = y; this.getter = getter; this.setter = setter;
        }
        void toggle() { setter.accept(!getter.getAsBoolean()); }
        Text getLabel() { boolean on = getter.getAsBoolean(); return Text.literal((on ? "§a✔ " : "§c✘ ") + name); }
    }
}
