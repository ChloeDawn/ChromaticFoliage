package io.github.insomniakitten.chromaticfoliage.common;

import io.github.insomniakitten.chromaticfoliage.common.config.ClientConfig;
import io.github.insomniakitten.chromaticfoliage.common.config.FeaturesConfig;
import io.github.insomniakitten.chromaticfoliage.common.config.GeneralConfig;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

import static io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage.MOD_ID;

@SuppressWarnings("PublicField")
@EventBusSubscriber(modid = MOD_ID)
final class ChromaticConfig {
  static final ClientConfig CLIENT = new ClientConfigImpl();
  static final FeaturesConfig FEATURES = new FeaturesConfigImpl();
  static final GeneralConfig GENERAL = new GeneralConfigImpl();

  private ChromaticConfig() {
    throw new UnsupportedOperationException();
  }

  @SubscribeEvent
  static void onConfigurationChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
    if (MOD_ID.equals(event.getModID())) {
      ClientConfigImpl.COLORS.preSync();
      ConfigManager.sync(MOD_ID, Type.INSTANCE);
      ClientConfigImpl.COLORS.postSync();
    }
  }

  @Config(modid = MOD_ID, name = MOD_ID + "/client", category = "")
  public static final class ClientConfigImpl implements ClientConfig {
    @Name("blocks")
    public static final BlockConfigImpl BLOCK = new BlockConfigImpl();

    @Name("colors")
    public static final ColorConfigImpl COLORS = new ColorConfigImpl();

    @Name("info")
    public static final InfoConfigImpl INFO = new InfoConfigImpl();

    private ClientConfigImpl() {}

    @Override
    public BlockConfig getBlockConfig() {
      return BLOCK;
    }

    @Override
    public ColorConfig getColorsConfig() {
      return COLORS;
    }

    @Override
    public InfoConfig getInfoConfig() {
      return INFO;
    }

    public static final class BlockConfigImpl implements BlockConfig {
      @Name("snow_layers")
      @Comment("Tint snow layers when above a chromatic grass block")
      @RequiresMcRestart
      public boolean snowLayers = true;

      @Name("grass_plants")
      @Comment("Tint grass plants when above a chromatic grass block")
      @RequiresMcRestart
      public boolean grassPlants = true;

      private BlockConfigImpl() {}

      @Override
      public boolean isSnowLayerTintingEnabled() {
        return snowLayers;
      }

      @Override
      public boolean isGrassPlantTintingEnabled() {
        return grassPlants;
      }
    }

    public static final class ColorConfigImpl implements ColorConfig {
      public int black = 0x1D1D21;
      public int red = 0xB02E26;
      public int green = 0x5E7C16;
      public int brown = 0x835432;
      public int blue = 0x3C44AA;
      public int purple = 0x8932B8;
      public int cyan = 0x169C9C;
      @Name("light_gray")
      public int lightGray = 0x9D9D97;
      public int gray = 0x474F52;
      public int pink = 0xF38BAA;
      public int lime = 0x80C71F;
      public int yellow = 0xFED83D;
      @Name("light_blue")
      public int lightBlue = 0x3AB3DA;
      public int magenta = 0xC74EBD;
      public int orange = 0xF9801D;
      public int white = 0xF9FFFE;

      @Nullable
      private IntList lastColors;
      private IntList colors = toList();

      private ColorConfigImpl() {}

      @Override
      public int getBlack() {
        return black;
      }

      @Override
      public int getRed() {
        return red;
      }

      @Override
      public int getGreen() {
        return green;
      }

      @Override
      public int getBrown() {
        return brown;
      }

      @Override
      public int getBlue() {
        return blue;
      }

      @Override
      public int getPurple() {
        return purple;
      }

      @Override
      public int getCyan() {
        return cyan;
      }

      @Override
      public int getLightGray() {
        return lightGray;
      }

      @Override
      public int getGray() {
        return gray;
      }

      @Override
      public int getPink() {
        return pink;
      }

      @Override
      public int getLime() {
        return lime;
      }

      @Override
      public int getYellow() {
        return yellow;
      }

      @Override
      public int getLightBlue() {
        return lightBlue;
      }

      @Override
      public int getMagenta() {
        return magenta;
      }

      @Override
      public int getOrange() {
        return orange;
      }

      @Override
      public int getWhite() {
        return white;
      }

      public void preSync() {
        lastColors = colors;
      }

      public void postSync() {
        if (lastColors != (colors = toList())) {
          FMLCommonHandler.instance().reloadRenderers();
        }
      }

      private IntList toList() {
        return new IntArrayList(new int[] {
          black,
          red,
          green,
          brown,
          blue,
          purple,
          cyan,
          lightGray,
          lightGray,
          pink,
          lime,
          yellow,
          lightBlue,
          magenta,
          orange,
          white
        });
      }
    }

    public static final class InfoConfigImpl implements InfoConfig {
      @Name("item_tooltip")
      @Comment("Display the color variant of the chromatic block in the item tooltip")
      public boolean itemTooltip = true;

      @Name("waila_color")
      @Comment({
                 "Display the color variant of the chromatic block in Hwyla/Waila",
                 "This config value is ignored if Hwyla/Waila is not present"
               })
      public boolean wailaColor = true;

      @Name("waila_illuminated")
      @Comment({
                 "Display an additional Hwyla/Waila tooltip for illuminated chromatic blocks",
                 "This config value is ignored if Hwyla/Waila is not present"
               })
      public boolean wailaIlluminated = true;

      private InfoConfigImpl() {}

      @Override
      public boolean isItemTooltipEnabled() {
        return itemTooltip;
      }

      @Override
      public boolean isWailaColorVariantTooltipEnabled() {
        return wailaColor;
      }

      @Override
      public boolean isWailaIlluminationTooltipEnabled() {
        return wailaIlluminated;
      }
    }
  }

  @Config(modid = MOD_ID, name = MOD_ID + "/features", category = "features")
  public static final class FeaturesConfigImpl implements FeaturesConfig {
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

    private FeaturesConfigImpl() {}

    @Override
    public boolean areGrassBlocksEnabled() {
      return grassBlocks;
    }

    @Override
    public boolean areOakLeavesEnabled() {
      return oakLeaves;
    }

    @Override
    public boolean areSpruceLeavesEnabled() {
      return spruceLeaves;
    }

    @Override
    public boolean areBirchLeavesEnabled() {
      return birchLeaves;
    }

    @Override
    public boolean areJungleLeavesEnabled() {
      return jungleLeaves;
    }

    @Override
    public boolean areAcaciaLeavesEnabled() {
      return acaciaLeaves;
    }

    @Override
    public boolean areDarkOakLeavesEnabled() {
      return darkOakLeaves;
    }

    @Override
    public boolean areVinesEnabled() {
      return vines;
    }
  }

  @Config(modid = MOD_ID, name = MOD_ID + "/general", category = "general")
  public static final class GeneralConfigImpl implements GeneralConfig {
    @Name("chroma_recoloring")
    @Comment({
               "Register shapeless recipes that allow recoloring of chromatic blocks",
               "In-world recoloring requires the in-world interaction to be enabled"
             })
    @RequiresMcRestart
    public static boolean chromaRecoloring = true;

    @Name("in_world_interaction")
    @Comment({
               "Allow coloring of blocks when right-clicking them with dyes",
               "Only functions on blocks with a chromatic variant",
               "This config does not control in-world illumination"
             })
    public static boolean inWorldInteraction = true;

    @Name("in_world_illumination")
    @Comment({
               "Allow right-clicking glowstone dust on chromatic blocks to illuminated them",
               "Produces a small amount of light, and allows the block to glow in the dark",
               "Illuminated blocks will drop their glowstone dust when broken"
             })
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
    @Comment({
               "Allow vines to be replaced by other blocks, including themselves",
               "Set value to true for vanilla behaviour"
             })
    @RequiresMcRestart
    public static boolean replaceableVines = false;

    private GeneralConfigImpl() {}

    @Override
    public boolean isChromaRecoloringEnabled() {
      return chromaRecoloring;
    }

    @Override
    public boolean isInWorldInteractionEnabled() {
      return inWorldInteraction;
    }

    @Override
    public boolean isInWorldIlluminationEnabled() {
      return inWorldIllumination;
    }

    @Override
    public boolean isGrassToDirtSpreadingEnabled() {
      return grassSpreadDirt;
    }

    @Override
    public boolean isGrassToGrassSpreadingEnabled() {
      return grassSpreadGrass;
    }

    @Override
    public boolean isLeavesToLeavesSpreadingEnabled() {
      return leavesSpreadLeaves;
    }

    @Override
    public boolean areVinesReplaceable() {
      return replaceableVines;
    }
  }
}
