package dev.sapphic.chromaticfoliage.block;

import com.google.common.collect.Lists;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import dev.sapphic.chromaticfoliage.init.ChromaticSounds;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EmissiveLeavesBlock extends ChromaticLeavesBlock {
  public EmissiveLeavesBlock(final EnumType type) {
    super(type);
    this.setLightOpacity(0);
  }

  @Override
  protected boolean isEmissive() {
    return true;
  }

  @Override
  public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    return 3;
  }

  @Override
  @Deprecated
  @SideOnly(Side.CLIENT)
  public int getPackedLightmapCoords(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    return 0xF000F0;
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    final ItemStack stack = player.getHeldItem(hand);
    if ((stack.getItem() == Items.GLOWSTONE_DUST) || !player.canPlayerEdit(pos, facing, stack)) {
      return false;
    }
    if (!player.isSneaking() || !stack.isEmpty()) {
      return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }
    if (world.isRemote) {
      player.swingArm(hand);
      return true;
    }
    final IBlockState leaves = ChromaticBlocks.CHROMATIC_LEAVES.get(this.getFoliageType()).getDefaultState();
    world.setBlockState(pos, leaves.withProperty(ChromaticFoliage.COLOR, state.getValue(ChromaticFoliage.COLOR)), 3);
    world.playSound(null, pos, ChromaticSounds.BLOCK_DYED, SoundCategory.BLOCKS, 1.0F, 0.8F);
    if (!player.inventory.addItemStackToInventory(new ItemStack(Items.GLOWSTONE_DUST))) {
      throw new IllegalStateException("Hand was empty but couldn't add item to inventory");
    }
    return true;
  }

  @Override
  public void getSubBlocks(final CreativeTabs group, final NonNullList<ItemStack> items) {

  }

  @Override
  public void getDrops(final NonNullList<ItemStack> drops, final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
    super.getDrops(drops, world, pos, state, fortune);
    drops.add(new ItemStack(Items.GLOWSTONE_DUST));
  }

  @Override
  public List<ItemStack> onSheared(final ItemStack stack, final IBlockAccess access, final BlockPos pos, final int fortune) {
    return Lists.newArrayList(this.getSilkTouchDrop(access.getBlockState(pos)), new ItemStack(Items.GLOWSTONE_DUST));
  }
}
