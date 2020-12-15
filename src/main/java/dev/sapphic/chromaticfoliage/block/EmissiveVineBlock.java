package dev.sapphic.chromaticfoliage.block;

import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import dev.sapphic.chromaticfoliage.init.ChromaticSounds;
import net.minecraft.block.properties.IProperty;
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

import java.util.Map.Entry;

public class EmissiveVineBlock extends ChromaticVineBlock {
  @Override
  public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    return 5;
  }

  @Override
  protected boolean isEmissive() {
    return true;
  }

  @Override
  @Deprecated
  @SideOnly(Side.CLIENT)
  public int getPackedLightmapCoords(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    return 0xF000F0;
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    if (!player.isSneaking()) {
      return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }
    final ItemStack stack = player.getHeldItem(hand);
    if (!stack.isEmpty() || !player.canPlayerEdit(pos, facing, stack)) {
      return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }
    if (world.isRemote) {
      player.swingArm(hand);
      return true;
    }
    final IBlockState actualState = state.getActualState(world, pos);
    IBlockState vine = ChromaticBlocks.CHROMATIC_VINE.getDefaultState();
    for (final Entry<IProperty<?>, Comparable<?>> prop : actualState.getProperties().entrySet()) {
      //noinspection unchecked,rawtypes
      vine = vine.withProperty((IProperty) prop.getKey(), (Comparable) prop.getValue());
    }
    if (world.setBlockState(pos, vine)) {
      world.playSound(null, pos, ChromaticSounds.BLOCK_DARKENED, SoundCategory.BLOCKS, 1.0F, 0.8F);
      final ItemStack glowstone = new ItemStack(Items.GLOWSTONE_DUST);
      if (!player.inventory.addItemStackToInventory(glowstone)) {
        throw new IllegalStateException();
      }
      return true;
    }
    return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public void getSubBlocks(final CreativeTabs group, final NonNullList<ItemStack> items) {

  }

  @Override
  public void getDrops(final NonNullList<ItemStack> drops, final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
    super.getDrops(drops, world, pos, state, fortune);
    drops.add(new ItemStack(Items.GLOWSTONE_DUST));
  }
}
