package com.pvpoptimizer.mixin;

import com.pvpoptimizer.PvpOptimizerMod;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class FogMixin {

    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void onRenderWorld(float tickDelta, long limitTime,
                               net.minecraft.client.util.math.MatrixStack matrix,
                               CallbackInfo ci) {
        if (PvpOptimizerMod.CONFIG.disableFog) {
            com.mojang.blaze3d.systems.RenderSystem.setShaderFogStart(Float.MAX_VALUE);
            com.mojang.blaze3d.systems.RenderSystem.setShaderFogEnd(Float.MAX_VALUE);
        }
    }
}
