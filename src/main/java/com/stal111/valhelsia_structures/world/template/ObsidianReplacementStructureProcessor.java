package com.stal111.valhelsia_structures.world.template;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import com.stal111.valhelsia_structures.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ObsidianReplacementStructureProcessor extends StructureProcessor {

    @Nullable
    @Override
    public Template.BlockInfo process(@Nonnull IWorldReader world, @Nonnull BlockPos pos, @Nonnull Template.BlockInfo blockInfo0, @Nonnull Template.BlockInfo blockInfo, @Nonnull PlacementSettings placementSettings, @Nullable Template template) {
        Block block = blockInfo.state.getBlock();
        if (block != Blocks.OBSIDIAN) {
            return blockInfo;
        } else {
            return new Template.BlockInfo(blockInfo.pos, ModBlocks.SPECIAL_SPAWNER.get().getDefaultState(), null);
        }
    }

    @Override
    @Nonnull
    protected IStructureProcessorType getType() {
        return IStructureProcessorType.JIGSAW_REPLACEMENT;
    }

    @Override
    @Nonnull
    protected <T> Dynamic<T> serialize0(@Nonnull DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.emptyMap());
    }
}
