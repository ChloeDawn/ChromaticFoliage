package dev.sapphic.chromaticfoliage.tree;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
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

public class ChromaticSpruceTreeGenerator extends WorldGenAbstractTree {
  private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);

  private final IBlockState leaves;

  public ChromaticSpruceTreeGenerator(final ChromaticColor color) {
    super(true);
    this.leaves = ChromaticBlocks.CHROMATIC_SPRUCE_LEAVES.getDefaultState()
      .withProperty(BlockLeaves.CHECK_DECAY, false).withProperty(ChromaticFoliage.COLOR, color);
  }

  @Override
  public boolean generate(final World world, final Random rand, final BlockPos pos) {
    final int height = rand.nextInt(4) + 6;
    final int j = 1 + rand.nextInt(2);
    final int l = 2 + rand.nextInt(2);
    if ((pos.getY() >= 1) && ((pos.getY() + height + 1) <= world.getHeight())) {
      boolean canGenerate = true;
      for (int y = pos.getY(); (y <= (pos.getY() + 1 + height)) && canGenerate; ++y) {
        final int j1;
        if ((y - pos.getY()) < j) {
          j1 = 0;
        } else {
          j1 = l;
        }
        final BlockPos.MutableBlockPos offset = new BlockPos.MutableBlockPos();
        for (int x = pos.getX() - j1; (x <= (pos.getX() + j1)) && canGenerate; ++x) {
          for (int z = pos.getZ() - j1; (z <= (pos.getZ() + j1)) && canGenerate; ++z) {
            if ((y >= 0) && (y < world.getHeight())) {
              final IBlockState state = world.getBlockState(offset.setPos(x, y, z));

              if (!state.getBlock().isAir(state, world, offset.setPos(x, y, z))
                && !state.getBlock().isLeaves(state, world, offset.setPos(x, y, z))) {
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
      IBlockState state = world.getBlockState(below);
      if (state.getBlock().canSustainPlant(state, world, below, EnumFacing.UP, (IPlantable) Blocks.SAPLING) && (pos.getY() < (world.getHeight() - height - 1))) {
        state.getBlock().onPlantGrow(state, world, below, pos);
        int i3 = rand.nextInt(2);
        int j3 = 1;
        int k3 = 0;
        final int k = height - j;
        for (int l3 = 0; l3 <= k; ++l3) {
          final int j4 = (pos.getY() + height) - l3;
          for (int x = pos.getX() - i3; x <= (pos.getX() + i3); ++x) {
            final int j2 = x - pos.getX();
            for (int z = pos.getZ() - i3; z <= (pos.getZ() + i3); ++z) {
              final int l2 = z - pos.getZ();
              if ((Math.abs(j2) != i3) || (Math.abs(l2) != i3) || (i3 <= 0)) {
                final BlockPos blockpos = new BlockPos(x, j4, z);
                state = world.getBlockState(blockpos);
                if (state.getBlock().canBeReplacedByLeaves(state, world, blockpos)) {
                  this.setBlockAndNotifyAdequately(world, blockpos, this.leaves);
                }
              }
            }
          }
          if (i3 >= j3) {
            i3 = k3;
            k3 = 1;
            ++j3;

            if (j3 > l) {
              j3 = l;
            }
          } else {
            ++i3;
          }
        }
        final int i4 = rand.nextInt(3);
        for (int k4 = 0; k4 < (height - i4); ++k4) {
          final BlockPos upN = pos.up(k4);
          state = world.getBlockState(upN);

          if (state.getBlock().isAir(state, world, upN) || state.getBlock().isLeaves(state, world, upN)) {
            this.setBlockAndNotifyAdequately(world, pos.up(k4), TRUNK);
          }
        }

        return true;
      }
    }
    return false;
  }
}
