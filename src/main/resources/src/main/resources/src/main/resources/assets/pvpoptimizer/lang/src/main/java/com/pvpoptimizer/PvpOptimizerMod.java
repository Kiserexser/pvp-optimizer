package com.pvpoptimizer;

import com.pvpoptimizer.config.ModConfig;
import com.pvpoptimizer.hud.PvpHud;
import com.pvpoptimizer.keybind.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PvpOptimizerMod implements ClientModInitializer {

    public static final String MOD_ID = "pvpoptimizer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ModConfig CONFIG;
    private static PvpHud pvpHud;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[PvP Optimizer] Loading...");

        CONFIG = ModConfig.load();
        pvpHud = new PvpHud();

        KeyBindings.register();

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        HudRenderCallback.EVENT.register(pvpHud::render);

        applyGraphicsSettings();

        LOGGER.info("[PvP Optimizer] Loaded! Enjoy 1000+ FPS.");
    }

    private void applyGraphicsSettings() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        GameOptions opts = client.options;

        opts.getViewDistance().setValue(CONFIG.renderDistance);
        opts.getSimulationDistance().setValue(CONFIG.simulationDistance);
        opts.getGraphicsMode().setValue(net.minecraft.client.option.GraphicsMode.FAST);
        opts.getCloudRenderMod().setValue(net.minecraft.client.option.CloudRenderMode.OFF);
        opts.getParticles().setValue(net.minecraft.client.option.ParticlesMode.MINIMAL);
        opts.getEntityShadows().setValue(false);
        opts.getBobView().setValue(false);
        opts.getShowAutosaveIndicator().setValue(false);
        opts.getEntityDistanceScaling().setValue(CONFIG.entityDistanceScaling);
        opts.getBiomeBlendRadius().setValue(0);
        opts.getMipmapLevels().setValue(0);
        opts.getMaxFps().setValue(260);

        opts.write();
        LOGGER.info("[PvP Optimizer] Graphics settings applied.");
    }

    private void onClientTick(MinecraftClient client) {
        KeyBindings.handleKeyPresses(client);

        if (pvpHud != null) {
            pvpHud.tick(client);
        }
    }

    public static PvpHud getHud() {
        return pvpHud;
    }
}
