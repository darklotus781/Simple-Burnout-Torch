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
