package com.pvpoptimizer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.*;

public class ModConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("pvpoptimizer.json");

    public int renderDistance = 4;
    public int simulationDistance = 4;
    public float entityDistanceScaling = 0.5f;

    public boolean toggleSprintEnabled = true;
    public boolean hitboxesEnabled = false;
    public boolean showCps = true;
    public boolean showFps = true;
    public boolean showPing = true;
    public boolean showReach = true;
    public boolean showArmor = true;
    public boolean showDirection = true;
    public boolean showCoords = true;
    public boolean showPotionTimer = true;

    public boolean entityCullingEnabled = true;
    public boolean particleLimitEnabled = true;
    public int maxParticles = 10;
    public boolean disableFog = true;
    public boolean disableClouds = true;
    public boolean disableWeather = false;
    public boolean disableEntityAnimations = false;
    public boolean disableBlockAnimations = false;

    public boolean sprintResetIndicator = true;
    public boolean lowHealthEffect = true;

    public int hudX = 2;
    public int hudY = 2;
    public String hudColor = "#FFFFFF";

    public static ModConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                return GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                System.err.println("[PvP Optimizer] Failed to read config, using defaults.");
            }
        }
        ModConfig defaults = new ModConfig();
        defaults.save();
        return defaults;
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("[PvP Optimizer] Failed to save config.");
        }
    }
}
