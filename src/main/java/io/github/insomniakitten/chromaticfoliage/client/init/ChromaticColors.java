package io.github.insomniakitten.chromaticfoliage.client.init;

import io.github.insomniakitten.chromaticfoliage.client.color.ChromaticColorProvider;
import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.block.ChromaticBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.ChromaticGrassBlock;
import io.github.insomniakitten.chromaticfoliage.common.config.ClientConfig.BlockConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDoublePlant.EnumBlockHalf;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import static io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage.MOD_ID;
import static io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage.isNamespaced;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT, modid = MOD_ID)
final class ChromaticColors {
  private static final BlockConfig BLOCK_CONFIG = ChromaticFoliage.getClientConfig().getBlockConfig();
  private static final Logger LOGGER = ChromaticFoliage.getLogger("Colors");

  private ChromaticColors() {
    throw new UnsupportedOperationException();
  }

  @SubscribeEvent
  static void registerAll(final ColorHandlerEvent.Block event) {
    LOGGER.debug("Beginning registration to {}", event);
    registerAll(event.getBlockColors());
    LOGGER.debug("Completed registration to {}", event);
  }

  @SubscribeEvent
  static void registerAll(final ColorHandlerEvent.Item event) {
    LOGGER.debug("Beginning registration to {}", event);
    registerAll(event.getItemColors());
    LOGGER.debug("Completed registration to {}", event);
  }

  private static void registerAll(final BlockColors colors) {
    for (final Block block : ForgeRegistries.BLOCKS.getValuesCollection()) {
      if (isNamespaced(block)) {
        register(colors, block);
      }
    }

    if (BLOCK_CONFIG.isSnowLayerTintingEnabled()) {
      registerSnowLayer(colors);
    }

    if (BLOCK_CONFIG.isGrassPlantTintingEnabled()) {
      registerDoublePlant(colors);
      registerTallGrass(colors);
    }
  }

  private static void registerAll(final ItemColors colors) {
    for (final Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
      if (isNamespaced(item)) {
        register(colors, item);
      }
    }
  }

  private static void register(final BlockColors colors, final Block block) {
    LOGGER.debug("| Binding chromatic color provider to {}", block);
    colors.registerBlockColorHandler(ChromaticColorProvider.getInstance(), block);
  }

  private static void register(final ItemColors colors, final Item item) {
    LOGGER.debug("| Binding chromatic color provider to {}", item);
    colors.registerItemColorHandler(ChromaticColorProvider.getInstance(), item);
  }

  private static void registerSnowLayer(final BlockColors colors) {
    if (BLOCK_CONFIG.isSnowLayerTintingEnabled()) {
      LOGGER.debug("| Binding color provider override to {}", Blocks.SNOW_LAYER);
      colors.registerBlockColorHandler((state, world, pos, tint) -> {
        if (world != null && pos != null && pos.down().getY() >= 0) {
          final IBlockState below = world.getBlockState(pos.down());
          if (below.getBlock() instanceof ChromaticGrassBlock) {
            return below.getValue(ChromaticBlock.COLOR).getColorValue();
          }
          if (below.getBlock() == Blocks.SNOW_LAYER) {
            return colors.colorMultiplier(below, world, pos.down(), tint);
          }
        }
        return 0xFFFFFF;
      }, Blocks.SNOW_LAYER);
    }
  }

  private static void registerDoublePlant(final BlockColors colors) {
    LOGGER.debug("| Binding color provider override to {}", Blocks.DOUBLE_PLANT);
    colors.registerBlockColorHandler((state, world, pos, tint) -> {
      if (world != null && pos != null) {
        final EnumPlantType variant = state.getValue(BlockDoublePlant.VARIANT);
        BlockPos target = pos;
        if (variant == EnumPlantType.GRASS || variant == EnumPlantType.FERN) {
          if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) {
            target = target.down();
          }
        }
        if (target.down().getY() >= 0) {
          final IBlockState below = world.getBlockState(target.down());
          if (below.getBlock() instanceof ChromaticGrassBlock) {
            return below.getValue(ChromaticBlock.COLOR).getColorValue();
          }
        }
        return BiomeColorHelper.getGrassColorAtPos(world, target);
      }
      return 0xFFFFFF;
    }, Blocks.DOUBLE_PLANT);
  }

  private static void registerTallGrass(final BlockColors colors) {
    LOGGER.debug("| Binding color provider override to {}", Blocks.TALLGRASS);
    colors.registerBlockColorHandler((state, world, pos, tint) -> {
      if (world != null && pos != null) {
        if (pos.down().getY() >= 0) {
          final IBlockState below = world.getBlockState(pos.down());
          if (below.getBlock() instanceof ChromaticGrassBlock) {
            return below.getValue(ChromaticBlock.COLOR).getColorValue();
          }
        }
        return BiomeColorHelper.getGrassColorAtPos(world, pos);
      }
      if (state.getValue(BlockTallGrass.TYPE) != EnumType.DEAD_BUSH) {
        return ColorizerGrass.getGrassColor(0.5D, 1.0D);
      }
      return 0xFFFFFF;
    }, Blocks.TALLGRASS);
  }
}
