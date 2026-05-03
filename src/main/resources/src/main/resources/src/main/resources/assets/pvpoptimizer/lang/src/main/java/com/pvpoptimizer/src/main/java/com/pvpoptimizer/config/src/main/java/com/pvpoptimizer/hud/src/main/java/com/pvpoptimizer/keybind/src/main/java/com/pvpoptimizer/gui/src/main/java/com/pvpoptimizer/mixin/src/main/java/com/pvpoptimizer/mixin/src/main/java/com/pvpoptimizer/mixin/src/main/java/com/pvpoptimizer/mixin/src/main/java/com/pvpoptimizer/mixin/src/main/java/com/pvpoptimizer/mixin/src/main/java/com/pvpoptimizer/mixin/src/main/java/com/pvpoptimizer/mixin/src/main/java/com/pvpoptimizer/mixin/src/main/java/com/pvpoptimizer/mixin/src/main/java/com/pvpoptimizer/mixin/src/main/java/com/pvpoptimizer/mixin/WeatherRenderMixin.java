package com.pvpoptimizer.mixin;

import com.pvpoptimizer.PvpOptimizerMod;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WeatherRenderMixin {

    @Inject(
        method = "renderWeather",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onRenderWeather(
            LightmapTextureManager manager,
            float tickDelta, double x, double y, double z,
            CallbackInfo ci) {
        if (PvpOptimizerMod.CONFIG.disableWeather) {
            ci.cancel();
        }
    }
}
