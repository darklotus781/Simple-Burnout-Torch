/*
 * Simple Burnout Torch
 * Copyright (c) 2025 DarkLotus (DarkLotus781) / LithiumCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
