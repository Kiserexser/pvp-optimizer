package com.pvpoptimizer.mixin;

import com.pvpoptimizer.PvpOptimizerMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ToggleSprintMixin {

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void onTickMovement(CallbackInfo ci) {
        if (!PvpOptimizerMod.CONFIG.toggleSprintEnabled) return;
        if (!PvpOptimizerMod.getHud().isToggleSprinting()) return;

        ClientPlayerEntity player = (ClientPlayerEntity)(Object) this;
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.options.forwardKey.isPressed()
                && !player.isTouchingWater()
                && !player.isSubmergedInWater()
                && player.getHungerManager().getFoodLevel() > 6
                && !player.isBlocking()) {
            player.setSprinting(true);
        }
    }
}
