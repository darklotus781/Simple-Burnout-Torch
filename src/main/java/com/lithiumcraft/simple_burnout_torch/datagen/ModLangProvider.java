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

package com.lithiumcraft.simple_burnout_torch.datagen;

import com.lithiumcraft.simple_burnout_torch.SimpleBurnoutTorch;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {

    public ModLangProvider(PackOutput output) {
        super(output, SimpleBurnoutTorch.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("block.simple_burnout_torch.burned_out_torch", "Extinguished Torch");
        add("block.simple_burnout_torch.burned_out_wall_torch", "Extinguished Torch");
        add("block.simple_burnout_torch.burnable_torch", "Torch");
        add("block.simple_burnout_torch.burnable_wall_torch", "Torch");
        add("message.simple_burnout_torch.light_blocked", "That light source doesn't work here.");
    }
}
