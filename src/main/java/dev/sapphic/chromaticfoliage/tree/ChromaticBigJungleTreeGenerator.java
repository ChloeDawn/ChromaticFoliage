package dev.sapphic.chromaticfoliage.tree;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;

import java.util.Random;

public class ChromaticBigJungleTreeGenerator extends WorldGenHugeTrees {
  private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);

  private final IBlockState vines;

  public ChromaticBigJungleTreeGenerator(final ChromaticColor color) {
    super(true, 10, 20, TRUNK, ChromaticBlocks.CHROMATIC_JUNGLE_LEAVES.getDefaultState()
      .withProperty(BlockLeaves.CHECK_DECAY, false).withProperty(ChromaticFoliage.COLOR, color));
    this.vines = ChromaticBlocks.CHROMATIC_VINE.getDefaultState().withProperty(ChromaticFoliage.COLOR, color);
  }

  @Override
  @SuppressWarnings("NumericCastThatLosesPrecision")
  public boolean generate(final World world, final Random rand, final BlockPos pos) {
    final int height = this.getHeight(rand);

    if (!this.ensureGrowable(world, rand, pos, height)) {
      return false;
    }
    this.generateCrown(world, pos.up(height));

    for (int j = (pos.getY() + height) - 2 - rand.nextInt(4); j > (pos.getY() + (height / 2)); j -= 2 + rand.nextInt(4)) {
      final float f = rand.nextFloat() * ((float) Math.PI * 2.0F);
      int x = pos.getX() + (int) (0.5F + (MathHelper.cos(f) * 4.0F));
      int z = pos.getZ() + (int) (0.5F + (MathHelper.sin(f) * 4.0F));

      for (int i1 = 0; i1 < 5; ++i1) {
        x = pos.getX() + (int) (1.5F + (MathHelper.cos(f) * i1));
        z = pos.getZ() + (int) (1.5F + (MathHelper.sin(f) * i1));
        this.setBlockAndNotifyAdequately(world, new BlockPos(x, (j - 3) + (i1 / 2), z), this.woodMetadata);
      }

      final int j2 = 1 + rand.nextInt(2);
      final int j1 = j;

      for (int y = j - j2; y <= j1; ++y) {
        final int l1 = y - j1;
        this.growLeavesLayer(world, new BlockPos(x, y, z), 1 - l1);
      }
    }

    for (int y = 0; y < height; ++y) {
      final BlockPos blockpos = pos.up(y);

      if (this.canGenerateTrunk(world, blockpos)) {
        this.setBlockAndNotifyAdequately(world, blockpos, this.woodMetadata);

        if (y > 0) {
          this.generateVine(world, rand, blockpos.west(), BlockVine.EAST);
          this.generateVine(world, rand, blockpos.north(), BlockVine.SOUTH);
        }
      }

      if (y < (height - 1)) {
        final BlockPos east = blockpos.east();

        if (this.canGenerateTrunk(world, east)) {
          this.setBlockAndNotifyAdequately(world, east, this.woodMetadata);

          if (y > 0) {
            this.generateVine(world, rand, east.east(), BlockVine.WEST);
            this.generateVine(world, rand, east.north(), BlockVine.SOUTH);
          }
        }

        final BlockPos southEast = blockpos.south().east();

        if (this.canGenerateTrunk(world, southEast)) {
          this.setBlockAndNotifyAdequately(world, southEast, this.woodMetadata);

          if (y > 0) {
            this.generateVine(world, rand, southEast.east(), BlockVine.WEST);
            this.generateVine(world, rand, southEast.south(), BlockVine.NORTH);
          }
        }

        final BlockPos south = blockpos.south();

        if (this.canGenerateTrunk(world, south)) {
          this.setBlockAndNotifyAdequately(world, south, this.woodMetadata);

          if (y > 0) {
            this.generateVine(world, rand, south.west(), BlockVine.EAST);
            this.generateVine(world, rand, south.south(), BlockVine.NORTH);
          }
        }
      }
    }

    return true;
  }

  private void generateVine(final World world, final Random rand, final BlockPos pos, final PropertyBool property) {
    if ((rand.nextInt(3) > 0) && world.isAirBlock(pos)) {
      this.setBlockAndNotifyAdequately(world, pos, this.vines.withProperty(property, true));
    }
  }

  private void generateCrown(final World world, final BlockPos pos) {
    for (int y = -2; y <= 0; ++y) {
      this.growLeavesLayerStrict(world, pos.up(y), 3 - y);
    }
  }

  private boolean canGenerateTrunk(final World world, final BlockPos pos) {
    final IBlockState state = world.getBlockState(pos);
    return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos);
  }
}