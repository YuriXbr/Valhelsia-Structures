package com.stal111.valhelsia_structures.world.structures;

import com.mojang.datafixers.Dynamic;
import com.stal111.valhelsia_structures.config.StructureGenConfig;
import com.stal111.valhelsia_structures.utils.StructureUtils;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * Desert House Structure
 * Valhelsia-Structures - com.stal111.valhelsia_structures.world.structures.DesertHouseStructure
 *
 * @author Valhelsia Team
 * @version 14.0.4
 * @since 2020-05-27
 */
public class DesertHouseStructure extends AbstractValhelsiaStructure {
    private static final int CHUNK_RADIUS = 2;
    public static final String SHORT_NAME = "desert_house";

    public DesertHouseStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory) {
        super(configFactory, SHORT_NAME);
    }

    @Override
    protected int getFeatureDistance(ChunkGenerator<?> generator) {
        return StructureGenConfig.DESERT_HOUSE_DISTANCE.get();
    }

    @Override
    protected int getFeatureSeparation(ChunkGenerator<?> generator) {
        return StructureGenConfig.DESERT_HOUSE_SEPARATION.get();
    }

    @Override
    protected double getSpawnChance() {
        return StructureGenConfig.DESERT_HOUSE_SPAWN_CHANCE.get();
    }

    @Override
    protected int getSeedModifier() {
        return 14862926;
    }

    @Override
    @Nonnull
    public IStartFactory getStartFactory() {
        return Start::new;
    }

    public static class Start extends MarginedStructureStart {

        public Start(Structure<?> structure, int chunkX, int chunkY, Biome biome, MutableBoundingBox bounds, int reference, long seed) {
            super(structure, chunkX, chunkY, biome, bounds, reference, seed);
        }

        @Override
        public void init(@Nonnull ChunkGenerator<?> generator, @Nonnull TemplateManager templateManager, int chunkX, int chunkZ, @Nonnull Biome biome) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            BlockPos position = StructureUtils.getSurfaceStructurePosition(generator, CHUNK_RADIUS, rotation, chunkX, chunkZ);
            DesertHousePieces.generate(generator, templateManager, position, this.components, this.rand);
            this.recalculateStructureSize();
        }
    }
}
