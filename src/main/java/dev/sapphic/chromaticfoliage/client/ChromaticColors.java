package dev.sapphic.chromaticfoliage.client;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.block.ChromaticGrassBlock;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.checkerframework.checker.nullness.qual.Nullable;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT, modid = ChromaticFoliage.ID)
final class ChromaticColors {
  private ChromaticColors() {
  }

  @SubscribeEvent
  static void registerAll(final ColorHandlerEvent.Block event) {
    final BlockColors colors = event.getBlockColors();

    for (final Block block : ForgeRegistries.BLOCKS.getValuesCollection()) {
      final @Nullable ResourceLocation id = block.getRegistryName();
      if ((id != null) && ChromaticFoliage.ID.equals(id.getNamespace())) {
        colors.registerBlockColorHandler((state, world, pos, tint) -> {
          return state.getValue(ChromaticFoliage.COLOR).getIntColor();
        }, block);
      }
    }

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
  static void registerAll(final ColorHandlerEvent.Item event) {
    final ItemColors colors = event.getItemColors();
    for (final Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
      final @Nullable ResourceLocation id = item.getRegistryName();
      if ((id != null) && ChromaticFoliage.ID.equals(id.getNamespace())) {
        colors.registerItemColorHandler((stack, tint) -> {
          return ChromaticColor.of(stack.getMetadata() & 15).getIntColor();
        }, item);
      }
    }
  }
}
