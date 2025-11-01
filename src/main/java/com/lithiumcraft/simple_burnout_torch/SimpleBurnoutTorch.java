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

package com.lithiumcraft.simple_burnout_torch;

import com.lithiumcraft.simple_burnout_torch.block.ModBlocks;
import com.lithiumcraft.simple_burnout_torch.event.ClientEvents;
import com.lithiumcraft.simple_burnout_torch.item.ModItems;
import com.lithiumcraft.simple_burnout_torch.util.ModTags;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SimpleBurnoutTorch.MOD_ID)
public class SimpleBurnoutTorch
{
    public static final String MOD_ID = "simple_burnout_torch";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SimpleBurnoutTorch(IEventBus modEventBus, Dist dist, ModContainer modContainer)
    {
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ClientEvents.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
