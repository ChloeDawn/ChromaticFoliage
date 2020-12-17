package dev.sapphic.chromaticfoliage.block;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.block.entity.ChromaticBlockEntity;
import dev.sapphic.chromaticfoliage.client.ChromaticParticles;
import dev.sapphic.chromaticfoliage.client.particle.ChromaticDustParticle;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import dev.sapphic.chromaticfoliage.init.ChromaticItems;
import dev.sapphic.chromaticfoliage.init.ChromaticSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.Random;

public class ChromaticVineBlock extends BlockVine {
  private final Random rand = new Random();

  public ChromaticVineBlock() {
    this.setHardness(0.2F);
    this.setSoundType(SoundType.PLANT);
  }

  protected boolean isEmissive() {
    return false;
  }

  @Override
  @Deprecated
  public MapColor getMapColor(final IBlockState state, final IBlockAccess access, final BlockPos pos) {
    return state.getActualState(access, pos).getValue(ChromaticFoliage.COLOR).getMapColor();
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    final ItemStack stack = player.getHeldItem(hand);
    if (!player.canPlayerEdit(pos, facing, stack) || stack.isEmpty()) {
      return false;
    }
    if (ChromaticConfig.General.inWorldIllumination && (Items.GLOWSTONE_DUST == stack.getItem())) {
      if (world.isRemote) {
        player.swingArm(hand);
        return true;
      }
      final IBlockState actualState = state.getActualState(world, pos);
      final ChromaticColor color = actualState.getValue(ChromaticFoliage.COLOR);
      IBlockState vine = ChromaticBlocks.EMISSIVE_VINE.getDefaultState().withProperty(ChromaticFoliage.COLOR, color);
      for (final EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
        final PropertyBool property = getPropertyFor(side);
        vine = vine.withProperty(property, actualState.getValue(property));
      }
      world.setBlockState(pos, vine);
      world.playSound(null, pos, ChromaticSounds.BLOCK_ILLUMINATED, SoundCategory.BLOCKS, 1.0F, 0.8F);
      if (!player.capabilities.isCreativeMode) {
        stack.shrink(1);
      }
      return true;
    }
    if (ChromaticConfig.General.recolorRecipes) {
      final IBlockState actualState = state.getActualState(world, pos);
      final Block block = actualState.getBlock();
      if ((block instanceof IGrowable) && ((IGrowable) block).canUseBonemeal(world, world.rand, pos, actualState)) {
        if ((stack.getItem() == Items.DYE) && (stack.getMetadata() == EnumDyeColor.WHITE.getDyeDamage())) {
          return false;
        }
      }
      final @Nullable ChromaticColor color = ChromaticColor.of(stack);
      if ((color == null) || (color == actualState.getValue(ChromaticFoliage.COLOR))) {
        return false;
      }
      if (world.isRemote) {
        player.swingArm(hand);
        return true;
      }
      world.setBlockState(pos, actualState.withProperty(ChromaticFoliage.COLOR, color), 3);
      world.playSound(null, pos, ChromaticSounds.BLOCK_DYED, SoundCategory.BLOCKS, 1.0F, 0.8F);
      if (!player.capabilities.isCreativeMode) {
        stack.shrink(1);
      }
      return true;
    }
    return false;
  }

  @Override
  protected ItemStack getSilkTouchDrop(final IBlockState state) {
    return new ItemStack(this, 1, state.getValue(ChromaticFoliage.COLOR).ordinal());
  }

  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    final @Nullable TileEntity te = world.getTileEntity(pos);
    if (te instanceof ChromaticBlockEntity) {
      ((ChromaticBlockEntity) te).setColor(ChromaticColor.of(stack.getMetadata() & 15));
    }
  }

  @Override
  public void getSubBlocks(final CreativeTabs group, final NonNullList<ItemStack> items) {
    for (final ChromaticColor color : ChromaticColor.COLORS) {
      items.add(new ItemStack(this, 1, color.ordinal()));
    }
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(final World world, final IBlockState state) {
    return new ChromaticBlockEntity(state.getValue(ChromaticFoliage.COLOR));
  }

  @Override
  public ItemStack getPickBlock(final IBlockState state, final RayTraceResult target, final World world, final BlockPos pos, final EntityPlayer player) {
    return this.getSilkTouchDrop(state.getActualState(world, pos));
  }

  @Override
  public boolean recolorBlock(final World world, final BlockPos pos, final EnumFacing side, final EnumDyeColor color) {
    return world.setBlockState(pos, world.getBlockState(pos).withProperty(ChromaticFoliage.COLOR, ChromaticColor.of(color)));
  }

  @Override
  public IBlockState getActualState(final IBlockState state, final IBlockAccess access, final BlockPos pos) {
    final IBlockState actualState = super.getActualState(state, access, pos);
    final TileEntity te = Objects.requireNonNull(access.getTileEntity(pos));
    final ChromaticColor color = ((ChromaticBlockEntity) te).getColor();
    return actualState.withProperty(ChromaticFoliage.COLOR, color);
  }

  @Override
  public boolean isReplaceable(final IBlockAccess access, final BlockPos pos) {
    return ChromaticConfig.General.replaceableVines;
  }

  @Override
  public boolean canAttachTo(final World world, final BlockPos pos, final EnumFacing side) {
    final Block block = world.getBlockState(pos.up()).getBlock();
    return this.isAcceptableNeighbor(world, pos.offset(side.getOpposite()), side)
      && ((block == Blocks.AIR) || (block instanceof BlockVine)
      || this.isAcceptableNeighbor(world, pos.up(), EnumFacing.UP));
  }

  @Override
  public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
    if (!world.isRemote && !this.recheckGrownSides(world, pos, state)) {
      this.dropBlockAsItem(world, pos, state, 0);
      world.setBlockToAir(pos);
    }
  }

  @Override
  public void updateTick(final World world, final BlockPos pos, final IBlockState state, final Random rand) {
    if (world.isRemote || (world.rand.nextInt(4) != 0) || !world.isAreaLoaded(pos, 4)) {
      return;
    }
    int j = 5;
    boolean flag = false;
    areaCheck:
    for (int x = -4; x <= 4; ++x) {
      for (int y = -4; y <= 4; ++y) {
        for (int z = -1; z <= 1; ++z) {
          final Block block = world.getBlockState(pos.add(x, z, y)).getBlock();
          if (block instanceof BlockVine) {
            --j;
            if (j <= 0) {
              flag = true;
              break areaCheck;
            }
          }
        }
      }
    }
    final IBlockState actualState = state.getActualState(world, pos);
    final ChromaticColor color = actualState.getValue(ChromaticFoliage.COLOR);
    final EnumFacing randSide = EnumFacing.random(rand);
    final BlockPos posUp = pos.up();
    if ((randSide == EnumFacing.UP) && (pos.getY() < 255) && world.isAirBlock(posUp)) {
      IBlockState stateAt = actualState;
      for (final EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
        if (rand.nextBoolean() && this.canAttachTo(world, posUp, side.getOpposite())) {
          stateAt = stateAt.withProperty(getPropertyFor(side), true);
        } else {
          stateAt = stateAt.withProperty(getPropertyFor(side), false);
        }
      }
      if (stateAt.getValue(BlockVine.NORTH) || stateAt.getValue(BlockVine.EAST)
        || stateAt.getValue(BlockVine.SOUTH) || stateAt.getValue(BlockVine.WEST)) {
        world.setBlockState(posUp, stateAt, 2);
      }
    } else if (randSide.getAxis().isHorizontal() && !actualState.getValue(getPropertyFor(randSide))) {
      if (flag) {
        return;
      }
      final BlockPos blockpos4 = pos.offset(randSide);
      final IBlockState iblockstate3 = world.getBlockState(blockpos4);
      final Block block1 = iblockstate3.getBlock();
      if (block1.isAir(iblockstate3, world, blockpos4)) {
        final EnumFacing rotY = randSide.rotateY();
        final EnumFacing rotYCCW = randSide.rotateYCCW();
        final boolean valRotY = actualState.getValue(getPropertyFor(rotY));
        final boolean valRotYCCW = actualState.getValue(getPropertyFor(rotYCCW));
        final BlockPos posRotY = blockpos4.offset(rotY);
        final BlockPos posRotYCCW = blockpos4.offset(rotYCCW);
        final IBlockState retState = ChromaticBlocks.CHROMATIC_VINE.getDefaultState().withProperty(ChromaticFoliage.COLOR, color);
        if (valRotY && this.canAttachTo(world, posRotY.offset(rotY), rotY)) {
          world.setBlockState(blockpos4, retState.withProperty(getPropertyFor(rotY), true), 2);
        } else if (valRotYCCW && this.canAttachTo(world, posRotYCCW.offset(rotYCCW), rotYCCW)) {
          world.setBlockState(blockpos4, retState.withProperty(getPropertyFor(rotYCCW), true), 2);
        } else if (valRotY && world.isAirBlock(posRotY) && this.canAttachTo(world, posRotY, randSide)) {
          world.setBlockState(posRotY, retState.withProperty(getPropertyFor(randSide.getOpposite()), true), 2);
        } else if (valRotYCCW && world.isAirBlock(posRotYCCW) && this.canAttachTo(world, posRotYCCW, randSide)) {
          world.setBlockState(posRotYCCW, retState.withProperty(getPropertyFor(randSide.getOpposite()), true), 2);
          world.setTileEntity(posRotYCCW, new ChromaticBlockEntity(color));
        }
      } else if (iblockstate3.getBlockFaceShape(world, blockpos4, randSide) == BlockFaceShape.SOLID) {
        final IBlockState chromatic = ChromaticBlocks.CHROMATIC_VINE.getDefaultState();
        world.setBlockState(pos, chromatic.withProperty(ChromaticFoliage.COLOR, color).withProperty(getPropertyFor(randSide), true), 2);
        world.setTileEntity(pos, new ChromaticBlockEntity(color));
      }
    } else if (pos.getY() > 1) {
      final BlockPos posDown = pos.down();
      final IBlockState below = world.getBlockState(posDown);
      final Block block = below.getBlock();
      if (block.isAir(below, world, posDown)) {
        IBlockState chromatic = ChromaticBlocks.CHROMATIC_VINE.getDefaultState().withProperty(ChromaticFoliage.COLOR, color);
        for (final EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
          if (rand.nextBoolean()) {
            chromatic = chromatic.withProperty(getPropertyFor(side), false);
          } else {
            chromatic = chromatic.withProperty(getPropertyFor(side), actualState.getValue(getPropertyFor(side)));
          }
        }
        if (chromatic.getValue(BlockVine.NORTH) || chromatic.getValue(BlockVine.EAST)
          || chromatic.getValue(BlockVine.SOUTH) || chromatic.getValue(BlockVine.WEST)) {
          world.setBlockState(posDown, chromatic, 2);
        }
      } else if (block == this) {
        IBlockState originalState = below;
        for (final EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
          final PropertyBool prop = getPropertyFor(side);
          if (rand.nextBoolean() && actualState.getValue(prop)) {
            originalState = originalState.withProperty(prop, true);
          }
        }
        if (originalState.getValue(BlockVine.NORTH) || originalState.getValue(BlockVine.EAST)
          || originalState.getValue(BlockVine.SOUTH) || originalState.getValue(BlockVine.WEST)) {
          world.setBlockState(posDown, originalState, 2);
          world.setTileEntity(posDown, new ChromaticBlockEntity(color));
        }
      }
    }
  }

  @Override
  public boolean addLandingEffects(final IBlockState state, final WorldServer world, final BlockPos pos, final IBlockState state1, final EntityLivingBase entity, final int scale) {
    ChromaticParticles.landing(state, world, pos, entity, scale, this.isEmissive());
    return true;
  }

  @Override
  public boolean addRunningEffects(final IBlockState state, final World world, final BlockPos pos, final Entity entity) {
    ChromaticParticles.sprinting(this.rand, state, world, pos, entity, this.isEmissive());
    return true;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean addHitEffects(final IBlockState state, final World world, final RayTraceResult hit, final ParticleManager manager) {
    final BlockPos pos = hit.getBlockPos();
    final EnumFacing side = hit.sideHit;
    final AxisAlignedBB aabb = state.getBoundingBox(world, pos);
    double x = pos.getX() + (this.rand.nextDouble() * (aabb.maxX - aabb.minX - 0.2)) + 0.1 + aabb.minX;
    double y = pos.getY() + (this.rand.nextDouble() * (aabb.maxY - aabb.minY - 0.2)) + 0.1 + aabb.minY;
    double z = pos.getZ() + (this.rand.nextDouble() * (aabb.maxZ - aabb.minZ - 0.2)) + 0.1 + aabb.minZ;
    if (side == EnumFacing.DOWN) {
      y = (pos.getY() + aabb.minY) - 0.1;
    } else if (side == EnumFacing.UP) {
      y = pos.getY() + aabb.maxY + 0.1;
    } else if (side == EnumFacing.NORTH) {
      z = (pos.getZ() + aabb.minZ) - 0.1;
    } else if (side == EnumFacing.SOUTH) {
      z = pos.getZ() + aabb.maxZ + 0.1;
    } else if (side == EnumFacing.WEST) {
      x = (pos.getX() + aabb.minX) - 0.1;
    } else if (side == EnumFacing.EAST) {
      x = pos.getX() + aabb.maxX + 0.1;
    }
    manager.addEffect(new ChromaticDustParticle(world, new Vec3d(x, y, z), Vec3d.ZERO, state, this.isEmissive()).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
    return true;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean addDestroyEffects(final World world, final BlockPos pos, final ParticleManager manager) {
    final IBlockState state = world.getBlockState(pos);
    for (int x = 0; x < 4; ++x) {
      for (int y = 0; y < 4; ++y) {
        for (int z = 0; z < 4; ++z) {
          final double ox = (x + 0.5) / 4.0;
          final double oy = (y + 0.5) / 4.0;
          final double oz = (z + 0.5) / 4.0;
          final Vec3d position = new Vec3d(pos.getX() + ox, pos.getY() + oy, pos.getZ() + oz);
          final Vec3d velocity = new Vec3d(ox - 0.5, oy - 0.5, oz - 0.5);
          manager.addEffect(new ChromaticDustParticle(world, position, velocity, state, this.isEmissive()).setBlockPos(pos));
        }
      }
    }
    return true;
  }

  @Override
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
    return super.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, meta, placer)
      .withProperty(ChromaticFoliage.COLOR, ChromaticColor.of(meta & 15));
  }

  @Override
  public void harvestBlock(final World world, final EntityPlayer player, final BlockPos pos, final IBlockState state, final @Nullable TileEntity te, final ItemStack stack) {
    if (!world.isRemote && (stack.getItem() == Items.SHEARS)) {
      player.addStat(Objects.requireNonNull(StatList.getBlockStats(this)));
      Block.spawnAsEntity(world, pos, new ItemStack(ChromaticItems.CHROMATIC_VINE, 1, 0));
    } else {
      super.harvestBlock(world, player, pos, state, te, stack);
    }
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, ChromaticFoliage.COLOR, UP, NORTH, SOUTH, WEST, EAST);
  }

  @SuppressWarnings("MethodOverridesInaccessibleMethodOfSuper")
  private boolean isAcceptableNeighbor(final World world, final BlockPos pos, final EnumFacing side) {
    final IBlockState state = world.getBlockState(pos);
    final BlockFaceShape shape = state.getBlockFaceShape(world, pos, side);
    return (shape == BlockFaceShape.SOLID) && !BlockVine.isExceptBlockForAttaching(state.getBlock());
  }

  @SuppressWarnings("MethodOverridesInaccessibleMethodOfSuper")
  private boolean recheckGrownSides(final World world, final BlockPos pos, final IBlockState state) {
    IBlockState actualState = state.getActualState(world, pos);
    final IBlockState originalState = actualState;
    for (final EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
      final PropertyBool property = BlockVine.getPropertyFor(side);
      if (actualState.getValue(property) && !this.canAttachTo(world, pos, side.getOpposite())) {
        final IBlockState above = world.getBlockState(pos.up());
        if (!(above.getBlock() instanceof BlockVine) || !above.getValue(property)) {
          actualState = actualState.withProperty(property, false);
        }
      }
    }
    if (BlockVine.getNumGrownFaces(actualState) != 0) {
      if (originalState != actualState) {
        world.setBlockState(pos, actualState, 2);
      }
      return true;
    }
    return false;
  }
}
