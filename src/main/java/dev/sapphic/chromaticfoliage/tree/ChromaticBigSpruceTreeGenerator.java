package dev.sapphic.chromaticfoliage.tree;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class ChromaticBigSpruceTreeGenerator extends WorldGenHugeTrees {
  private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
  private static final IBlockState PODZOL = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);

  private final boolean useBaseHeight;

  public ChromaticBigSpruceTreeGenerator(final ChromaticColor color, final boolean useBaseHeight) {
    super(true, 13, 15, TRUNK, ChromaticBlocks.CHROMATIC_SPRUCE_LEAVES.getDefaultState()
      .withProperty(BlockLeaves.CHECK_DECAY, false).withProperty(ChromaticFoliage.COLOR, color));
    this.useBaseHeight = useBaseHeight;
  }

  @Override
  public boolean generate(final World world, final Random rand, final BlockPos pos) {
    final int height = this.getHeight(rand);
    if (!this.ensureGrowable(world, rand, pos, height)) {
      return false;
    }
    this.createCrown(world, pos.getX(), pos.getZ(), pos.getY() + height, rand);
    for (int y = 0; y < height; ++y) {
      if (this.canGenerateTrunk(world, pos.up(y))) {
        this.setBlockAndNotifyAdequately(world, pos.up(y), this.woodMetadata);
      }
      if (y < (height - 1)) {
        if (this.canGenerateTrunk(world, pos.add(1, y, 0))) {
          this.setBlockAndNotifyAdequately(world, pos.add(1, y, 0), this.woodMetadata);
        }

        if (this.canGenerateTrunk(world, pos.add(1, y, 1))) {
          this.setBlockAndNotifyAdequately(world, pos.add(1, y, 1), this.woodMetadata);
        }

        if (this.canGenerateTrunk(world, pos.add(0, y, 1))) {
          this.setBlockAndNotifyAdequately(world, pos.add(0, y, 1), this.woodMetadata);
        }
      }
    }
    return true;
  }

  @Override
  public void generateSaplings(final World worldIn, final Random random, final BlockPos pos) {
    this.spreadPodzol(worldIn, pos.west().north());
    this.spreadPodzol(worldIn, pos.east(2).north());
    this.spreadPodzol(worldIn, pos.west().south(2));
    this.spreadPodzol(worldIn, pos.east(2).south(2));

    for (int i = 0; i < 5; ++i) {
      final int j = random.nextInt(64);
      final int k = j % 8;
      final int l = j / 8;

      if ((k == 0) || (k == 7) || (l == 0) || (l == 7)) {
        this.spreadPodzol(worldIn, pos.add(-3 + k, 0, -3 + l));
      }
    }
  }

  private void createCrown(final World world, final int x, final int z, final int y, final Random rand) {
    final int height = rand.nextInt(5) + (this.useBaseHeight ? this.baseHeight : 3);
    int j = 0;
    for (int oy = y - height; oy <= y; ++oy) {
      final int base = y - oy;
      final int i1 = MathHelper.floor(((float) base / height) * 3.5F);
      this.growLeavesLayerStrict(world, new BlockPos(x, oy, z), i1 + (((base > 0) && (i1 == j) && ((oy & 1) == 0)) ? 1 : 0));
      j = i1;
    }
  }

  private void spreadPodzol(final World world, final BlockPos pos) {
    for (int x = -2; x <= 2; ++x) {
      for (int z = -2; z <= 2; ++z) {
        if ((Math.abs(x) != 2) || (Math.abs(z) != 2)) {
          this.generatePodzol(world, pos.add(x, 0, z));
        }
      }
    }
  }

  private void generatePodzol(final World world, final BlockPos pos) {
    for (int y = 2; y >= -3; --y) {
      final BlockPos above = pos.up(y);
      final IBlockState state = world.getBlockState(above);
      final Block block = state.getBlock();
      if (block.canSustainPlant(state, world, above, EnumFacing.UP, (IPlantable) Blocks.SAPLING)) {
        this.setBlockAndNotifyAdequately(world, above, PODZOL);
        break;
      }
      if ((state.getMaterial() != Material.AIR) && (y < 0)) {
        break;
      }
    }
  }

  private boolean canGenerateTrunk(final World world, final BlockPos pos) {
    final IBlockState state = world.getBlockState(pos);
    return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos);
  }
}
