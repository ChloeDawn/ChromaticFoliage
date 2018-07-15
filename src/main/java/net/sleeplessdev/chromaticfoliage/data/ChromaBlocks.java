package net.sleeplessdev.chromaticfoliage.data;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;

@ObjectHolder(ChromaticFoliage.ID)
public final class ChromaBlocks {
    public static final Block CHROMATIC_GRASS = Blocks.AIR;
    public static final Block CHROMATIC_OAK_LEAVES = Blocks.AIR;
    public static final Block CHROMATIC_SPRUCE_LEAVES = Blocks.AIR;
    public static final Block CHROMATIC_BIRCH_LEAVES = Blocks.AIR;
    public static final Block CHROMATIC_JUNGLE_LEAVES = Blocks.AIR;
    public static final Block CHROMATIC_ACACIA_LEAVES = Blocks.AIR;
    public static final Block CHROMATIC_DARK_OAK_LEAVES = Blocks.AIR;
    public static final Block CHROMATIC_VINE = Blocks.AIR;

    public static final Block EMISSIVE_GRASS = Blocks.AIR;
    public static final Block EMISSIVE_OAK_LEAVES = Blocks.AIR;
    public static final Block EMISSIVE_SPRUCE_LEAVES = Blocks.AIR;
    public static final Block EMISSIVE_BIRCH_LEAVES = Blocks.AIR;
    public static final Block EMISSIVE_JUNGLE_LEAVES = Blocks.AIR;
    public static final Block EMISSIVE_ACACIA_LEAVES = Blocks.AIR;
    public static final Block EMISSIVE_DARK_OAK_LEAVES = Blocks.AIR;
    public static final Block EMISSIVE_VINE = Blocks.AIR;

    private ChromaBlocks() {
        throw new UnsupportedOperationException("Cannot instantiate " + this.getClass());
    }
}
