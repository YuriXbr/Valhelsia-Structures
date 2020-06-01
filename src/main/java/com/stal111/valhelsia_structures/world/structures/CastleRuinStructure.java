package com.stal111.valhelsia_structures.world.structures;

import com.mojang.datafixers.Dynamic;
import com.stal111.valhelsia_structures.ValhelsiaStructures;
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
 * Small Castle Structure
 * Valhelsia Structures - com.stal111.valhelsia_structures.world.structures.SmallCastleStructure
 *
 * @author Valhelsia Team
 * @version 14.0.4
 * @since 2019-10-31
 */

public class CastleRuinStructure extends AbstractValhelsiaStructure {
    public static final String SHORT_NAME = "castle_ruin";
    public static final String FULL_NAME = ValhelsiaStructures.MOD_ID + ":" + SHORT_NAME;

    private static final int CHUNK_RADIUS = 2;
    private static final int SEED_MODIFIER = 14357618;

    public CastleRuinStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory) {
        super(configFactory, SHORT_NAME);
    }

    @Override
    protected int getSeedModifier() {
        return SEED_MODIFIER;
    }

    @Override
    public @Nonnull IStartFactory getStartFactory() {
        return Start::new;
    }

    @Override
    public @Nonnull String getStructureName() {
        return FULL_NAME;
    }


    @Override
    protected int getFeatureDistance(ChunkGenerator<?> generator) {
        return StructureGenConfig.CASTLE_RUIN_DISTANCE.get();
    }

    @Override
    protected int getFeatureSeparation(ChunkGenerator<?> generator) {
        return StructureGenConfig.CASTLE_RUIN_SEPARATION.get();
    }

    @Override
    protected double getSpawnChance() {
        return StructureGenConfig.CASTLE_RUIN_SPAWN_CHANCE.get();
    }

    @Override
    public int getSize() {
        return CHUNK_RADIUS;
    }

    public static class Start extends MarginedStructureStart {
        public Start(Structure<?> structure, int chunkX, int chunkY, Biome biome, MutableBoundingBox bounds, int reference, long seed) {
            super(structure, chunkX, chunkY, biome, bounds, reference, seed);
        }

        @Override
        public void init(@Nonnull ChunkGenerator<?> generator, @Nonnull TemplateManager templateManager, int chunkX, int chunkZ, @Nonnull Biome biome) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            BlockPos position = StructureUtils.getSurfaceStructurePosition(generator, CHUNK_RADIUS, rotation, chunkX, chunkZ);
            CastleRuinPieces.generate(generator, templateManager, position, this.components, this.rand);
            this.recalculateStructureSize();
        }
    }
}