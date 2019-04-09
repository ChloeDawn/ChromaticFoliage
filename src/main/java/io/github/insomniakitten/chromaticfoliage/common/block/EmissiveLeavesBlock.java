package io.github.insomniakitten.chromaticfoliage.common.block;

import com.google.common.collect.Lists;
import io.github.insomniakitten.chromaticfoliage.common.init.ChromaticBlocks;
import io.github.insomniakitten.chromaticfoliage.common.init.ChromaticSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EmissiveLeavesBlock extends ChromaticLeavesBlock {
  public EmissiveLeavesBlock(final EnumType type) {
    super(type);
  }

  @Override
  public void harvestBlock(final World world, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack stack) {
    Block.spawnAsEntity(world, pos, new ItemStack(Items.GLOWSTONE_DUST));
    super.harvestBlock(world, player, pos, state, te, stack);
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    if (!player.isSneaking()) return false;
    final ItemStack stack = player.getHeldItem(hand);
    if (world.isRemote) return stack.isEmpty();
    if (!stack.isEmpty() || !player.canPlayerEdit(pos, facing, stack)) return false;
    final IBlockState leaves = ChromaticBlocks.chromaticLeaves(foliageType).getDefaultState();
    if (world.setBlockState(pos, leaves.withProperty(COLOR, state.getValue(COLOR)), 3)) {
      world.playSound(null, pos, ChromaticSounds.blockDarkened(), SoundCategory.BLOCKS, 1.0F, 0.8F);
      final ItemStack glowstone = new ItemStack(Items.GLOWSTONE_DUST);
      if (!player.inventory.addItemStackToInventory(glowstone)) {
        Block.spawnAsEntity(world, pos, glowstone);
      }
      return true;
    }
    return false;
  }

  @Override
  public void getSubBlocks(final CreativeTabs group, final NonNullList<ItemStack> items) {

  }

  @Override
  public String toString() {
    return "EmissiveLeavesBlock(" + foliageType + ')';
  }

  @Override
  public List<ItemStack> onSheared(final ItemStack stack, final IBlockAccess access, final BlockPos pos, final int fortune) {
    return Lists.newArrayList(getSilkTouchDrop(access.getBlockState(pos)), new ItemStack(Items.GLOWSTONE_DUST));
  }
}
