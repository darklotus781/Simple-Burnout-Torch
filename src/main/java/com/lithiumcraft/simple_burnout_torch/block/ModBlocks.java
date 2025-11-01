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

package com.lithiumcraft.simple_burnout_torch.block;

import com.lithiumcraft.simple_burnout_torch.SimpleBurnoutTorch;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SimpleBurnoutTorch.MOD_ID);

    public static final DeferredBlock<BurnableTorchBlock> BURNABLE_TORCH =
            BLOCKS.register("burnable_torch",
                    () -> new BurnableTorchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH)));

    public static final DeferredBlock<BurnableWallTorchBlock> BURNABLE_WALL_TORCH =
            BLOCKS.register("burnable_wall_torch",
                    () -> new BurnableWallTorchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WALL_TORCH)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
