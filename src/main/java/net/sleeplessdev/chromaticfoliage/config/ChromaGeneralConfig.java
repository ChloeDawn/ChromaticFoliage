package net.sleeplessdev.chromaticfoliage.config;

import net.minecraftforge.common.config.Config;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;

@Config(modid = ChromaticFoliage.ID, name = ChromaticFoliage.ID + "/general")
public final class ChromaGeneralConfig {

    @Config.Name("chroma_recoloring")
    @Config.Comment({"Register shapeless recipes that allow recoloring of chromatic blocks",
                     "In-world recoloring requires the in-world interaction to be enabled"})
    @Config.RequiresMcRestart
    public static boolean chromaRecoloring = true;

    @Config.Name("in_world_interaction")
    @Config.Comment({"Allow coloring of blocks when right-clicking them with dyes",
                     "Only functions on blocks with a chromatic variant",
                     "This config does not control in-world illumination"})
    public static boolean inWorldInteraction = true;

    @Config.Name("in_world_illumination")
    @Config.Comment({"Allow right-clicking glowstone dust on chromatic blocks to illuminated them",
                     "Produces a small amount of light, and allows the block to glow in the dark",
                     "Illuminated blocks will drop their glowstone dust when broken"})
    public static boolean inWorldIllumination = true;

    private ChromaGeneralConfig() {}

}
