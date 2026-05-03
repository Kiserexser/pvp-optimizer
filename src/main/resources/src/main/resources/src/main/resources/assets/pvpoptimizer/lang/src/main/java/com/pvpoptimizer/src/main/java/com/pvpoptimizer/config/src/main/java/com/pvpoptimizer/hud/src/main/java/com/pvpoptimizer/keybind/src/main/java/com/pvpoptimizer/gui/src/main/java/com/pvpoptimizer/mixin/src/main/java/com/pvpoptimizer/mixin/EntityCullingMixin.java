package com.pvpoptimizer.mixin;

import com.pvpoptimizer.PvpOptimizerMod;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.MinecraftClient;

@Mixin(EntityRenderDispatcher.class)
public class EntityCullingMixin {

    @Inject(
        method = "shouldRender",
        at = @At("HEAD"),
        cancellable = true
    )
    private <E extends Entity> void onShouldRender(
            E entity,
            net.minecraft.client.frustum.Frustum frustum,
            double x, double y, double z,
            CallbackInfoReturnable<Boolean> cir) {

        if (!PvpOptimizerMod.CONFIG.entityCullingEnabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        double distSq = client.player.squaredDistanceTo(entity);
        double maxDist = PvpOptimizerMod.CONFIG.renderDistance * 16.0;

        if (distSq > maxDist * maxDist) {
            cir.setReturnValue(false);
            return;
        }

        if (!frustum.isVisible(entity.getBoundingBox())) {
            cir.setReturnValue(false);
        }
    }
}
