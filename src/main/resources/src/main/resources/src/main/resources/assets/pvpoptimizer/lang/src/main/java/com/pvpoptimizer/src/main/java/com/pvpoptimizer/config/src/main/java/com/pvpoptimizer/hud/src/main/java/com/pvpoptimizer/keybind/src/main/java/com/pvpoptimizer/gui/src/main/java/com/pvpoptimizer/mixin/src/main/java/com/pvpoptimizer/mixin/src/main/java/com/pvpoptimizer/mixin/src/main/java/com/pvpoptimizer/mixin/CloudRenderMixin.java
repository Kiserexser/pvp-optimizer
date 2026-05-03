package com.pvpoptimizer.mixin;

import com.pvpoptimizer.PvpOptimizerMod;
import net.minecraft.client.render.BackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class CloudRenderMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private static void onRenderFog(CallbackInfo ci) {
        if (PvpOptimizerMod.CONFIG.disableFog) {
            ci.cancel();
        }
    }
}
