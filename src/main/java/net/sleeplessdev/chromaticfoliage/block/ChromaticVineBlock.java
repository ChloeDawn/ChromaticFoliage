package net.sleeplessdev.chromaticfoliage.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
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
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.block.entity.ChromaBlockEntity;
import net.sleeplessdev.chromaticfoliage.config.ChromaGeneralConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;
import net.sleeplessdev.chromaticfoliage.data.ChromaItems;

import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class ChromaticVineBlock extends BlockVine {
    private final boolean isReplaceable;

    public ChromaticVineBlock() {
        this.setHardness(0.2F);
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(ChromaticFoliage.TAB);
        this.isReplaceable = ChromaGeneralConfig.replaceableVines;
    }

    @Override
    @Deprecated
    public MapColor getMapColor(IBlockState state, IBlockAccess access, BlockPos pos) {
        return state.getActualState(access, pos).getValue(ChromaColor.PROPERTY).getMapColor();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        final ItemStack stack = player.getHeldItem(hand);
        if (player.canPlayerEdit(pos, facing, stack) && !stack.isEmpty()) {
            if (ChromaGeneralConfig.inWorldIllumination && stack.getItem() == Items.GLOWSTONE_DUST) {
                if (world.isRemote) return true;
                final IBlockState actualState = state.getActualState(world, pos);
                IBlockState emissive = ChromaBlocks.EMISSIVE_VINE.getDefaultState();
                for (final Entry<IProperty<?>, Comparable<?>> prop : actualState.getProperties().entrySet()) {
                    //noinspection unchecked,RedundantCast
                    emissive = emissive.withProperty((IProperty) prop.getKey(), (Comparable) prop.getValue());
                }
                if (world.setBlockState(pos, emissive, 3)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                    if (!player.isCreative()) stack.shrink(1);
                    return true;
                }
            } else if (ChromaGeneralConfig.chromaRecoloring) {
                Optional<ChromaColor> optionalColor = ChromaColor.from(stack);
                if (optionalColor.isPresent()) {
                    final IBlockState actualState = state.getActualState(world, pos);
                    final ChromaColor color = optionalColor.get();
                    if (color != actualState.getValue(ChromaColor.PROPERTY)) {
                        if (world.isRemote) return true;
                        if (world.setBlockState(pos, actualState.withProperty(ChromaColor.PROPERTY, color), 3)) {
                            world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                            if (!player.capabilities.isCreativeMode) stack.shrink(1);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof ChromaBlockEntity) {
            final int meta = stack.getMetadata();
            final ChromaColor color = ChromaColor.VALUES[meta & 15];
            ((ChromaBlockEntity) tileEntity).withColor(color);
        }
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (final ChromaColor color : ChromaColor.VALUES) {
            items.add(new ItemStack(this, 1, color.ordinal()));
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new ChromaBlockEntity().withColor(state.getValue(ChromaColor.PROPERTY));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        final IBlockState actualState = state.getActualState(world, pos);
        final ChromaColor color = actualState.getValue(ChromaColor.PROPERTY);
        return new ItemStack(ChromaItems.CHROMATIC_VINE, 1, color.ordinal());
    }

    private boolean isAcceptableNeighbor(World world, BlockPos pos, EnumFacing side) {
        final IBlockState state = world.getBlockState(pos);
        final BlockFaceShape shape = state.getBlockFaceShape(world, pos, side);
        return shape == BlockFaceShape.SOLID && !BlockVine.isExceptBlockForAttaching(state.getBlock());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos) {
        final IBlockState actualState = super.getActualState(state, access, pos);
        final TileEntity tileEntity = access.getTileEntity(pos);
        if (tileEntity instanceof ChromaBlockEntity) {
            final ChromaColor color = ((ChromaBlockEntity) tileEntity).getColor();
            return actualState.withProperty(ChromaColor.PROPERTY, color);
        }
        return actualState;
    }

    @Override
    public boolean isReplaceable(IBlockAccess access, BlockPos pos) {
        return this.isReplaceable;
    }

    @Override
    public boolean canAttachTo(World world, BlockPos pos, EnumFacing side) {
        Block block = world.getBlockState(pos.up()).getBlock();
        return this.isAcceptableNeighbor(world, pos.offset(side.getOpposite()), side)
            && (block == Blocks.AIR || block instanceof BlockVine
            || this.isAcceptableNeighbor(world, pos.up(), EnumFacing.UP));
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!world.isRemote && !this.recheckGrownSides(world, pos, state)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.isRemote || world.rand.nextInt(4) != 0 || !world.isAreaLoaded(pos, 4)) return;
        int j = 5;
        boolean flag = false;
        areaCheck:
        for (int x = -4; x <= 4; ++x) {
            for (int y = -4; y <= 4; ++y) {
                for (int z = -1; z <= 1; ++z) {
                    Block block = world.getBlockState(pos.add(x, z, y)).getBlock();
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
        EnumFacing randSide = EnumFacing.random(rand);
        BlockPos posUp = pos.up();
        if (randSide == EnumFacing.UP && pos.getY() < 255 && world.isAirBlock(posUp)) {
            state = state.getActualState(world, pos);
            IBlockState stateAt = state;
            for (EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
                if (rand.nextBoolean() && canAttachTo(world, posUp, side.getOpposite())) {
                    stateAt = stateAt.withProperty(getPropertyFor(side), true);
                } else stateAt = stateAt.withProperty(getPropertyFor(side), false);
            }
            if (stateAt.getValue(BlockVine.NORTH) || stateAt.getValue(BlockVine.EAST)
                || stateAt.getValue(BlockVine.SOUTH) || stateAt.getValue(BlockVine.WEST)) {
                world.setBlockState(posUp, stateAt, 2);
            }
        } else if (randSide.getAxis().isHorizontal() && !state.getValue(getPropertyFor(randSide))) {
            if (flag) return;
            BlockPos blockpos4 = pos.offset(randSide);
            IBlockState iblockstate3 = world.getBlockState(blockpos4);
            Block block1 = iblockstate3.getBlock();
            if (block1.isAir(iblockstate3, world, blockpos4)) {
                EnumFacing rotY = randSide.rotateY();
                EnumFacing rotYCCW = randSide.rotateYCCW();
                boolean valRotY = state.getValue(getPropertyFor(rotY));
                boolean valRotYCCW = state.getValue(getPropertyFor(rotYCCW));
                BlockPos posRotY = blockpos4.offset(rotY);
                BlockPos posRotYCCW = blockpos4.offset(rotYCCW);
                ChromaColor color = state.getActualState(world, pos).getValue(ChromaColor.PROPERTY);
                IBlockState retState = getDefaultState().withProperty(ChromaColor.PROPERTY, color);
                if (valRotY && canAttachTo(world, posRotY.offset(rotY), rotY)) {
                    world.setBlockState(blockpos4, retState.withProperty(getPropertyFor(rotY), true), 2);
                } else if (valRotYCCW && canAttachTo(world, posRotYCCW.offset(rotYCCW), rotYCCW)) {
                    world.setBlockState(blockpos4, retState.withProperty(getPropertyFor(rotYCCW), true), 2);
                } else if (valRotY && world.isAirBlock(posRotY) && canAttachTo(world, posRotY, randSide)) {
                    world.setBlockState(posRotY, retState.withProperty(getPropertyFor(randSide.getOpposite()), true), 2);
                } else if (valRotYCCW && world.isAirBlock(posRotYCCW) && canAttachTo(world, posRotYCCW, randSide)) {
                    world.setBlockState(posRotYCCW, retState.withProperty(getPropertyFor(randSide.getOpposite()), true), 2);
                }
            } else if (iblockstate3.getBlockFaceShape(world, blockpos4, randSide) == BlockFaceShape.SOLID) {
                world.setBlockState(pos, state.withProperty(getPropertyFor(randSide), true), 2);
            }
        } else if (pos.getY() > 1) {
            BlockPos posDown = pos.down();
            IBlockState below = world.getBlockState(posDown);
            Block block = below.getBlock();
            if (below.getMaterial() == Material.AIR) {
                IBlockState originalState = state;
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                    if (rand.nextBoolean()) {
                        originalState = originalState.withProperty(getPropertyFor(enumfacing), false);
                    }
                }
                if (originalState.getValue(BlockVine.NORTH) || originalState.getValue(BlockVine.EAST)
                    || originalState.getValue(BlockVine.SOUTH) || originalState.getValue(BlockVine.WEST)) {
                    world.setBlockState(posDown, originalState, 2);
                }
            } else if (block instanceof BlockVine) {
                IBlockState originalState = below;
                for (EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
                    PropertyBool prop = getPropertyFor(side);
                    if (rand.nextBoolean() && state.getValue(prop)) {
                        originalState = originalState.withProperty(prop, true);
                    }
                }
                if (originalState.getValue(BlockVine.NORTH) || originalState.getValue(BlockVine.EAST)
                    || originalState.getValue(BlockVine.SOUTH) || originalState.getValue(BlockVine.WEST)) {
                    world.setBlockState(posDown, originalState, 2);
                }
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, meta, placer).withProperty(ChromaColor.PROPERTY, ChromaColor.VALUES[meta & 15]);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tile, ItemStack stack) {
        if (!world.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(Objects.requireNonNull(StatList.getBlockStats(this)));
            Block.spawnAsEntity(world, pos, new ItemStack(ChromaItems.CHROMATIC_VINE, 1, 0));
        } else super.harvestBlock(world, player, pos, state, tile, stack);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this)
            .add(ChromaColor.PROPERTY)
            .add(BlockVine.UP)
            .add(BlockVine.NORTH)
            .add(BlockVine.SOUTH)
            .add(BlockVine.WEST)
            .add(BlockVine.EAST)
            .build();
    }

    private boolean recheckGrownSides(World world, BlockPos pos, IBlockState state) {
        IBlockState actualState = state.getActualState(world, pos);
        final IBlockState originalState = actualState;

        for (final EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
            final PropertyBool property = BlockVine.getPropertyFor(side);

            if (actualState.getValue(property) && !this.canAttachTo(world, pos, side.getOpposite())) {
                final IBlockState above = world.getBlockState(pos.up());

                if ((!(above.getBlock() instanceof BlockVine)) || !above.getValue(property)) {
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
