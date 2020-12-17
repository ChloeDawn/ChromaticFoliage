package dev.sapphic.chromaticfoliage.tree;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class ChromaticDarkOakTreeGenerator extends WorldGenAbstractTree {
  private static final IBlockState TRUNK =
    Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);

  private final IBlockState leaves;

  public ChromaticDarkOakTreeGenerator(final ChromaticColor color) {
    super(true);
    this.leaves = ChromaticBlocks.CHROMATIC_DARK_OAK_LEAVES.getDefaultState()
      .withProperty(BlockLeaves.CHECK_DECAY, false)
      .withProperty(ChromaticFoliage.COLOR, color);
  }

  @Override
  public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
    final int i = rand.nextInt(3) + rand.nextInt(2) + 6;
    final int j = position.getX();
    final int k = position.getY();
    final int l = position.getZ();
    if ((k >= 1) && ((k + i + 1) < 256)) {
      final BlockPos blockpos = position.down();
      IBlockState state = worldIn.getBlockState(blockpos);
      final boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, blockpos, EnumFacing.UP, (IPlantable) Blocks.SAPLING);
      if (!(isSoil && (position.getY() < (worldIn.getHeight() - i - 1)))) {
        return false;
      }
      if (!this.placeTreeOfHeight(worldIn, position, i)) {
        return false;
      }
      this.onPlantGrow(worldIn, blockpos, position);
      this.onPlantGrow(worldIn, blockpos.east(), position);
      this.onPlantGrow(worldIn, blockpos.south(), position);
      this.onPlantGrow(worldIn, blockpos.south().east(), position);
      final EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
      final int i1 = i - rand.nextInt(4);
      int j1 = 2 - rand.nextInt(3);
      int k1 = j;
      int l1 = l;
      final int i2 = (k + i) - 1;

      for (int j2 = 0; j2 < i; ++j2) {
        if ((j2 >= i1) && (j1 > 0)) {
          k1 += enumfacing.getXOffset();
          l1 += enumfacing.getZOffset();
          --j1;
        }

        final int k2 = k + j2;
        final BlockPos blockpos1 = new BlockPos(k1, k2, l1);
        state = worldIn.getBlockState(blockpos1);

        if (state.getBlock().isAir(state, worldIn, blockpos1) || state.getBlock().isLeaves(state, worldIn, blockpos1)) {
          this.placeLogAt(worldIn, blockpos1);
          this.placeLogAt(worldIn, blockpos1.east());
          this.placeLogAt(worldIn, blockpos1.south());
          this.placeLogAt(worldIn, blockpos1.east().south());
        }
      }

      for (int i3 = -2; i3 <= 0; ++i3) {
        for (int l3 = -2; l3 <= 0; ++l3) {
          int k4 = -1;
          this.placeLeafAt(worldIn, k1 + i3, i2 + k4, l1 + l3);
          this.placeLeafAt(worldIn, (1 + k1) - i3, i2 + k4, l1 + l3);
          this.placeLeafAt(worldIn, k1 + i3, i2 + k4, (1 + l1) - l3);
          this.placeLeafAt(worldIn, (1 + k1) - i3, i2 + k4, (1 + l1) - l3);

          if (((i3 > -2) || (l3 > -1)) && ((i3 != -1) || (l3 != -2))) {
            int i4 = 1;
            this.placeLeafAt(worldIn, k1 + i3, i2 + i4, l1 + l3);
            this.placeLeafAt(worldIn, (1 + k1) - i3, i2 + i4, l1 + l3);
            this.placeLeafAt(worldIn, k1 + i3, i2 + i4, (1 + l1) - l3);
            this.placeLeafAt(worldIn, (1 + k1) - i3, i2 + i4, (1 + l1) - l3);
          }
        }
      }

      if (rand.nextBoolean()) {
        this.placeLeafAt(worldIn, k1, i2 + 2, l1);
        this.placeLeafAt(worldIn, k1 + 1, i2 + 2, l1);
        this.placeLeafAt(worldIn, k1 + 1, i2 + 2, l1 + 1);
        this.placeLeafAt(worldIn, k1, i2 + 2, l1 + 1);
      }

      for (int j3 = -3; j3 <= 4; ++j3) {
        for (int i4 = -3; i4 <= 4; ++i4) {
          if (((j3 != -3) || (i4 != -3)) && ((j3 != -3) || (i4 != 4)) && ((j3 != 4) || (i4 != -3)) && ((j3 != 4) || (i4 != 4)) && ((Math.abs(j3) < 3) || (Math.abs(i4) < 3))) {
            this.placeLeafAt(worldIn, k1 + j3, i2, l1 + i4);
          }
        }
      }

      for (int k3 = -1; k3 <= 2; ++k3) {
        for (int j4 = -1; j4 <= 2; ++j4) {
          if (((k3 < 0) || (k3 > 1) || (j4 < 0) || (j4 > 1)) && (rand.nextInt(3) <= 0)) {
            final int l4 = rand.nextInt(3) + 2;

            for (int i5 = 0; i5 < l4; ++i5) {
              this.placeLogAt(worldIn, new BlockPos(j + k3, i2 - i5 - 1, l + j4));
            }

            for (int j5 = -1; j5 <= 1; ++j5) {
              for (int l2 = -1; l2 <= 1; ++l2) {
                this.placeLeafAt(worldIn, k1 + k3 + j5, i2, l1 + j4 + l2);
              }
            }

            for (int k5 = -2; k5 <= 2; ++k5) {
              for (int l5 = -2; l5 <= 2; ++l5) {
                if ((Math.abs(k5) != 2) || (Math.abs(l5) != 2)) {
                  this.placeLeafAt(worldIn, k1 + k3 + k5, i2 - 1, l1 + j4 + l5);
                }
              }
            }
          }
        }
      }

      return true;
    }
    return false;
  }

  private boolean placeTreeOfHeight(final World worldIn, final BlockPos pos, final int height) {
    final int i = pos.getX();
    final int j = pos.getY();
    final int k = pos.getZ();
    final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
    for (int l = 0; l <= (height + 1); ++l) {
      int i1 = 1;
      if (l == 0) {
        i1 = 0;
      }
      if (l >= (height - 1)) {
        i1 = 2;
      }
      for (int j1 = -i1; j1 <= i1; ++j1) {
        for (int k1 = -i1; k1 <= i1; ++k1) {
          if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.setPos(i + j1, j + l, k + k1))) {
            return false;
          }
        }
      }
    }
    return true;
  }

  private void placeLogAt(final World worldIn, final BlockPos pos) {
    if (this.canGrowInto(worldIn.getBlockState(pos).getBlock())) {
      this.setBlockAndNotifyAdequately(worldIn, pos, TRUNK);
    }
  }

  private void placeLeafAt(final World worldIn, final int x, final int y, final int z) {
    final BlockPos blockpos = new BlockPos(x, y, z);
    final IBlockState state = worldIn.getBlockState(blockpos);

    if (state.getBlock().isAir(state, worldIn, blockpos)) {
      this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leaves);
    }
  }

  private void onPlantGrow(final World world, final BlockPos pos, final BlockPos source) {
    final IBlockState state = world.getBlockState(pos);
    state.getBlock().onPlantGrow(state, world, pos, source);
  }
}