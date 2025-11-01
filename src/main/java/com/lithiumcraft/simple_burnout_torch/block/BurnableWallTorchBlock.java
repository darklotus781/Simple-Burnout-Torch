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

import com.lithiumcraft.simple_burnout_torch.Config;
import com.lithiumcraft.simple_burnout_torch.SimpleBurnoutTorch;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.core.registries.Registries;

public class BurnableWallTorchBlock extends WallTorchBlock {

    public static final IntegerProperty LITSTATE = IntegerProperty.create("litstate", 0, 2);
    public static final int UNLIT = 0;
    public static final int SMOLDERING = 1;
    public static final int LIT = 2;

    public static final TagKey<Item> TORCH_RELIGHTERS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(SimpleBurnoutTorch.MOD_ID, "torch_relighting_items")
    );

    public BurnableWallTorchBlock(Properties props) {
        super(ParticleTypes.FLAME, props.randomTicks().noCollission());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LITSTATE, LIT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // Must explicitly add both properties, otherwise DataGen will fail
        builder.add(FACING, LITSTATE);
    }

    // --- Everything below unchanged (light, tick, relight, etc.) ---
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        int litstate = state.getValue(LITSTATE);
        return litstate == LIT || litstate == SMOLDERING;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int litstate = state.getValue(LITSTATE);
        if (litstate == LIT && random.nextFloat() < Config.extinguishChance) {
            level.setBlock(pos, state.setValue(LITSTATE, SMOLDERING), 3);
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.3F, 1.2F);
        } else if (litstate == SMOLDERING && random.nextFloat() < Config.extinguishChance) {
            level.setBlock(pos, state.setValue(LITSTATE, UNLIT), 3);
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.0F);
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter getter, BlockPos pos) {
        int litstate = state.getValue(LITSTATE);
        if (litstate == LIT) return Config.torchLightLevel;
        if (litstate == SMOLDERING) return Math.max(1, Config.torchLightLevel / 2);
        return 0;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int litstate = state.getValue(LITSTATE);
        Direction dir = state.getValue(FACING);
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.7D;
        double z = pos.getZ() + 0.5D;
        double offset = 0.27D;
        double dx = x - dir.getStepX() * offset;
        double dy = y + 0.22D;
        double dz = z - dir.getStepZ() * offset;

        if (litstate == LIT) {
            level.addParticle(ParticleTypes.SMOKE, dx, dy, dz, 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.FLAME, dx, dy, dz, 0.0D, 0.0D, 0.0D);
        } else if (litstate == SMOLDERING) {
            if (random.nextFloat() < 0.15F)
                level.addParticle(ParticleTypes.FLAME, dx, dy, dz, 0.0D, 0.002D, 0.0D);
            if (random.nextFloat() < 0.8F)
                level.addParticle(ParticleTypes.SMOKE, dx, dy, dz, 0.0D, 0.01D, 0.0D);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                              BlockPos pos, Player player, InteractionHand hand,
                                              net.minecraft.world.phys.BlockHitResult hitResult) {
        int lit = state.getValue(LITSTATE);
        Item item = stack.getItem();

        if (lit == UNLIT || lit == SMOLDERING) {
            boolean validRelighter = item.builtInRegistryHolder().is(TORCH_RELIGHTERS)
                    || item == Items.FLINT_AND_STEEL;
            if (!validRelighter)
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(LITSTATE, LIT), 3);
                level.playSound(null, pos,
                        (item == Items.FLINT_AND_STEEL) ? SoundEvents.FLINTANDSTEEL_USE : SoundEvents.FIRECHARGE_USE,
                        SoundSource.BLOCKS, 1.0F, 1.0F);

                if (Config.consumeRelighters && !player.getAbilities().instabuild) {
                    if (stack.isDamageableItem()) {
                        stack.hurtAndBreak(1, player,
                                hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    } else stack.shrink(1);
                }
            }

            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
