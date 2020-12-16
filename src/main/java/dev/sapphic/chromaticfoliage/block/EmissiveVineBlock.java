package dev.sapphic.chromaticfoliage.block;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import dev.sapphic.chromaticfoliage.init.ChromaticSounds;
import net.minecraft.block.properties.PropertyBool;
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

public class EmissiveVineBlock extends ChromaticVineBlock {
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
      return true;
    }
    final IBlockState actualState = state.getActualState(world, pos);
    final ChromaticColor color = actualState.getValue(ChromaticFoliage.COLOR);
    IBlockState vine = ChromaticBlocks.CHROMATIC_VINE.getDefaultState().withProperty(ChromaticFoliage.COLOR, color);
    for (final EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
      final PropertyBool property = getPropertyFor(side);
      vine = vine.withProperty(property, actualState.getValue(property));
    }
    world.setBlockState(pos, vine);
    world.playSound(null, pos, ChromaticSounds.BLOCK_DARKENED, SoundCategory.BLOCKS, 1.0F, 0.8F);
    player.swingArm(hand);
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
}
