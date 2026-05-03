package com.pvpoptimizer.mixin;

import com.pvpoptimizer.PvpOptimizerMod;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Queue;

@Mixin(ParticleManager.class)
public class ParticleLimitMixin {

    @Shadow
    private Queue<?> newEmitterParticles;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!PvpOptimizerMod.CONFIG.particleLimitEnabled) return;

        int max = PvpOptimizerMod.CONFIG.maxParticles;
        while (newEmitterParticles != null && newEmitterParticles.size() > max) {
            newEmitterParticles.poll();
        }
    }
}
