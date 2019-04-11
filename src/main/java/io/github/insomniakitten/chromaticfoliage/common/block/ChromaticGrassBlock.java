package io.github.insomniakitten.chromaticfoliage.common.block;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.base.ChromaticColor;
import io.github.insomniakitten.chromaticfoliage.common.init.ChromaticBlocks;
import io.github.insomniakitten.chromaticfoliage.common.init.ChromaticItems;
import io.github.insomniakitten.chromaticfoliage.common.init.ChromaticSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;
import java.util.Random;

import static com.google.common.base.Preconditions.checkState;

public class ChromaticGrassBlock extends BlockGrass implements ChromaticBlock {
  private final boolean checkSnow;
  private final boolean spreadDirt;
  private final boolean spreadGrass;

  public ChromaticGrassBlock() {
    checkSnow = !ChromaticFoliage.getClientConfig().getBlockConfig().isSnowLayerTintingEnabled();
    spreadDirt = ChromaticFoliage.getGeneralConfig().isGrassToDirtSpreadingEnabled();
    spreadGrass = ChromaticFoliage.getGeneralConfig().isGrassToGrassSpreadingEnabled();
    setHardness(0.6F);
    setSoundType(SoundType.PLANT);
    setTickRandomly(spreadDirt || spreadGrass);
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
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    final ItemStack stack = player.getHeldItem(hand);
    if (!player.canPlayerEdit(pos, facing, stack)) return false;
    if (stack.isEmpty()) return false;
    if (ChromaticFoliage.getGeneralConfig().isInWorldIlluminationEnabled() && Items.GLOWSTONE_DUST == stack.getItem()) {
      if (world.isRemote) return true;
      final IBlockState grass = ChromaticBlocks.emissiveGrass().getDefaultState();
      if (!world.setBlockState(pos, grass.withProperty(COLOR, state.getValue(COLOR)), 3)) return false;
      world.playSound(null, pos, ChromaticSounds.blockIlluminated(), SoundCategory.BLOCKS, 1.0F, 0.8F);
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
      world.playSound(null, pos, ChromaticSounds.blockDyed(), SoundCategory.BLOCKS, 1.0F, 0.8F);
      if (!player.capabilities.isCreativeMode) stack.shrink(1);
      return true;
    }
    return false;
  }

  @Override
  protected ItemStack getSilkTouchDrop(final IBlockState state) {
    return new ItemStack(ChromaticItems.chromaticGrass(), 1, state.getValue(COLOR).ordinal());
  }

  @Override
  public void getSubBlocks(final CreativeTabs group, final NonNullList<ItemStack> items) {
    for (final ChromaticColor color : ChromaticColor.colors()) {
      items.add(new ItemStack(this, 1, color.ordinal()));
    }
  }

  @Override
  public String toString() {
    return "ChromaticGrassBlock";
  }

  @Override
  public ItemStack getPickBlock(final IBlockState state, final RayTraceResult target, final World world, final BlockPos pos, final EntityPlayer player) {
    return getSilkTouchDrop(state);
  }

  @Override
  public boolean canSustainPlant(final IBlockState state, final IBlockAccess access, final BlockPos pos, final EnumFacing side, final IPlantable plant) {
    return Blocks.GRASS.canSustainPlant(Blocks.GRASS.getDefaultState(), access, pos, side, plant);
  }

  @Override
  public boolean recolorBlock(final World world, final BlockPos pos, final EnumFacing side, final EnumDyeColor color) {
    final IBlockState state = world.getBlockState(pos);
    checkState(state.getBlock() == this, "Unexpected block %s at %s", state, pos);
    return world.setBlockState(pos, state.withProperty(COLOR, ChromaticColor.byDyeColor(color)));
  }

  @Override
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer, final EnumHand hand) {
    return getDefaultState().withProperty(COLOR, ChromaticColor.valueOf(meta));
  }

  @Override
  public IBlockState getActualState(final IBlockState state, final IBlockAccess access, final BlockPos pos) {
    return state.withProperty(SNOWY, checkSnow && isSnowBlock(access, pos.up()));
  }

  @Override
  public void updateTick(final World world, final BlockPos pos, final IBlockState state, final Random rand) {
    if (!spreadDirt && !spreadGrass || world.isRemote || !world.isAreaLoaded(pos, 3)) return;
    final BlockPos up = pos.up();
    final int lightOpacity = world.getBlockState(up).getLightOpacity(world, up);
    if (lightOpacity <= 2 || world.getLightFromNeighbors(up) >= 4) {
      if (world.getLightFromNeighbors(up) < 9) return;
      for (int i = 0; i < 4; ++i) {
        final int x = rand.nextInt(3) - 1;
        final int y = rand.nextInt(5) - 3;
        final int z = rand.nextInt(3) - 1;
        final BlockPos offset = pos.add(x, y, z);
        if (world.isOutsideBuildHeight(offset)) return;
        if (!world.isBlockLoaded(offset)) return;
        if (!canSpreadInto(world, offset)) continue;
        if (world.getLightFromNeighbors(offset.up()) < 4) continue;
        if (lightOpacity > 2) continue;
        world.setBlockState(offset, state, 3);
      }
    } else {
      world.setBlockState(pos, Blocks.DIRT.getDefaultState());
    }
  }

  @Override // todo: clean this up one day
  public void grow(final World world, final Random rand, final BlockPos pos, final IBlockState state) {
    final BlockPos above = pos.up();
    for (int i = 0; i < 128; ++i) {
      BlockPos targetPos = above;
      int j = 0;
      while (true) {
        if (j >= i / 16) {
          if (world.isAirBlock(targetPos)) {
            if (rand.nextInt(8) == 0) {
              world.getBiome(targetPos).plantFlower(world, rand, targetPos);
            } else {
              final IBlockState tallGrass = Blocks.TALLGRASS.getDefaultState()
                .withProperty(BlockTallGrass.TYPE, EnumType.GRASS);
              if (Blocks.TALLGRASS.canBlockStay(world, targetPos, tallGrass)) {
                world.setBlockState(targetPos, tallGrass, 3);
              }
            }
          }
          break;
        }
        targetPos = targetPos.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
        if (!(world.getBlockState(targetPos.down()).getBlock() instanceof BlockGrass) || world.getBlockState(targetPos)
          .isNormalCube()) {
          break;
        }
        ++j;
      }
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT_MIPPED;
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(COLOR).ordinal();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, COLOR, SNOWY);
  }

  protected final boolean canSpreadInto(final IBlockAccess access, final BlockPos pos) {
    final IBlockState state = access.getBlockState(pos);
    if (spreadDirt && state.getBlock() == Blocks.DIRT) {
      if (DirtType.DIRT == state.getValue(BlockDirt.VARIANT)) {
        return true;
      }
    }
    return spreadGrass && Blocks.GRASS == state.getBlock();
  }

  protected final boolean isSnowBlock(final IBlockAccess access, final BlockPos pos) {
    final Block block = access.getBlockState(pos).getBlock();
    return Blocks.SNOW == block || Blocks.SNOW_LAYER == block;
  }
}
