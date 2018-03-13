package net.sleeplessdev.chromaticfoliage.config;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;

@Config(modid = ChromaticFoliage.ID, name = ChromaticFoliage.ID + "/client", category = "")
public final class ChromaClientConfig {

    public static final Blocks BLOCKS = new Blocks();
    public static final Colors COLORS = new Colors();
    public static final Info INFO = new Info();

    private ChromaClientConfig() {}

    public static final class Blocks {
        @Config.Name("snow_layers")
        @Config.Comment("Tint snow layers when above a chromatic grass block")
        @Config.RequiresMcRestart
        public boolean snowLayers = true;

        @Config.Name("grass_plants")
        @Config.Comment("Tint grass plants when above a chromatic grass block")
        @Config.RequiresMcRestart
        public boolean grassPlants = true;

        private Blocks() {}
    }

    public static final class Colors {
        public int black = 0x1D1D21;
        public int red = 0xB02E26;
        public int green = 0x5E7C16;
        public int brown = 0x835432;
        public int blue = 0x3C44AA;
        public int purple = 0x8932B8;
        public int cyan = 0x169C9C;
        @Config.Name("light_gray")
        public int lightGray = 0x9D9D97;
        public int gray = 0x474F52;
        public int pink = 0xF38BAA;
        public int lime = 0x80C71F;
        public int yellow = 0xFED83D;
        @Config.Name("light_blue")
        public int lightBlue = 0x3AB3DA;
        public int magenta = 0xC74EBD;
        public int orange = 0xF9801D;
        public int white = 0xF9FFFE;

        private IntList lastColors = null;
        private IntList colors = toList();

        private Colors() {} // TODO Move to JSON for resource pack override support?

        public void onConfigPre() {
            lastColors = colors;
        }

        public void onConfigPost() {
            if (lastColors != (colors = toList())) {
                ChromaticFoliage.LOGGER.info("Reloading renderers...");
                FMLCommonHandler.instance().reloadRenderers();
            }
        }

        private IntList toList() {
            return new IntArrayList(new int[] {
                    black, red, green, brown,
                    blue, purple, cyan, lightGray,
                    gray, pink, lime, yellow,
                    lightBlue, magenta, orange, white
            });
        }
    }

    public static final class Info {
        @Config.Name("item_tooltip")
        @Config.Comment("Display the color variant of the chromatic block in the item tooltip")
        public boolean itemTooltip = true;

        @Config.Name("waila_color")
        @Config.Comment({ "Display the color variant of the chromatic block in Hwyla/Waila",
                          "This config value is ignored if Hwyla/Waila is not present" })
        public boolean wailaColor = true;

        @Config.Name("waila_illuminated")
        @Config.Comment({ "Display an additional Hwyla/Waila tooltip for illuminated chromatic blocks",
                          "This config value is ignored if Hwyla/Waila is not present" })
        public boolean wailaIlluminated = true;

        private Info() {}
    }

}
