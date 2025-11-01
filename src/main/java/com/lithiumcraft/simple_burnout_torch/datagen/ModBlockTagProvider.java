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
import com.lithiumcraft.simple_burnout_torch.block.ModBlocks;
import com.lithiumcraft.simple_burnout_torch.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SimpleBurnoutTorch.MOD_ID, existingFileHelper);
    }

    private static final ResourceLocation CHIPPED_SEA_LANTERN_TAG =
            ResourceLocation.fromNamespaceAndPath("chipped", "sea_lantern");
    private static final ResourceLocation CHIPPED_JACK_O_LANTERN_TAG =
            ResourceLocation.fromNamespaceAndPath("chipped", "jack_o_lantern");
    private static final ResourceLocation STUFF_AND_THINGS_LIGHT_BLOCKS =
            ResourceLocation.fromNamespaceAndPath("stuff_and_things", "light_blocks");

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Blocks.SEA_LANTERN_LIKE)
                .add(Blocks.SEA_LANTERN)
                .addOptionalTag(CHIPPED_SEA_LANTERN_TAG);   // <- optional

        // If you have a JACK_O_LANTERN_LIKE tag:
        tag(ModTags.Blocks.JACK_O_LANTERN_LIKE)
                .add(Blocks.JACK_O_LANTERN)
                .addOptionalTag(CHIPPED_JACK_O_LANTERN_TAG); // <- optional

        tag(ModTags.Blocks.GLOWSTONE_LIKE)
                .add(Blocks.GLOWSTONE);

        tag(ModTags.Blocks.BLOCKED_LIGHTS)
                .addTag(ModTags.Blocks.SEA_LANTERN_LIKE)
                .addTag(ModTags.Blocks.GLOWSTONE_LIKE)
                .addTag(ModTags.Blocks.JACK_O_LANTERN_LIKE)
                .addOptionalTag(STUFF_AND_THINGS_LIGHT_BLOCKS);
    }
}
