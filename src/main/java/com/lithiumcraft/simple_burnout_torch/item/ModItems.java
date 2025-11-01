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

package com.lithiumcraft.simple_burnout_torch.item;

import com.lithiumcraft.simple_burnout_torch.SimpleBurnoutTorch;
import com.lithiumcraft.simple_burnout_torch.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SimpleBurnoutTorch.MOD_ID);

    // Single torch item that places either floor or wall variant
    public static final DeferredItem<Item> BURNABLE_TORCH_ITEM =
            ITEMS.register("burnable_torch", () ->
                    new StandingAndWallBlockItem(
                            ModBlocks.BURNABLE_TORCH.get(),       // standing version
                            ModBlocks.BURNABLE_WALL_TORCH.get(),  // wall version
                            new Item.Properties(),                // normal properties
                            Direction.DOWN                        // attachment direction (same as vanilla)
                    ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
