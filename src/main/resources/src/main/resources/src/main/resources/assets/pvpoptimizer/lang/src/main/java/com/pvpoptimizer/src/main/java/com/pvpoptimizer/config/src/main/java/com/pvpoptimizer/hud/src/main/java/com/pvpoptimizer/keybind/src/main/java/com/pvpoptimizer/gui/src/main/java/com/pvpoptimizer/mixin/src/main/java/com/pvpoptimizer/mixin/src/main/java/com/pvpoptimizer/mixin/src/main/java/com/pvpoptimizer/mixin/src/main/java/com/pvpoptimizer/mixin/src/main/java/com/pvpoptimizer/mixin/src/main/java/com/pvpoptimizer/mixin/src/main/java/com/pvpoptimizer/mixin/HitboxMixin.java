package com.pvpoptimizer.mixin;

import com.pvpoptimizer.PvpOptimizerMod;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class HitboxMixin {

    @Inject(
        method = "drawEntityOutline",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onDrawEntityOutline(CallbackInfo ci) {
        if (!PvpOptimizerMod.CONFIG.hitboxesEnabled) {
            ci.cancel();
        }
    }
}
