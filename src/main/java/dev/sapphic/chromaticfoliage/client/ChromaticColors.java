package dev.sapphic.chromaticfoliage.client;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.block.ChromaticGrassBlock;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import dev.sapphic.chromaticfoliage.init.ChromaticItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDoublePlant.EnumBlockHalf;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT, modid = ChromaticFoliage.ID)
public final class ChromaticColors {
  private static final IBlockColor BLOCK_COLOR_TINT = (state, world, pos, tint) -> {
    return state.getValue(ChromaticFoliage.COLOR).getIntColor();
  };

  private static final IBlockColor SAPLING_BLOCK_COLOR_MASK = (state, world, pos, tint) -> {
    return (tint > 0) ? state.getValue(ChromaticFoliage.COLOR).getIntColor() : 0xFFFFFF;
  };

  private static final IItemColor ITEM_COLOR_TINT = (stack, tint) -> {
    return ChromaticColor.of(stack.getMetadata()).getIntColor();
  };

  private static final IItemColor SAPLING_ITEM_COLOR_MASK = (stack, tint) -> {
    return (tint > 0) ? ChromaticColor.of(stack.getMetadata() >> 3).getIntColor() : 0xFFFFFF;
  };

  private ChromaticColors() {
  }

  @SubscribeEvent
  public static void registerAll(final ColorHandlerEvent.Block event) {
    final BlockColors colors = event.getBlockColors();

    chromatic(colors, ChromaticBlocks.CHROMATIC_GRASS);
    chromatic(colors, ChromaticBlocks.CHROMATIC_OAK_LEAVES);
    chromatic(colors, ChromaticBlocks.CHROMATIC_SPRUCE_LEAVES);
    chromatic(colors, ChromaticBlocks.CHROMATIC_BIRCH_LEAVES);
    chromatic(colors, ChromaticBlocks.CHROMATIC_JUNGLE_LEAVES);
    chromatic(colors, ChromaticBlocks.CHROMATIC_ACACIA_LEAVES);
    chromatic(colors, ChromaticBlocks.CHROMATIC_DARK_OAK_LEAVES);
    chromatic(colors, ChromaticBlocks.CHROMATIC_VINE);

    chromatic(colors, ChromaticBlocks.EMISSIVE_GRASS);
    chromatic(colors, ChromaticBlocks.EMISSIVE_OAK_LEAVES);
    chromatic(colors, ChromaticBlocks.EMISSIVE_SPRUCE_LEAVES);
    chromatic(colors, ChromaticBlocks.EMISSIVE_BIRCH_LEAVES);
    chromatic(colors, ChromaticBlocks.EMISSIVE_JUNGLE_LEAVES);
    chromatic(colors, ChromaticBlocks.EMISSIVE_ACACIA_LEAVES);
    chromatic(colors, ChromaticBlocks.EMISSIVE_DARK_OAK_LEAVES);
    chromatic(colors, ChromaticBlocks.EMISSIVE_VINE);

    sapling(colors, ChromaticBlocks.CHROMATIC_SAPLING);

    if (ChromaticConfig.Client.BLOCKS.snowLayers) {
      colors.registerBlockColorHandler((state, world, pos, tint) -> {
        if ((world != null) && (pos != null) && (pos.down().getY() >= 0)) {
          final IBlockState below = world.getBlockState(pos.down());
          if (below.getBlock() instanceof ChromaticGrassBlock) {
            return below.getValue(ChromaticFoliage.COLOR).getIntColor();
          }
          if (below.getBlock() == Blocks.SNOW_LAYER) {
            return colors.colorMultiplier(below, world, pos.down(), tint);
          }
        }
        return 0xFFFFFF;
      }, Blocks.SNOW_LAYER);
    }

    if (ChromaticConfig.Client.BLOCKS.grassPlants) {
      colors.registerBlockColorHandler((state, world, pos, tint) -> {
        if ((world != null) && (pos != null)) {
          final EnumPlantType variant = state.getValue(BlockDoublePlant.VARIANT);
          BlockPos target = pos;
          if ((variant == EnumPlantType.GRASS) || (variant == EnumPlantType.FERN)) {
            if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) {
              target = target.down();
            }
          }
          if (target.down().getY() >= 0) {
            final IBlockState below = world.getBlockState(target.down());
            if (below.getBlock() instanceof ChromaticGrassBlock) {
              return below.getValue(ChromaticFoliage.COLOR).getIntColor();
            }
          }
          return BiomeColorHelper.getGrassColorAtPos(world, target);
        }
        return 0xFFFFFF;
      }, Blocks.DOUBLE_PLANT);

      colors.registerBlockColorHandler((state, world, pos, tint) -> {
        if ((world != null) && (pos != null)) {
          if (pos.down().getY() >= 0) {
            final IBlockState below = world.getBlockState(pos.down());
            if (below.getBlock() instanceof ChromaticGrassBlock) {
              return below.getValue(ChromaticFoliage.COLOR).getIntColor();
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

  @SubscribeEvent
  public static void registerAll(final ColorHandlerEvent.Item event) {
    final ItemColors colors = event.getItemColors();

    chromatic(colors, ChromaticItems.CHROMATIC_GRASS);
    chromatic(colors, ChromaticItems.CHROMATIC_OAK_LEAVES);
    chromatic(colors, ChromaticItems.CHROMATIC_SPRUCE_LEAVES);
    chromatic(colors, ChromaticItems.CHROMATIC_BIRCH_LEAVES);
    chromatic(colors, ChromaticItems.CHROMATIC_JUNGLE_LEAVES);
    chromatic(colors, ChromaticItems.CHROMATIC_ACACIA_LEAVES);
    chromatic(colors, ChromaticItems.CHROMATIC_DARK_OAK_LEAVES);
    chromatic(colors, ChromaticItems.CHROMATIC_VINE);

    chromatic(colors, ChromaticItems.EMISSIVE_GRASS);
    chromatic(colors, ChromaticItems.EMISSIVE_OAK_LEAVES);
    chromatic(colors, ChromaticItems.EMISSIVE_SPRUCE_LEAVES);
    chromatic(colors, ChromaticItems.EMISSIVE_BIRCH_LEAVES);
    chromatic(colors, ChromaticItems.EMISSIVE_JUNGLE_LEAVES);
    chromatic(colors, ChromaticItems.EMISSIVE_ACACIA_LEAVES);
    chromatic(colors, ChromaticItems.EMISSIVE_DARK_OAK_LEAVES);
    chromatic(colors, ChromaticItems.EMISSIVE_VINE);

    sapling(colors, ChromaticItems.CHROMATIC_SAPLING);
  }

  private static void chromatic(final BlockColors colors, final Block block) {
    colors.registerBlockColorHandler(BLOCK_COLOR_TINT, block);
  }

  private static void sapling(final BlockColors colors, final Block block) {
    colors.registerBlockColorHandler(SAPLING_BLOCK_COLOR_MASK, block);
  }

  private static void chromatic(final ItemColors colors, final Item item) {
    colors.registerItemColorHandler(ITEM_COLOR_TINT, item);
  }

  private static void sapling(final ItemColors colors, final Item item) {
    colors.registerItemColorHandler(SAPLING_ITEM_COLOR_MASK, item);
  }
}
