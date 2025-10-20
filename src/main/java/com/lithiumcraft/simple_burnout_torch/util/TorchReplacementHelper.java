package com.lithiumcraft.simple_burnout_torch.util;

import com.lithiumcraft.simple_burnout_torch.Config;
import com.lithiumcraft.simple_burnout_torch.SimpleBurnoutTorch;
import com.lithiumcraft.simple_burnout_torch.block.ModBlocks;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Set;

import static com.lithiumcraft.simple_burnout_torch.util.ModTags.Blocks.*;

public final class TorchReplacementHelper {
    private TorchReplacementHelper() {}

    // -------- messaging throttle --------
    private static final Long2LongOpenHashMap LAST_MSG = new Long2LongOpenHashMap();
    private static final long MSG_COOLDOWN_MS = 500L;

    // -------- dimension gate --------
    public static boolean shouldReplaceIn(Level level) {
        ResourceKey<Level> dim = level.dimension();
        return Config.dimensions.contains(dim);
    }

    // -------- type helpers (is[Type]Block) --------
    public static boolean isTorchBlock(BlockState s) {
        return s.getBlock() instanceof TorchBlock;
    }

    public static boolean isLanternBlock(BlockState s) {
        return s.getBlock() instanceof LanternBlock;
    }

    /** Glowstone (vanilla) + modded variants via tag. */
    public static boolean isGlowstoneLike(BlockState s) {
        return s.is(GLOWSTONE_LIKE) || s.getBlock() == Blocks.GLOWSTONE;
    }

    /** Sea lantern (vanilla) + modded variants via tag. */
    public static boolean isSeaLanternLike(BlockState s) {
        return s.is(SEA_LANTERN_LIKE);
    }

    /** Jack o’ lanterns via tag first; otherwise detect “lit carved pumpkins”. */
    public static boolean isJackOLanternLike(Level level, BlockPos pos, BlockState s) {
        if (s.is(JACK_O_LANTERN_LIKE)) return true;
        if (!(s.getBlock() instanceof CarvedPumpkinBlock)) return false;
        return s.getLightEmission(level, pos) > 0; // lit -> treat as jack o' lantern
    }

    /** Optional: redstone torches */
    public static boolean isRedstoneTorch(BlockState s) {
        Block b = s.getBlock();
        return b == Blocks.REDSTONE_TORCH || b == Blocks.REDSTONE_WALL_TORCH;
    }

    // -------- our blocks / vanilla torch allow list --------
    public static boolean isOurTorch(BlockState s) {
        Block b = s.getBlock();
        return b == ModBlocks.BURNABLE_TORCH.get() || b == ModBlocks.BURNABLE_WALL_TORCH.get();
    }

    /** Use Config.allowedBlocks (ResourceLocation of blocks you allow). */
    public static boolean isAllowed(BlockState s) {
        ResourceLocation id = getBlockId(s);
        return id != null && Config.allowedBlocks.contains(id);
    }

    /** Use Config.deniedBlocks OR tag (and types) to block. */
    public static boolean isBlocked(Level level, BlockPos pos, BlockState s) {
        // Explicit deny list first
        ResourceLocation id = getBlockId(s);
        if (id != null && Config.deniedBlocks.contains(id)) return true;

        // Global deny tag (datapack extendable)
        if (s.is(BLOCKED_LIGHTS)) return true;

        // Type-based denies (default behavior)
        if (isLanternBlock(s) || isSeaLanternLike(s) || isGlowstoneLike(s) || isJackOLanternLike(level, pos, s)) return true;

        // If you want redstone torches blocked by default, uncomment:
        // if (isRedstoneTorch(s)) return true;

        // Mod torches: block if it’s a TorchBlock and NOT in allowed list (vanilla torches are in allowed list)
        if (isTorchBlock(s) && !isAllowed(s) && !isOurTorch(s)) return true;

        return false;
    }

    // -------- mapping/rewrite for allowed vanilla torches --------
    public static boolean isAllowedVanillaTorch(BlockState s) {
        // “Allowed” is coming from config; if you want a fast path specific to vanilla:
        Block b = s.getBlock();
        return b == Blocks.TORCH || b == Blocks.WALL_TORCH; // soul variants below if you add them in config or here
    }

    /** Map allowed vanilla torch → our burned-out variants (preserves FACING for wall). */
    public static BlockState mapAllowedVanillaTorch(BlockState state) {
        Block b = state.getBlock();
        if (b == Blocks.WALL_TORCH) {
            Direction facing = state.hasProperty(WallTorchBlock.FACING)
                    ? state.getValue(WallTorchBlock.FACING) : Direction.NORTH;
            return ModBlocks.BURNABLE_WALL_TORCH.get().defaultBlockState().setValue(WallTorchBlock.FACING, facing);
        }
        if (b == Blocks.TORCH) {
            return ModBlocks.BURNABLE_TORCH.get().defaultBlockState();
        }
        return state;
    }

    /** Used by @ModifyVariable to keep vanilla pipeline intact (no cancel). */
    public static BlockState rewriteIfAllowed(Level level, BlockState state) {
        if (!shouldReplaceIn(level)) return state;
        if (isOurTorch(state)) return state;
        // If you prefer pure-config, just use isAllowed(state) instead of isAllowedVanillaTorch(state)
        return isAllowed(state) ? mapAllowedVanillaTorch(state) : state;
    }

    // -------- decision facade for mixin (keeps LevelMixin tiny) --------
    public enum PlaceDecision { PASS, REWRITE, BLOCK }

    public static PlaceDecision onPlaceDecision(Level level, BlockPos pos, BlockState incoming) {
        if (!shouldReplaceIn(level) || isOurTorch(incoming)) return PlaceDecision.PASS;

        if (isAllowed(incoming)) {
            return PlaceDecision.REWRITE; // allowed → we’ll rewrite to our variant (torch only)
        }

        if (isBlocked(level, pos, incoming)) {
            return PlaceDecision.BLOCK;
        }

        return PlaceDecision.PASS;
    }

    // -------- UX helper --------
    public static void notifyNearestPlayer(Level level, BlockPos pos, Component msg) {
        if (!(level instanceof ServerLevel sl)) return;
        long now = System.currentTimeMillis();
        long key = pos.asLong();
        long last = LAST_MSG.getOrDefault(key, 0L);
        if (now - last < MSG_COOLDOWN_MS) return;
        LAST_MSG.put(key, now);

        Player p = sl.getNearestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 6.0, false);
        if (p != null) p.displayClientMessage(msg, true);
    }

    public static Component defaultBlockedMessage() {
        return Component.translatable("message.simple_burnout_torch.light_blocked");
    }

    // -------- utils --------
    public static ResourceLocation getBlockId(BlockState s) {
        return s.getBlockHolder().unwrapKey().map(k -> k.location()).orElse(null);
    }
}
