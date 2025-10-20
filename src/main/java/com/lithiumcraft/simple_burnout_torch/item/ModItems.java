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
