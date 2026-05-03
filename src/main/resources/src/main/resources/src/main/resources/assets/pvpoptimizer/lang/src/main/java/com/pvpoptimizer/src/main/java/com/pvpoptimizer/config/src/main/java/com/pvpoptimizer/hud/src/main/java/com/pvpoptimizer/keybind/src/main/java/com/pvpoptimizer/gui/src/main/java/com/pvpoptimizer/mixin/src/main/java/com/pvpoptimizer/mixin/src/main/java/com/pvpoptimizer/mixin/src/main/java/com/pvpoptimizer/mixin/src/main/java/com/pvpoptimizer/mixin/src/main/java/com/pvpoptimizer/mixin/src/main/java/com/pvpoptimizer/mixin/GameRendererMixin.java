package com.pvpoptimizer.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(
        method = "bobViewWhenHurt",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelHurtBob(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(
        method = "showFloatingItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelFloatingItem(ItemStack floatingItem, CallbackInfo ci) {
        ci.cancel();
    }
}
