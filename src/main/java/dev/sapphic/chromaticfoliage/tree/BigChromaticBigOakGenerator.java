package dev.sapphic.chromaticfoliage.tree;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BigChromaticBigOakGenerator extends WorldGenBigTree {
  private final IBlockState leaves;

  private int heightLimit;
  private int height;
  private int leafDistanceLimit = 4;

  public BigChromaticBigOakGenerator(final ChromaticColor color, final boolean notify) {
    super(notify);
    this.leaves = ChromaticBlocks.CHROMATIC_OAK_LEAVES.getDefaultState()
      .withProperty(BlockLeaves.CHECK_DECAY, false)
      .withProperty(ChromaticFoliage.COLOR, color);
  }

  @Override
  public void setDecorationDefaults() {
    this.leafDistanceLimit = 5;
  }

  @Override
  public boolean generate(final World world, final Random rand, final BlockPos pos) {
    final Random nodeRand = new Random(rand.nextLong());
    if (this.heightLimit == 0) {
      this.heightLimit = 5 + nodeRand.nextInt(12);
    }
    if (this.checkHeight(world, pos)) {
      final List<FoliageNode> nodes = this.computeLeafNodes(world, pos, nodeRand);
      this.generateLeafNodes(world, nodes);
      this.generateTrunk(world, pos);
      this.generateBranches(world, pos, nodes);
      return true;
    }
    return false;
  }

  private List<FoliageNode> computeLeafNodes(final World world, final BlockPos pos, final Random rand) {
    final List<FoliageNode> nodes = new ArrayList<>(1);
    final double heightAttenuation = 0.618D;
    //noinspection NumericCastThatLosesPrecision
    this.height = (int) (this.heightLimit * heightAttenuation);

    if (this.height >= this.heightLimit) {
      this.height = this.heightLimit - 1;
    }
    //noinspection NumericCastThatLosesPrecision
    int count = (int) (1.382 + StrictMath.pow((1.0 * this.heightLimit) / 13.0, 2.0));
    if (count < 1) {
      count = 1;
    }
    final int ceiling = pos.getY() + this.height;
    int offsetY = this.heightLimit - this.leafDistanceLimit;
    nodes.add(new FoliageNode(pos.up(offsetY), ceiling));
    while (offsetY >= 0) {
      final float size = this.getLayerSize(offsetY);
      if (size >= 0.0F) {
        for (int step = 0; step < count; ++step) {
          final double scale = 1.0 * size * (rand.nextFloat() + 0.328);
          final double variance = (rand.nextFloat() * 2.0F) * Math.PI;
          final double x = (scale * StrictMath.sin(variance)) + 0.5;
          final double z = (scale * StrictMath.cos(variance)) + 0.5;
          final BlockPos offset = pos.add(x, offsetY - 1, z);
          if (this.getActualHeight(world, offset, offset.up(this.leafDistanceLimit)) == -1) {
            final int distX = pos.getX() - offset.getX();
            final int distZ = pos.getZ() - offset.getZ();
            final double reach = offset.getY() - (Math.sqrt((distX * distX) + (distZ * distZ)) * 0.381);
            //noinspection NumericCastThatLosesPrecision
            final int up = (reach > ceiling) ? ceiling : (int) reach;
            final BlockPos above = new BlockPos(pos.getX(), up, pos.getZ());
            if (this.getActualHeight(world, above, offset) == -1) {
              nodes.add(new FoliageNode(offset, above.getY()));
            }
          }
        }
      }
      --offsetY;
    }
    return nodes;
  }

  private void crossSection(final World world, final BlockPos pos, final float radius, final IBlockState state) {
    //noinspection NumericCastThatLosesPrecision
    final int range = (int) (radius + 0.618);
    for (int x = -range; x <= range; ++x) {
      for (int z = -range; z <= range; ++z) {
        if ((StrictMath.pow(Math.abs(x) + 0.5D, 2.0D) + StrictMath.pow(Math.abs(z) + 0.5D, 2.0D)) <= (
          radius * radius
        )) {
          final BlockPos offset = pos.add(x, 0, z);
          final IBlockState other = world.getBlockState(offset);
          if (other.getBlock().isAir(other, world, offset) || other.getBlock().isLeaves(other, world, offset)) {
            this.setBlockAndNotifyAdequately(world, offset, state);
          }
        }
      }
    }
  }

  private float getLayerSize(final int y) {
    if (y < (this.heightLimit * 0.3F)) {
      return -1.0F;
    }
    final float mid = this.heightLimit / 2.0F;
    final float floor = mid - y;
    float sqrt = MathHelper.sqrt((mid * mid) - (floor * floor));
    if (floor == 0.0F) {
      sqrt = mid;
    } else if (Math.abs(floor) >= mid) {
      return 0.0F;
    }
    return sqrt * 0.5F;
  }

  private float getNodeRadius(final int y) {
    if ((y >= 0) && (y < this.leafDistanceLimit)) {
      return ((y != 0) && (y != (this.leafDistanceLimit - 1))) ? 3.0F : 2.0F;
    }
    return -1.0F;
  }

  private void limb(final World world, final BlockPos floor, final BlockPos ceiling, final IBlockState state) {
    final BlockPos origin = ceiling.add(-floor.getX(), -floor.getY(), -floor.getZ());
    final int furthestPoint = this.getFurthestPoint(origin);
    final float x = (float) origin.getX() / furthestPoint;
    final float y = (float) origin.getY() / furthestPoint;
    final float z = (float) origin.getZ() / furthestPoint;
    for (int j = 0; j <= furthestPoint; ++j) {
      final BlockPos pos = floor.add(0.5F + (j * x), 0.5F + (j * y), 0.5F + (j * z));
      final EnumAxis axis = this.getAxis(floor, pos);
      this.setBlockAndNotifyAdequately(world, pos, state.withProperty(BlockLog.LOG_AXIS, axis));
    }
  }

  private void generateLeafNodes(final World world, final List<FoliageNode> nodes) {
    final IBlockState leaves = this.leaves.withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
    for (final FoliageNode node : nodes) {
      for (int i = 0; i < this.leafDistanceLimit; ++i) {
        this.crossSection(world, node.up(i), this.getNodeRadius(i), leaves);
      }
    }
  }

  private void generateTrunk(final World world, final BlockPos pos) {
    final IBlockState log = Blocks.LOG.getDefaultState();
    final BlockPos ceiling = pos.up(this.height);
    this.limb(world, pos, ceiling, log);
  }

  private void generateBranches(final World world, final BlockPos pos, final List<FoliageNode> nodes) {
    for (final FoliageNode node : nodes) {
      final int height = node.getHeight();
      final BlockPos offset = new BlockPos(pos.getX(), height, pos.getZ());
      if (!offset.equals(node) && ((height - pos.getY()) >= (this.heightLimit * 0.2D))) {
        this.limb(world, offset, node, Blocks.LOG.getDefaultState());
      }
    }
  }

  private int getActualHeight(final World world, final BlockPos floor, final BlockPos ceiling) {
    final BlockPos offset = ceiling.add(-floor.getX(), -floor.getY(), -floor.getZ());
    final int furthestPoint = this.getFurthestPoint(offset);
    final float x = (float) offset.getX() / furthestPoint;
    final float y = (float) offset.getY() / furthestPoint;
    final float z = (float) offset.getZ() / furthestPoint;
    if (furthestPoint != 0) {
      for (int length = 0; length <= furthestPoint; ++length) {
        final BlockPos pos = floor.add(0.5F + (length * x), 0.5F + (length * y), 0.5F + (length * z));
        if (!this.isReplaceable(world, pos)) {
          return length;
        }
      }
    }
    return -1;
  }

  private int getFurthestPoint(final BlockPos pos) {
    final int x = MathHelper.abs(pos.getX());
    final int y = MathHelper.abs(pos.getY());
    final int z = MathHelper.abs(pos.getZ());
    return ((z > x) && (z > y)) ? z : Math.max(y, x);
  }

  private EnumAxis getAxis(final BlockPos from, final BlockPos to) {
    final int x = Math.abs(to.getX() - from.getX());
    final int z = Math.abs(to.getZ() - from.getZ());
    final int furthest = Math.max(x, z);
    if (furthest > 0) {
      return (x == furthest) ? EnumAxis.X : EnumAxis.Z;
    }
    return EnumAxis.Y;
  }

  private boolean checkHeight(final World world, final BlockPos pos) {
    final BlockPos down = pos.down();
    final IBlockState state = world.getBlockState(down);
    if (!state.getBlock().canSustainPlant(state, world, down, EnumFacing.UP, (IPlantable) Blocks.SAPLING)) {
      return false;
    }
    final int height = this.getActualHeight(world, pos, pos.up(this.heightLimit - 1));
    if (height == -1) {
      return true;
    }
    if (height < 6) {
      return false;
    }
    this.heightLimit = height;
    return true;
  }

  private static final class FoliageNode extends BlockPos {
    private final int root;

    FoliageNode(final BlockPos pos, final int root) {
      super(pos.getX(), pos.getY(), pos.getZ());
      this.root = root;
    }

    public int getHeight() {
      return this.root;
    }
  }
}
