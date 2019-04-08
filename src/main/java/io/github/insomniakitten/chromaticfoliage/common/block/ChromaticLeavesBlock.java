package io.github.insomniakitten.chromaticfoliage.common.block;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.base.ChromaticColor;
import io.github.insomniakitten.chromaticfoliage.common.init.CFBlocks;
import io.github.insomniakitten.chromaticfoliage.common.init.CFItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ChromaticLeavesBlock extends BlockLeaves implements ChromaticBlock {
  protected final EnumType foliageType;
  private final boolean spreadLeaves;

  public ChromaticLeavesBlock(final EnumType type) {
    foliageType = type;
    spreadLeaves = ChromaticFoliage.getGeneralConfig().isLeavesToLeavesSpreadingEnabled();
    setTickRandomly(spreadLeaves);
    setHardness(0.2F);
    setLightOpacity(1);
    setSoundType(SoundType.PLANT);
    setDefaultState(getDefaultState()
      .withProperty(BlockLeaves.CHECK_DECAY, false)
      .withProperty(BlockLeaves.DECAYABLE, false)
    );
  }

  @Override
  @Deprecated
  public MapColor getMapColor(final IBlockState state, final IBlockAccess access, final BlockPos pos) {
    return state.getValue(COLOR).getMapColor();
  }

  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return getDefaultState().withProperty(COLOR, ChromaticColor.valueOf(meta));
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(COLOR).ordinal();
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    final ItemStack stack = player.getHeldItem(hand);
    if (!player.canPlayerEdit(pos, facing, stack)) return false;
    if (stack.isEmpty()) return false;
    if (ChromaticFoliage.getGeneralConfig().isInWorldIlluminationEnabled() && Items.GLOWSTONE_DUST == stack.getItem()) {
      if (world.isRemote) return true;
      final IBlockState leaves = CFBlocks.emissiveLeaves(foliageType).getDefaultState();
      if (!world.setBlockState(pos, leaves.withProperty(COLOR, state.getValue(COLOR)), 3)) return false;
      world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
      if (!player.capabilities.isCreativeMode) {
        stack.shrink(1);
      }
      return true;
    }
    if (ChromaticFoliage.getGeneralConfig().isChromaRecoloringEnabled()) {
      final Optional<ChromaticColor> optional = ChromaticColor.byDyeColor(stack);
      if (!optional.isPresent()) return false;
      final ChromaticColor color = optional.get();
      if (color == state.getValue(COLOR)) return false;
      if (world.isRemote) return true;
      if (!world.setBlockState(pos, state.withProperty(COLOR, color), 3)) return false;
      world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
      if (!player.capabilities.isCreativeMode) stack.shrink(1);
      return true;
    }
    return false;
  }

  @Override
  protected ItemStack getSilkTouchDrop(final IBlockState state) {
    return new ItemStack(CFItems.chromaticLeaves(foliageType), 1, state.getValue(COLOR).ordinal());
  }

  @Override
  public void getSubBlocks(final CreativeTabs group, final NonNullList<ItemStack> items) {

  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, COLOR, CHECK_DECAY, DECAYABLE);
  }

  @Override
  public String toString() {
    return "ChromaticLeavesBlock(" + foliageType + ')';
  }

  @Override
  public boolean doesSideBlockRendering(final IBlockState state, final IBlockAccess access, final BlockPos pos, final EnumFacing side) {
    return !isFancyGraphics();
  }

  @Override
  public ItemStack getPickBlock(final IBlockState state, final RayTraceResult target, final World world, final BlockPos pos, final EntityPlayer player) {
    return getSilkTouchDrop(state);
  }

  @Override
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer, final EnumHand hand) {
    return getDefaultState().withProperty(COLOR, ChromaticColor.valueOf(meta));
  }

  @Override
  public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {

  }

  @Override
  public void updateTick(final World world, final BlockPos pos, final IBlockState state, final Random rand) {
    if (spreadLeaves && !world.isRemote && world.isAreaLoaded(pos, 3)) {
      for (int i = 0; i < 4; ++i) {
        final int x = rand.nextInt(3) - 1;
        final int y = rand.nextInt(5) - 3;
        final int z = rand.nextInt(3) - 1;
        final BlockPos offset = pos.add(x, y, z);
        if (world.isOutsideBuildHeight(offset)) return;
        if (!world.isBlockLoaded(offset)) return;
        if (!canSpreadInto(world, offset)) continue;
        world.setBlockState(offset, state, 3);
      }
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(final IBlockState state, final World world, final BlockPos pos, final Random rand) {
    if (world.isRainingAt(pos.up())) {
      final BlockPos down = pos.down();
      final IBlockState below = world.getBlockState(down);
      final BlockFaceShape shape = below.getBlockFaceShape(world, down, EnumFacing.UP);
      if (BlockFaceShape.SOLID != shape && 1 == rand.nextInt(15)) {
        final double x = (double) ((float) pos.getX() + rand.nextFloat());
        final double y = (double) pos.getY() - 0.05D;
        final double z = (double) ((float) pos.getZ() + rand.nextFloat());
        world.spawnParticle(EnumParticleTypes.DRIP_WATER, x, y, z, 0.0D, 0.0D, 0.0D);
      }
    }
  }

  @Override
  @Deprecated
  public boolean isOpaqueCube(final IBlockState state) {
    return !isFancyGraphics();
  }

  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getRenderLayer() {
    return isFancyGraphics() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
  }

  @Override
  public EnumType getWoodType(final int meta) {
    return foliageType;
  }

  @Override
  public void beginLeavesDecay(final IBlockState state, final World world, final BlockPos pos) {

  }

  @Override
  public void getDrops(final NonNullList<ItemStack> drops, final IBlockAccess access, final BlockPos pos, final IBlockState state, final int fortune) {

  }

  @Override
  @Deprecated
  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(final IBlockState state, final IBlockAccess access, final BlockPos pos, final EnumFacing side) {
    final BlockPos offset = pos.offset(side);
    return !access.getBlockState(offset).doesSideBlockRendering(access, offset, side.getOpposite());
  }

  @Override
  public List<ItemStack> onSheared(final ItemStack stack, final IBlockAccess access, final BlockPos pos, final int fortune) {
    return Collections.singletonList(getSilkTouchDrop(access.getBlockState(pos)));
  }

  protected final boolean canSpreadInto(final IBlockAccess access, final BlockPos pos) {
    final IBlockState state = access.getBlockState(pos);
    final Block block = state.getBlock();
    if (Blocks.LEAVES == block) {
      return foliageType == state.getValue(BlockOldLeaf.VARIANT);
    }
    if (Blocks.LEAVES2 == block) {
      return foliageType == state.getValue(BlockNewLeaf.VARIANT);
    }
    return false;
  }

  private boolean isFancyGraphics() {
    return !Blocks.LEAVES.getDefaultState().isOpaqueCube();
  }
}
