package com.pvpoptimizer.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.MinecraftClient;

@Mixin(EntityRenderer.class)
public class EntityRenderMixin<T extends Entity> {

    @Inject(
        method = "hasLabel(Lnet/minecraft/entity/Entity;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void limitLabelDistance(T entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity == null) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        double distSq = client.player.squaredDistanceTo(entity);
        double labelDistMax = 16.0;
        if (distSq > labelDistMax * labelDistMax) {
            cir.setReturnValue(false);
        }
    }
}
