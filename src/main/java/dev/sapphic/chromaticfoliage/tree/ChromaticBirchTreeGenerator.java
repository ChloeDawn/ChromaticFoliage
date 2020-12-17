package dev.sapphic.chromaticfoliage.tree;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class ChromaticBirchTreeGenerator extends WorldGenAbstractTree {
  private static final IBlockState LOG =
    Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
  private final IBlockState leaves;

  public ChromaticBirchTreeGenerator(final ChromaticColor color) {
    super(true);
    this.leaves = ChromaticBlocks.CHROMATIC_BIRCH_LEAVES.getDefaultState()
      .withProperty(BlockLeaves.CHECK_DECAY, false)
      .withProperty(ChromaticFoliage.COLOR, color);
  }

  @Override
  public boolean generate(final World world, final Random rand, final BlockPos pos) {
    final int height = rand.nextInt(3) + 5;
    if ((pos.getY() >= 1) && ((pos.getY() + height + 1) <= 256)) {
      boolean canGenerate = true;
      for (int y = pos.getY(); y <= (pos.getY() + 1 + height); ++y) {
        int k = 1;
        if (y == pos.getY()) {
          k = 0;
        }
        if (y >= ((pos.getY() + 1 + height) - 2)) {
          k = 2;
        }
        final BlockPos.MutableBlockPos offset = new BlockPos.MutableBlockPos();
        for (int x = pos.getX() - k; (x <= (pos.getX() + k)) && canGenerate; ++x) {
          for (int z = pos.getZ() - k; (z <= (pos.getZ() + k)) && canGenerate; ++z) {
            if ((y >= 0) && (y < world.getHeight())) {
              if (!this.isReplaceable(world, offset.setPos(x, y, z))) {
                canGenerate = false;
              }
            } else {
              canGenerate = false;
            }
          }
        }
      }
      if (!canGenerate) {
        return false;
      }
      final BlockPos posBelow = pos.down();
      final IBlockState stateBelow = world.getBlockState(posBelow);
      final Block blockBelow = stateBelow.getBlock();
      final boolean isSoil = blockBelow.canSustainPlant(stateBelow, world, posBelow, EnumFacing.UP, (IPlantable) Blocks.SAPLING);
      if (isSoil && (pos.getY() < (world.getHeight() - height - 1))) {
        blockBelow.onPlantGrow(stateBelow, world, posBelow, pos);
        for (int i2 = (pos.getY() - 3) + height; i2 <= (pos.getY() + height); ++i2) {
          final int k2 = i2 - (pos.getY() + height);
          final int l2 = 1 - (k2 / 2);
          for (int i3 = pos.getX() - l2; i3 <= (pos.getX() + l2); ++i3) {
            final int j1 = i3 - pos.getX();
            for (int k1 = pos.getZ() - l2; k1 <= (pos.getZ() + l2); ++k1) {
              final int l1 = k1 - pos.getZ();
              if ((Math.abs(j1) != l2) || (Math.abs(l1) != l2) || ((rand.nextInt(2) != 0) && (k2 != 0))) {
                final BlockPos blockpos = new BlockPos(i3, i2, k1);
                final IBlockState state2 = world.getBlockState(blockpos);
                if (state2.getBlock().isAir(state2, world, blockpos) || state2.getBlock()
                  .isAir(state2, world, blockpos)) {
                  this.setBlockAndNotifyAdequately(world, blockpos, this.leaves);
                }
              }
            }
          }
        }
        for (int above = 0; above < height; ++above) {
          final BlockPos posAbove = pos.up(above);
          final IBlockState stateAbove = world.getBlockState(posAbove);
          final boolean isAir = stateAbove.getBlock().isAir(stateAbove, world, posAbove);
          if (isAir || stateAbove.getBlock().isLeaves(stateAbove, world, posAbove)) {
            this.setBlockAndNotifyAdequately(world, pos.up(above), LOG);
          }
        }
        return true;
      }
    }
    return false;
  }
}