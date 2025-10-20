package com.lithiumcraft.simple_burnout_torch.util;

import com.lithiumcraft.simple_burnout_torch.SimpleBurnoutTorch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> SEA_LANTERN_LIKE = createTag("sea_lantern_like");
        public static final TagKey<Block> GLOWSTONE_LIKE = createTag("glowstone_like");
        public static final TagKey<Block> JACK_O_LANTERN_LIKE = createTag("jack_o_lantern_like");
        public static final TagKey<Block> BLOCKED_LIGHTS = createTag("blocked_lights");


        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(SimpleBurnoutTorch.MOD_ID, name));
        }

        private static TagKey<Block> createTag(String namespace, String key) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(namespace, key));
        }
    }

    public static class Items {
//        public static final TagKey<Item> WRENCHES = createTag("wrenches");
//        public static final TagKey<Item> ANDESITE_ALLOY_INGOTS = createTag("c", "ingots/andesite_alloy");

        public static final TagKey<Item> TORCH_RELIGHTERS = createTag("torch_relighting_items");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SimpleBurnoutTorch.MOD_ID, name));
        }

        private static TagKey<Item> createTag(String namespace, String key) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, key));
        }
    }
}
