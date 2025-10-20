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
