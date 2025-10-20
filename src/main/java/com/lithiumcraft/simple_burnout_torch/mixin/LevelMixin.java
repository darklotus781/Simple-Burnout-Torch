package com.lithiumcraft.simple_burnout_torch.mixin;

import com.lithiumcraft.simple_burnout_torch.util.TorchReplacementHelper;
import com.lithiumcraft.simple_burnout_torch.util.TorchReplacementHelper.PlaceDecision;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class LevelMixin {

    // ----- BLOCK (deny) when config/type says so -----

    @Inject(
            method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sbt_block3(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<Boolean> cir) {
        Level self = (Level)(Object)this;
        PlaceDecision decision = TorchReplacementHelper.onPlaceDecision(self, pos, state);
        if (decision == PlaceDecision.BLOCK) {
            if (!self.isClientSide) {
                TorchReplacementHelper.notifyNearestPlayer(self, pos, TorchReplacementHelper.defaultBlockedMessage());
            }
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sbt_block4(BlockPos pos, BlockState state, int flags, int recursionLeft, CallbackInfoReturnable<Boolean> cir) {
        sbt_block3(pos, state, flags, cir);
    }

    // ----- REWRITE (allowed) to our burned-out variant, no cancel -----

    @ModifyVariable(
            method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            at = @At("HEAD"),
            ordinal = 0
    )
    private BlockState sbt_rewrite3(BlockState state) {
        Level self = (Level)(Object)this;
        return TorchReplacementHelper.rewriteIfAllowed(self, state);
    }

    @ModifyVariable(
            method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
            at = @At("HEAD"),
            ordinal = 0
    )
    private BlockState sbt_rewrite4(BlockState state) {
        Level self = (Level)(Object)this;
        return TorchReplacementHelper.rewriteIfAllowed(self, state);
    }
}
