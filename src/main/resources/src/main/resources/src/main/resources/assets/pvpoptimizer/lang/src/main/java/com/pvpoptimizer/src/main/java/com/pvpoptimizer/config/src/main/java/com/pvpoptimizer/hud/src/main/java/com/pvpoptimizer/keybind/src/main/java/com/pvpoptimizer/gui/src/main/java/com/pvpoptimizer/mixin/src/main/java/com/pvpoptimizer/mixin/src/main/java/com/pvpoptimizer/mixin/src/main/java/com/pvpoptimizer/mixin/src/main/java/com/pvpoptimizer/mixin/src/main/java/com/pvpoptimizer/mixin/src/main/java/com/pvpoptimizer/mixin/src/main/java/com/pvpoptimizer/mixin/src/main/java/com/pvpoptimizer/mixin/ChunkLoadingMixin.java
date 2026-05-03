package com.pvpoptimizer.mixin;

import net.minecraft.client.render.chunk.ChunkBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChunkBuilder.class)
public class ChunkLoadingMixin {

    @ModifyArg(
        method = "<init>",
        at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(II)I"),
        index = 1
    )
    private int limitBuilderThreads(int original) {
        return Math.min(original, 2);
    }
}
