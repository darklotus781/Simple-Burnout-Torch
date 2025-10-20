package com.lithiumcraft.simple_burnout_torch.event;

import com.lithiumcraft.simple_burnout_torch.SimpleBurnoutTorch;
import com.lithiumcraft.simple_burnout_torch.block.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BURNABLE_TORCH.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BURNABLE_WALL_TORCH.get(), RenderType.cutout());
        });
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ClientEvents::onClientSetup);
    }
}