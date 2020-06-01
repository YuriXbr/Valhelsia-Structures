package com.stal111.valhelsia_structures.utils;

import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * ValhelsiaStructure Utils
 * ValhelsiaStructure - com.stal111.valhelsia_structures.utils.StructureUtils
 *
 * @author Valhelsia Team
 * @version 14.0.3
 * @since 2019-10-31
 */
public class StructureUtils {
    /**
     * Get Random Direction
     * @param random Instance of Random to use.
     * @return A random cardinal direction, of N/S/E/W.
     */
    public static Direction getRandomDir(final Random random) {
        return Direction.byHorizontalIndex(random.nextInt(4));
    }

    /**
     * Gets the lowest height of four corners.
     * @param world The world to use the heightmap from.
     * @param x X Coordinate.
     * @param z Y Coordinate.
     * @param xSize X Size.
     * @param zSize Z Size.
     * @return The lowest height of the four corners.
     */
    public static int getLowestHeight(IWorld world, int x, int z, int xSize, int zSize) {
        int h0 = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, x, z);
        int h1 = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, x + xSize, z);
        int h2 = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, x, z + zSize);
        int h3 = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, x + xSize, z + zSize);
        return Math.min(Math.min(h0, h1), Math.min(h2, h3));
    }


    public static BlockPos getSurfaceStructurePosition(@Nonnull ChunkGenerator<?> generator, int size, Rotation rotation, int chunkX, int chunkZ) {
        int xOffset = size * 16;
        int zOffset = size * 16;

        int x = (chunkX << 4);
        int z = (chunkZ << 4);

        int i1 = generator.func_222531_c(x, z, Heightmap.Type.WORLD_SURFACE_WG);
        int j1 = generator.func_222531_c(x, z + zOffset, Heightmap.Type.WORLD_SURFACE_WG);
        int k1 = generator.func_222531_c(x + xOffset, z, Heightmap.Type.WORLD_SURFACE_WG);
        int l1 = generator.func_222531_c(x + xOffset, z + zOffset, Heightmap.Type.WORLD_SURFACE_WG);
        int minHeight = Math.min(Math.min(i1, j1), Math.min(k1, l1));

        return new BlockPos(x + (xOffset / 2), minHeight - 2, z + (zOffset / 2));
    }
}
