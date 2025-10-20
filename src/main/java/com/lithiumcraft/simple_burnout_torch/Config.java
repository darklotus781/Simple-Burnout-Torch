package com.lithiumcraft.simple_burnout_torch;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = SimpleBurnoutTorch.MOD_ID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static {
        BUILDER.comment(
                "===================================================",
                " Simple Burnout Torch Configuration",
                "",
                " This file contains gameplay settings for the mod.",
                " You can safely edit these values or reset them by",
                " deleting the file and letting it regenerate.",
                "",
                "",
                " This mod replaces vanilla torches with burnout versions.",
                " The list of allowed blocks is defined below.  These blocks",
                " are allowed to be placed as light sources along side",
                " minecraft vanilla torches which are replaced by burnout",
                " versions.",
                "",
                " The list of disallowed blocks prevents those blocks from",
                " being placed as light sources in the dimensions configured.",
                "",
                "===================================================",
                "",
                ""
        );
    }

    // --- Dimension List ---
    private static final ModConfigSpec.ConfigValue<List<? extends String>> DIMENSION_STRINGS =
            BUILDER.comment("A list of dimensions where Burnout Torch is forced. Example: dimension_expansion:deep_beneath, theupsidedown:upside_down")
                    .defineList(
                            "dimensions",
                            () -> List.of("dimension_expansion:deep_beneath", "theupsidedown:upside_down"),
                            () -> "allthemodium:the_other",
                            Config::validateRL
                    );

    // --- Extinguish Chance ---
    public static final ModConfigSpec.DoubleValue EXTINGUISH_CHANCE = BUILDER
            .comment("Per-tick chance that a Burnable Torch will extinguish. Range: 0.0 (never) to 1.0 (always). Default: 0.1 (10%).")
            .defineInRange("extinguishChance", 0.1D, 0.0D, 1.0D);

    // --- Torch Light Level (burning) ---
    private static final ModConfigSpec.IntValue TORCH_LIGHT_LEVEL = BUILDER
            .comment("Light level for Burnable Torch (1–15). Default: 8")
            .defineInRange("torchLightLevel", 8, 1, 15);

    // --- Torch Light Level (extinguished) ---
    private static final ModConfigSpec.IntValue EXTINGUISHED_TORCH_LIGHT_LEVEL = BUILDER
            .comment("Light level for Extinguished Torch (0–15). Default: 0")
            .defineInRange("extinguishedTorchLightLevel", 0, 0, 15);

    // --- Consume Relighters ---
    public static final ModConfigSpec.BooleanValue CONSUME_RELIGHTERS = BUILDER
            .comment(
                    "If true, items used to relight torches (e.g. Flint & Steel, Fire Charge, Blaze Rod, etc.)",
                    "will be consumed or damaged when used.",
                    "If false, relighting items will not be consumed or damaged."
            )
            .define("consumeRelighters", true);

//    // --- Require relighter to place lit torches ---
//    public static final ModConfigSpec.BooleanValue REQUIRE_RELIGHTER_FOR_PLACEMENT = BUILDER
//            .comment("""
//                If true, newly placed torches will start unlit unless the player has a valid
//                relighter item (from #simple_burnout_torch:torch_relighting_items or flint_and_steel)
//                in their offhand. If a relighter is found, the torch is placed lit and the item
//                will be consumed or damaged if 'consumeRelighters' is also enabled.""")
//            .define("requireRelighterForPlacement", false);

    // --- Allowed Blocks ---
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ALLOWED_BLOCK_STRINGS = BUILDER
            .comment("Block IDs of light sources that can be placed alongside Burnable Torch.")
            .comment("Vanilla Torches must be listed here as they are replaced with the Burnout Torch.")
            .defineList("allowedBlocks",
                    () -> List.of("minecraft:torch", "minecraft:wall_torch"),
                    () -> "minecraft:torch",
                    Config::validateRL);

    // --- Denied Blocks ---
    private static final ModConfigSpec.ConfigValue<List<? extends String>> DENIED_BLOCK_STRINGS = BUILDER
            .comment("Block IDs that are explicitly denied (cannot be placed as light sources).")
            .defineList("deniedBlocks",
                    () -> List.of("amendments:wall_lantern", "torchmaster:feral_flare_lantern", "torchmaster:megatorch", "actuallyadditions:tiny_torch"),
                    () -> "minecraft:lantern",
                    Config::validateRL);

    static final ModConfigSpec SPEC = BUILDER.build();

    // --- Cached values ---
    public static Set<ResourceKey<Level>> dimensions;
    public static float extinguishChance;
    public static int torchLightLevel;
    public static int extinguishedTorchLightLevel;
    public static boolean consumeRelighters;
//    public static boolean requireRelighterForPlacement;
    public static Set<ResourceLocation> allowedBlocks;
    public static Set<ResourceLocation> deniedBlocks;

    private static boolean validateRL(Object obj) {
        if (!(obj instanceof String s)) return false;
        try {
            ResourceLocation.parse(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        dimensions = DIMENSION_STRINGS.get().stream()
                .map(s -> ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(s)))
                .collect(Collectors.toSet());
        extinguishChance = EXTINGUISH_CHANCE.get().floatValue();
        torchLightLevel = TORCH_LIGHT_LEVEL.get();
        extinguishedTorchLightLevel = EXTINGUISHED_TORCH_LIGHT_LEVEL.get();
        consumeRelighters = CONSUME_RELIGHTERS.get();
//        requireRelighterForPlacement = REQUIRE_RELIGHTER_FOR_PLACEMENT.get();

        allowedBlocks = ALLOWED_BLOCK_STRINGS.get().stream()
                .map(ResourceLocation::parse)
                .collect(Collectors.toSet());

        deniedBlocks = DENIED_BLOCK_STRINGS.get().stream()
                .map(ResourceLocation::parse)
                .collect(Collectors.toSet());
    }
}
