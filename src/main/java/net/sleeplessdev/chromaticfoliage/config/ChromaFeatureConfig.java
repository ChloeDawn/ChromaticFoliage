package net.sleeplessdev.chromaticfoliage.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;

@Config(modid = ChromaticFoliage.ID, name = ChromaticFoliage.ID + "/features", category = "features")
public final class ChromaFeatureConfig {
    @Name("grass_blocks")
    @Comment("Register chromatic grass blocks")
    @RequiresMcRestart
    public static boolean grassBlocks = true;

    @Name("oak_leaves")
    @Comment("Register chromatic oak leaves")
    @RequiresMcRestart
    public static boolean oakLeaves = true;

    @Name("spruce_leaves")
    @Comment("Register chromatic spruce leaves")
    @RequiresMcRestart
    public static boolean spruceLeaves = true;

    @Name("birch_leaves")
    @Comment("Register chromatic birch leaves")
    @RequiresMcRestart
    public static boolean birchLeaves = true;

    @Name("jungle_leaves")
    @Comment("Register chromatic jungle leaves")
    @RequiresMcRestart
    public static boolean jungleLeaves = true;

    @Name("acacia_leaves")
    @Comment("Register chromatic acacia leaves")
    @RequiresMcRestart
    public static boolean acaciaLeaves = true;

    @Name("dark_oak_leaves")
    @Comment("Register chromatic dark oak leaves")
    @RequiresMcRestart
    public static boolean darkOakLeaves = true;

    @Name("vines")
    @Comment("Register chromatic vines")
    public static boolean vines = true;

    private ChromaFeatureConfig() {
        throw new UnsupportedOperationException("Cannot instantiate " + this.getClass());
    }
}
