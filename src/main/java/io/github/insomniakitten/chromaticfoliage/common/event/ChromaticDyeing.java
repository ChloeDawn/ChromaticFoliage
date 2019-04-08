package io.github.insomniakitten.chromaticfoliage.common.event;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.base.ChromaticColor;
import io.github.insomniakitten.chromaticfoliage.common.block.ChromaticBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.entity.ChromaticBlockEntity;
import io.github.insomniakitten.chromaticfoliage.common.init.ChromaticBlocks;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import java.util.Map.Entry;

@ObjectHolder(ChromaticFoliage.MOD_ID)
@EventBusSubscriber(modid = ChromaticFoliage.MOD_ID)
final class ChromaticDyeing {
  private ChromaticDyeing() {
    throw new UnsupportedOperationException();
  }

  @SubscribeEvent
  static void blockUsed(final PlayerInteractEvent.RightClickBlock event) {
    if (!ChromaticFoliage.getGeneralConfig().isInWorldInteractionEnabled()) return;
    final EntityPlayer player = event.getEntityPlayer();
    final World world = event.getWorld();
    final BlockPos pos = event.getPos();
    final IBlockState state = world.getBlockState(pos);
    if (state.getBlock() == Blocks.GRASS) {
      if (world.isRemote && !player.isSneaking()) {
        player.swingArm(event.getHand());
        return;
      }
      ChromaticColor.byDyeColor(event.getItemStack()).ifPresent((final ChromaticColor color) -> {
        final IBlockState grass = ChromaticBlocks.chromaticGrass().getDefaultState();
        if (world.setBlockState(pos, grass.withProperty(ChromaticBlock.COLOR, color), 3)) {
          world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
          if (!player.isCreative()) {
            event.getItemStack().shrink(1);
          }
        }
      });
    }
    if (state.getBlock() == Blocks.LEAVES || state.getBlock() == Blocks.LEAVES2) {
      if (world.isRemote && !player.isSneaking()) {
        player.swingArm(event.getHand());
        return;
      }
      ChromaticColor.byDyeColor(event.getItemStack()).ifPresent((final ChromaticColor color) -> {
        final BlockLeaves block = (BlockLeaves) state.getBlock();
        final int meta = block.getMetaFromState(state);
        final IBlockState leaves = ChromaticBlocks.chromaticLeaves(block.getWoodType(meta)).getDefaultState();
        if (world.setBlockState(pos, leaves.withProperty(ChromaticBlock.COLOR, color), 3)) {
          world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
          if (!player.capabilities.isCreativeMode) event.getItemStack().shrink(1);
        }
      });
    }
    if (state.getBlock() == Blocks.VINE) {
      if (world.isRemote && !player.isSneaking()) {
        player.swingArm(event.getHand());
        return;
      }
      ChromaticColor.byDyeColor(event.getItemStack()).ifPresent(color -> {
        final IBlockState actualState = state.getActualState(world, pos);
        IBlockState chroma = ChromaticBlocks.chromaticVine().getDefaultState();
        for (final Entry<IProperty<?>, Comparable<?>> entry : actualState.getProperties().entrySet()) {
          //noinspection unchecked,RedundantCast
          chroma = chroma.withProperty((IProperty) entry.getKey(), (Comparable) entry.getValue());
        }
        if (world.setBlockState(pos, chroma.withProperty(ChromaticBlock.COLOR, color), 3)) {
          world.setTileEntity(pos, new ChromaticBlockEntity(color));
          world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
          if (!player.isCreative()) {
            event.getItemStack().shrink(1);
          }
        }
      });
    }
  }
}
