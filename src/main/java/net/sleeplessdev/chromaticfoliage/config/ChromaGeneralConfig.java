package net.sleeplessdev.chromaticfoliage.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;

@Config(modid = ChromaticFoliage.ID, name = ChromaticFoliage.ID + "/general")
public final class ChromaGeneralConfig {
    @Name("chroma_recoloring")
    @Comment({ "Register shapeless recipes that allow recoloring of chromatic blocks",
               "In-world recoloring requires the in-world interaction to be enabled" })
    @RequiresMcRestart
    public static boolean chromaRecoloring = true;

    @Name("in_world_interaction")
    @Comment({ "Allow coloring of blocks when right-clicking them with dyes",
               "Only functions on blocks with a chromatic variant",
               "This config does not control in-world illumination" })
    public static boolean inWorldInteraction = true;

    @Name("in_world_illumination")
    @Comment({ "Allow right-clicking glowstone dust on chromatic blocks to illuminated them",
               "Produces a small amount of light, and allows the block to glow in the dark",
               "Illuminated blocks will drop their glowstone dust when broken" })
    public static boolean inWorldIllumination = true;

    @Name("grass_spreads_to_dirt")
    @Comment("Chromatic grass blocks spread onto dirt over time")
    @RequiresMcRestart
    public static boolean grassSpreadDirt = true;

    @Name("grass_spreads_to_grass")
    @Comment("Chromatic grass blocks spread onto regular grass blocks over time")
    @RequiresMcRestart
    public static boolean grassSpreadGrass = false;

    @Name("leaves_spread_to_leaves")
    @Comment("Chromatic leaves spread onto regular leaves over time")
    @RequiresMcRestart
    public static boolean leavesSpreadLeaves = false;

    @Name("replaceable_vines")
    @Comment({ "Allow vines to be replaced by other blocks, including themselves",
               "Set this value to true for vanilla behaviour" })
    @RequiresMcRestart
    public static boolean replaceableVines = false;

    private ChromaGeneralConfig() {
        throw new UnsupportedOperationException("Cannot instantiate " + this.getClass());
    }
}
