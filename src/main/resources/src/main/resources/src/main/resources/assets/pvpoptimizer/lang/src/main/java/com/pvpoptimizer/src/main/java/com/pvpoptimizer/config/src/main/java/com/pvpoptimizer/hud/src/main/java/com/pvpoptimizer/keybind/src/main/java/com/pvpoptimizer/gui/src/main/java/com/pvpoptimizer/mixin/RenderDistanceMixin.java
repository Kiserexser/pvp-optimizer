package com.pvpoptimizer.mixin;

import com.pvpoptimizer.PvpOptimizerMod;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldRenderer.class)
public class RenderDistanceMixin {

    @ModifyVariable(
        method = "<init>",
        at = @At("STORE"),
        ordinal = 0
    )
    private int limitRenderDistance(int original) {
        int max = PvpOptimizerMod.CONFIG.renderDistance;
        return Math.min(original, max);
    }
}
