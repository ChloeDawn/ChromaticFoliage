package dev.sapphic.chromaticfoliage.event;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.block.entity.ChromaticBlockEntity;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import dev.sapphic.chromaticfoliage.init.ChromaticSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import org.checkerframework.checker.nullness.qual.Nullable;

@ObjectHolder(ChromaticFoliage.ID)
@EventBusSubscriber(modid = ChromaticFoliage.ID)
public final class ChromaticDyeing {
  private ChromaticDyeing() {
  }

  @SubscribeEvent
  public static void blockUsed(final PlayerInteractEvent.RightClickBlock event) {
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
      final @Nullable ChromaticColor color = ChromaticColor.of(stack);
      if (color == null) {
        return;
      }
      if (world.isRemote) {
        player.swingArm(event.getHand());
        return;
      }
      final IBlockState grass = ChromaticBlocks.CHROMATIC_GRASS.getDefaultState();
      world.setBlockState(pos, grass.withProperty(ChromaticFoliage.COLOR, color), 3);
      world.playSound(null, pos, ChromaticSounds.BLOCK_DYED, SoundCategory.BLOCKS, 1.0F, 0.8F);
      if (!player.capabilities.isCreativeMode) {
        stack.shrink(1);

      }
    }
    if ((block == Blocks.LEAVES) || (block == Blocks.LEAVES2)) {
      final @Nullable ChromaticColor color = ChromaticColor.of(stack);
      if (color == null) {
        return;
      }
      if (world.isRemote) {
        player.swingArm(event.getHand());
        return;
      }
      final int meta = block.getMetaFromState(state);
      final BlockPlanks.EnumType woodType = ((BlockLeaves) block).getWoodType(meta);
      final IBlockState leaves = ChromaticBlocks.CHROMATIC_LEAVES.get(woodType).getDefaultState();
      world.setBlockState(pos, leaves.withProperty(ChromaticFoliage.COLOR, color), 3);
      world.playSound(null, pos, ChromaticSounds.BLOCK_DYED, SoundCategory.BLOCKS, 1.0F, 0.8F);
      if (!player.capabilities.isCreativeMode) {
        stack.shrink(1);
      }
    }
    if (block == Blocks.VINE) {
      final @Nullable ChromaticColor color = ChromaticColor.of(stack);
      if (color == null) {
        return;
      }
      if (world.isRemote) {
        player.swingArm(event.getHand());
        return;
      }
      final IBlockState actualState = state.getActualState(world, pos);
      IBlockState vine = ChromaticBlocks.CHROMATIC_VINE.getDefaultState();
      for (final EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
        final PropertyBool property = BlockVine.getPropertyFor(side);
        vine = vine.withProperty(property, actualState.getValue(property));
      }
      world.setBlockState(pos, vine.withProperty(ChromaticFoliage.COLOR, color), 3);
      world.setTileEntity(pos, new ChromaticBlockEntity(color));
      world.playSound(null, pos, ChromaticSounds.BLOCK_DYED, SoundCategory.BLOCKS, 1.0F, 0.8F);
      if (!player.capabilities.isCreativeMode) {
        stack.shrink(1);
      }
    }
  }
}
