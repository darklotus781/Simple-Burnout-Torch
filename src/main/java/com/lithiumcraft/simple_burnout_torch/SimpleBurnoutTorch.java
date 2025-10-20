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
