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

public class ChromaticAcaciaTreeGenerator extends WorldGenAbstractTree {
  private static final IBlockState TRUNK =
    Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);

  private final IBlockState leaves;

  public ChromaticAcaciaTreeGenerator(final ChromaticColor color) {
    super(true);
    this.leaves = ChromaticBlocks.CHROMATIC_ACACIA_LEAVES.getDefaultState()
      .withProperty(BlockLeaves.CHECK_DECAY, false)
      .withProperty(ChromaticFoliage.COLOR, color);
  }

  @Override
  public boolean generate(final World world, final Random rand, final BlockPos pos) {
    final int height = rand.nextInt(3) + rand.nextInt(3) + 5;
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
            if ((y >= 0) && (y < 256)) {
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
      final BlockPos below = pos.down();
      IBlockState stateBelow = world.getBlockState(below);
      final boolean isSoil = stateBelow.getBlock().canSustainPlant(stateBelow, world, below, EnumFacing.UP, (IPlantable) Blocks.SAPLING);
      if (isSoil && (pos.getY() < (world.getHeight() - height - 1))) {
        stateBelow.getBlock().onPlantGrow(stateBelow, world, below, pos);
        final EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
        final int k2 = height - rand.nextInt(4) - 1;
        int l2 = 3 - rand.nextInt(3);
        int i3 = pos.getX();
        int j1 = pos.getZ();
        int k1 = 0;
        for (int l1 = 0; l1 < height; ++l1) {
          final int i2 = pos.getY() + l1;
          if ((l1 >= k2) && (l2 > 0)) {
            i3 += enumfacing.getXOffset();
            j1 += enumfacing.getZOffset();
            --l2;
          }
          final BlockPos blockpos = new BlockPos(i3, i2, j1);
          stateBelow = world.getBlockState(blockpos);
          final boolean isAir = stateBelow.getBlock().isAir(stateBelow, world, blockpos);
          if (isAir || stateBelow.getBlock().isLeaves(stateBelow, world, blockpos)) {
            this.generateTrunk(world, blockpos);
            k1 = i2;
          }
        }
        BlockPos blockpos2 = new BlockPos(i3, k1, j1);
        for (int j3 = -3; j3 <= 3; ++j3) {
          for (int i4 = -3; i4 <= 3; ++i4) {
            if ((Math.abs(j3) != 3) || (Math.abs(i4) != 3)) {
              this.generateLeaves(world, blockpos2.add(j3, 0, i4));
            }
          }
        }
        blockpos2 = blockpos2.up();
        for (int k3 = -1; k3 <= 1; ++k3) {
          for (int j4 = -1; j4 <= 1; ++j4) {
            this.generateLeaves(world, blockpos2.add(k3, 0, j4));
          }
        }
        this.generateLeaves(world, blockpos2.east(2));
        this.generateLeaves(world, blockpos2.west(2));
        this.generateLeaves(world, blockpos2.south(2));
        this.generateLeaves(world, blockpos2.north(2));
        i3 = pos.getX();
        j1 = pos.getZ();
        final EnumFacing enumfacing1 = EnumFacing.Plane.HORIZONTAL.random(rand);
        if (enumfacing1 != enumfacing) {
          final int l3 = k2 - rand.nextInt(2) - 1;
          int k4 = 1 + rand.nextInt(3);
          k1 = 0;
          for (int l4 = l3; (l4 < height) && (k4 > 0); --k4) {
            if (l4 >= 1) {
              final int j2 = pos.getY() + l4;
              i3 += enumfacing1.getXOffset();
              j1 += enumfacing1.getZOffset();
              final BlockPos blockpos1 = new BlockPos(i3, j2, j1);
              stateBelow = world.getBlockState(blockpos1);

              if (stateBelow.getBlock().isAir(stateBelow, world, blockpos1) || stateBelow.getBlock()
                .isLeaves(stateBelow, world, blockpos1)) {
                this.generateTrunk(world, blockpos1);
                k1 = j2;
              }
            }

            ++l4;
          }
          if (k1 > 0) {
            BlockPos blockpos3 = new BlockPos(i3, k1, j1);
            for (int i5 = -2; i5 <= 2; ++i5) {
              for (int k5 = -2; k5 <= 2; ++k5) {
                if ((Math.abs(i5) != 2) || (Math.abs(k5) != 2)) {
                  this.generateLeaves(world, blockpos3.add(i5, 0, k5));
                }
              }
            }
            blockpos3 = blockpos3.up();
            for (int j5 = -1; j5 <= 1; ++j5) {
              for (int l5 = -1; l5 <= 1; ++l5) {
                this.generateLeaves(world, blockpos3.add(j5, 0, l5));
              }
            }
          }
        }
        return true;
      }
    }
    return false;
  }

  private void generateTrunk(final World world, final BlockPos pos) {
    this.setBlockAndNotifyAdequately(world, pos, TRUNK);
  }

  private void generateLeaves(final World world, final BlockPos pos) {
    final IBlockState state = world.getBlockState(pos);
    if (state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos)) {
      this.setBlockAndNotifyAdequately(world, pos, this.leaves);
    }
  }
}
