package com.lithiumcraft.simple_burnout_torch.datagen;

import com.lithiumcraft.simple_burnout_torch.SimpleBurnoutTorch;
import com.lithiumcraft.simple_burnout_torch.block.BurnableTorchBlock;
import com.lithiumcraft.simple_burnout_torch.block.BurnableWallTorchBlock;
import com.lithiumcraft.simple_burnout_torch.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.WallTorchBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SimpleBurnoutTorch.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        // ========== STANDING TORCH ==========

        // Model references
        ModelFile litTorch = new ModelFile.UncheckedModelFile("minecraft:block/torch");
        ModelFile smolderingTorch = models().torch("burnable_torch_smoldering", modLoc("block/burnable_torch_smoldering"));
        ModelFile unlitTorch = models().torch("burnable_torch_unlit", modLoc("block/burnable_torch_unlit"));

        getVariantBuilder(ModBlocks.BURNABLE_TORCH.get())
                .forAllStates(state -> {
                    int litstate = state.getValue(BurnableTorchBlock.LITSTATE);
                    ModelFile model;
                    switch (litstate) {
                        case 2 -> model = litTorch;
                        case 1 -> model = smolderingTorch;
                        default -> model = unlitTorch;
                    }
                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .build();
                });

        // Item model for standing torch (always show lit)
        simpleBlockItem(ModBlocks.BURNABLE_TORCH.get(), litTorch);

        // ========== WALL TORCH ==========
        ModelFile litWallTorch = new ModelFile.UncheckedModelFile("minecraft:block/wall_torch");
        ModelFile smolderingWallTorch = models()
                .withExistingParent("burnable_wall_torch_smoldering", mcLoc("block/template_torch_wall"))
                .texture("torch", modLoc("block/burnable_torch_smoldering"));
        ModelFile unlitWallTorch = models()
                .withExistingParent("burnable_wall_torch_unlit", mcLoc("block/template_torch_wall"))
                .texture("torch", modLoc("block/burnable_torch_unlit"));

        getVariantBuilder(ModBlocks.BURNABLE_WALL_TORCH.get())
                .forAllStates(state -> {
                    int yRot;
                    Direction facing = state.getValue(WallTorchBlock.FACING);
                    switch (facing) {
                        case NORTH -> yRot = 270;
                        case SOUTH -> yRot = 90;
                        case WEST -> yRot = 180;
                        default -> yRot = 0;
                    }

                    int litstate = state.getValue(BurnableWallTorchBlock.LITSTATE);
                    ModelFile model;
                    switch (litstate) {
                        case 2 -> model = litWallTorch;
                        case 1 -> model = smolderingWallTorch;
                        default -> model = unlitWallTorch;
                    }

                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY(yRot)
                            .build();
                });

        // Item model (uses the lit standing torch)
        simpleBlockItem(ModBlocks.BURNABLE_WALL_TORCH.get(), litTorch);
    }
}
