package net.sleeplessdev.chromaticfoliage.config;

import net.minecraftforge.common.config.Config;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;

@Config(modid = ChromaticFoliage.ID, name = ChromaticFoliage.ID + "/features", category = "features")
public final class ChromaFeatureConfig {

    @Config.Name("grass_blocks")
    @Config.Comment("Register chromatic grass blocks")
    @Config.RequiresMcRestart
    public static boolean grassBlocks = true;

    @Config.Name("oak_leaves")
    @Config.Comment("Register chromatic oak leaves")
    @Config.RequiresMcRestart
    public static boolean oakLeaves = true;

    @Config.Name("spruce_leaves")
    @Config.Comment("Register chromatic spruce leaves")
    @Config.RequiresMcRestart
    public static boolean spruceLeaves = true;

    @Config.Name("birch_leaves")
    @Config.Comment("Register chromatic birch leaves")
    @Config.RequiresMcRestart
    public static boolean birchLeaves = true;

    @Config.Name("jungle_leaves")
    @Config.Comment("Register chromatic jungle leaves")
    @Config.RequiresMcRestart
    public static boolean jungleLeaves = true;

    @Config.Name("acacia_leaves")
    @Config.Comment("Register chromatic acacia leaves")
    @Config.RequiresMcRestart
    public static boolean acaciaLeaves = true;

    @Config.Name("dark_oak_leaves")
    @Config.Comment("Register chromatic dark oak leaves")
    @Config.RequiresMcRestart
    public static boolean darkOakLeaves = true;

    @Config.Name("vines")
    @Config.Comment("Register chromatic vines")
    public static boolean vines = true;

    private ChromaFeatureConfig() {}

}
