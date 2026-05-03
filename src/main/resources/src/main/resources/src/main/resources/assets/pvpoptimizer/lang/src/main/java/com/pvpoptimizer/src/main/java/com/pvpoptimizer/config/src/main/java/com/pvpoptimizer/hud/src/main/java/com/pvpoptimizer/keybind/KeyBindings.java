package com.pvpoptimizer.keybind;

import com.pvpoptimizer.PvpOptimizerMod;
import com.pvpoptimizer.config.ModConfig;
import com.pvpoptimizer.gui.ModConfigScreen;
import com.pvpoptimizer.hud.PvpHud;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static KeyBinding openSettings;
    public static KeyBinding toggleSprint;
    public static KeyBinding toggleHitboxes;
    public static KeyBinding toggleHud;

    public static void register() {
        openSettings = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpoptimizer.settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.pvpoptimizer"
        ));

        toggleSprint = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpoptimizer.togglesprint",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.pvpoptimizer"
        ));

        toggleHitboxes = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpoptimizer.hitboxes",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.pvpoptimizer"
        ));

        toggleHud = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpoptimizer.hud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_BACKSLASH,
                "category.pvpoptimizer"
        ));
    }

    public static void handleKeyPresses(MinecraftClient client) {
        ModConfig cfg = PvpOptimizerMod.CONFIG;
        PvpHud hud = PvpOptimizerMod.getHud();

        while (openSettings.wasPressed()) {
            client.setScreen(new ModConfigScreen(client.currentScreen));
        }

        while (toggleSprint.wasPressed()) {
            boolean current = hud.isToggleSprinting();
            hud.setToggleSprint(!current);
            cfg.toggleSprintEnabled = !current;
            cfg.save();
        }

        while (toggleHitboxes.wasPressed()) {
            cfg.hitboxesEnabled = !cfg.hitboxesEnabled;
            client.getEntityRenderDispatcher().setRenderHitboxes(cfg.hitboxesEnabled);
            cfg.save();
        }

        while (toggleHud.wasPressed()) {
            cfg.showFps = !cfg.showFps;
            cfg.save();
        }
    }
}
