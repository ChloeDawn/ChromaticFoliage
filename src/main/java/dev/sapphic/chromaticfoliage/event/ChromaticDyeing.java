package dev.sapphic.chromaticfoliage.event;

import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.block.entity.ChromaticBlockEntity;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import dev.sapphic.chromaticfoliage.init.ChromaticSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import java.util.Map.Entry;

@ObjectHolder(ChromaticFoliage.ID)
@EventBusSubscriber(modid = ChromaticFoliage.ID)
final class ChromaticDyeing {
  private ChromaticDyeing() {
  }

  @SubscribeEvent
  static void blockUsed(final PlayerInteractEvent.RightClickBlock event) {
    if (!ChromaticConfig.General.inWorldInteraction) {
      return;
    }
    final EntityPlayer player = event.getEntityPlayer();
    final World world = event.getWorld();
    final BlockPos pos = event.getPos();
    final IBlockState state = world.getBlockState(pos);
    final ItemStack stack = event.getItemStack();
    final Block block = state.getBlock();
    if ((block instanceof IGrowable) && ((IGrowable) block).canUseBonemeal(world, world.rand, pos, state)) {
      if ((stack.getItem() == Items.DYE) && (stack.getMetadata() == EnumDyeColor.WHITE.getDyeDamage())) {
        return;
      }
    }
    if (block == Blocks.GRASS) {
      ChromaticColor.of(stack).ifPresent(color -> {
        final IBlockState grass = ChromaticBlocks.CHROMATIC_GRASS.getDefaultState();
        if (world.setBlockState(pos, grass.withProperty(ChromaticFoliage.COLOR, color), 3)) {
          world.playSound(null, pos, ChromaticSounds.BLOCK_DYED, SoundCategory.BLOCKS, 1.0F, 0.8F);
          player.swingArm(event.getHand());
          if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
          }
        }
      });
    }
    if ((block == Blocks.LEAVES) || (block == Blocks.LEAVES2)) {
      ChromaticColor.of(stack).ifPresent(color -> {
        final int meta = block.getMetaFromState(state);
        final IBlockState leaves =
          ChromaticBlocks.CHROMATIC_LEAVES.get(((BlockLeaves) block).getWoodType(meta)).getDefaultState();
        if (world.setBlockState(pos, leaves.withProperty(ChromaticFoliage.COLOR, color), 3)) {
          world.playSound(null, pos, ChromaticSounds.BLOCK_DYED, SoundCategory.BLOCKS, 1.0F, 0.8F);
          player.swingArm(event.getHand());
          if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
          }
        }
      });
    }
    if (block == Blocks.VINE) {
      ChromaticColor.of(stack).ifPresent(color -> {
        final IBlockState actualState = state.getActualState(world, pos);
        IBlockState chroma = ChromaticBlocks.CHROMATIC_VINE.getDefaultState();
        for (final Entry<IProperty<?>, Comparable<?>> entry : actualState.getProperties().entrySet()) {
          // noinspection unchecked,rawtypes
          chroma = chroma.withProperty((IProperty) entry.getKey(), (Comparable) entry.getValue());
        }
        if (world.setBlockState(pos, chroma.withProperty(ChromaticFoliage.COLOR, color), 3)) {
          world.setTileEntity(pos, new ChromaticBlockEntity(color));
          world.playSound(null, pos, ChromaticSounds.BLOCK_DYED, SoundCategory.BLOCKS, 1.0F, 0.8F);
          player.swingArm(event.getHand());
          if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
          }
        }
      });
    }
  }
}
