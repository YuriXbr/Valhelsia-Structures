package com.stal111.valhelsia_structures.world.structures;

import com.mojang.datafixers.Dynamic;
import com.stal111.valhelsia_structures.ValhelsiaStructures;
import com.stal111.valhelsia_structures.config.StructureGenConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Function;

/**
 * Abstract Overworld Structure
 * Valhelsia Structures - com.stal111.valhelsia_structures.world.structures.AbstractOverworldStructure
 *
 * Serves as a way to reduce duplicate code in structures - this has sensible defaults for most surface structures
 * but can be overridden if needed.
 *
 * @author Valhelsia Team
 * @version 14.0.4a
 * @since 2020-03-22
 */
public abstract class AbstractValhelsiaStructure extends ScatteredStructure<NoFeatureConfig> {

    public static final int DEFAULT_CHUNK_RADIUS = 2;
    private final String name;

    public AbstractValhelsiaStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn, String name) {
        super(configFactoryIn);
        this.name = name;
    }

    protected abstract double getSpawnChance();

    @Override
    public int getSize() {
        return DEFAULT_CHUNK_RADIUS;
    }

    @Override
    protected int getBiomeFeatureDistance(ChunkGenerator<?> generator) {
        return getFeatureDistance(generator);
    }

    protected abstract int getFeatureDistance(ChunkGenerator<?> generator);

    @Override
    protected int getBiomeFeatureSeparation(ChunkGenerator<?> generator) {
        return getFeatureSeparation(generator);
    }

    protected abstract int getFeatureSeparation(ChunkGenerator<?> generator);

    @Override
    @Nonnull
    public String getStructureName() {
        return new ResourceLocation(ValhelsiaStructures.MOD_ID, name).toString();
    }

    @Override
    protected @Nonnull ChunkPos getStartPositionForPosition(@Nonnull ChunkGenerator<?> generator, @Nonnull Random random, int chunkX, int chunkZ, int offsetX, int offsetZ) {
        int featureDistance = getFeatureDistance(generator);
        int featureSeparation = getFeatureSeparation(generator);

        int xTemp = chunkX + featureDistance * offsetX;
        int zTemp = chunkZ + featureDistance * offsetZ;
        int validChunkX = (xTemp < 0 ? xTemp - featureDistance + 1 : xTemp) / featureDistance;
        int validChunkZ = (zTemp < 0 ? zTemp - featureDistance + 1 : zTemp) / featureDistance;
        ((SharedSeedRandom)random).setLargeFeatureSeedWithSalt(generator.getSeed(), validChunkX, validChunkZ, getSeedModifier());
        validChunkX *= featureDistance;
        validChunkZ *= featureDistance;
        validChunkX += random.nextInt(featureDistance - featureSeparation) + random.nextInt(featureDistance - featureSeparation) / 2;
        validChunkZ += random.nextInt(featureDistance - featureSeparation) + random.nextInt(featureDistance - featureSeparation) / 2;
        return new ChunkPos(validChunkX, validChunkZ);
    }

    @Override
    public boolean hasStartAt(@Nonnull ChunkGenerator<?> generator, @Nonnull Random random, int chunkX, int chunkZ) {

        // Janky way of getting the world, but ¯\_(ツ)_/¯
        IWorld world = ObfuscationReflectionHelper.getPrivateValue(ChunkGenerator.class, generator, "field_222540_a");
        if (world == null || !(world.getDimension().getType() == DimensionType.OVERWORLD)) {
            // Whitelist only the Overworld.
            // TODO (VZ): Change this if we introduce structures that should spawn in any other dimension.
            return false;
        }

        ChunkPos chunkPos = getStartPositionForPosition(generator, random, chunkX, chunkZ, 0, 0);
        if (chunkX == chunkPos.x && chunkZ == chunkPos.z) {
            if (isSurfaceFlat(generator, chunkX, chunkZ)) {
                if (!generator.getBiomeProvider().getBiomesInSquare((chunkX << 4) + 9, (chunkZ << 4) + 9, getSize() * 16)
                        .stream()
                        .allMatch(biome -> generator.hasStructure(biome, this))) {
                    return false;
                }

                // Don't spawn hard up against a village.
                for (int k = chunkX - 5; k <= chunkX + 5; ++k) {
                    for (int l = chunkZ - 5; l <= chunkZ + 5; ++l) {
                        BlockPos position = new BlockPos((k << 4) + 9, 0, (l << 4) + 9);
                        if (Feature.VILLAGE.hasStartAt(generator, random, chunkX, chunkZ)) {
                            return false;
                        }
                    }
                }

                int i = chunkX >> 4;
                int j = chunkZ >> 4;
                random.setSeed((long) (i ^ j << 4) ^ generator.getSeed());
                random.nextInt();
                return random.nextDouble() < getSpawnChance();
            }
        }

        return false;
    }

    protected boolean isSurfaceFlat(@Nonnull ChunkGenerator<?> generator, int chunkX, int chunkZ) {
        // Size of the area to check.
        int offset = getSize() * 16;

        int xStart = (chunkX << 4);
        int zStart = (chunkZ << 4);

        int i1 = generator.func_222531_c(xStart, zStart, Heightmap.Type.WORLD_SURFACE_WG);
        int j1 = generator.func_222531_c(xStart, zStart + offset, Heightmap.Type.WORLD_SURFACE_WG);
        int k1 = generator.func_222531_c(xStart + offset, zStart, Heightmap.Type.WORLD_SURFACE_WG);
        int l1 = generator.func_222531_c(xStart + offset, zStart + offset, Heightmap.Type.WORLD_SURFACE_WG);
        int minHeight = Math.min(Math.min(i1, j1), Math.min(k1, l1));
        int maxHeight = Math.max(Math.max(i1, j1), Math.max(k1, l1));

        return Math.abs(maxHeight - minHeight) <= StructureGenConfig.FLATNESS_DELTA.get();
    }
}
