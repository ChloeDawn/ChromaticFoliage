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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.config.ChromaGeneralConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;
import net.sleeplessdev.chromaticfoliage.data.ChromaItems;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class ChromaticVineBlock extends BlockVine {

    private final boolean isReplaceable;

    public ChromaticVineBlock() {
        setHardness(0.2F);
        setSoundType(SoundType.PLANT);
        setCreativeTab(ChromaticFoliage.TAB);
        setUnlocalizedName(ChromaticFoliage.ID + ".chromatic_vine");
        isReplaceable = ChromaGeneralConfig.replaceableVines;
    }

    @Override
    @Deprecated
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getActualState(world, pos).getValue(ChromaColors.PROPERTY).getMapColor();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.canPlayerEdit(pos, facing, stack) && !stack.isEmpty()) {
            if (ChromaGeneralConfig.inWorldIllumination && stack.getItem() == Items.GLOWSTONE_DUST) {
                if (world.isRemote) return true;
                state = state.getActualState(world, pos);
                IBlockState emissive = ChromaBlocks.EMISSIVE_VINE.getDefaultState();
                for (Map.Entry<IProperty<?>, Comparable<?>> prop : state.getProperties().entrySet()) {
                    //noinspection unchecked,RedundantCast
                    emissive = emissive.withProperty((IProperty) prop.getKey(), (Comparable) prop.getValue());
                }
                if (world.setBlockState(pos, emissive, 3)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                    if (!player.isCreative()) stack.shrink(1);
                    return true;
                }
            } else if (ChromaGeneralConfig.chromaRecoloring) {
                Optional<ChromaColors> color = ChromaColors.getColorFor(stack);
                if (!color.isPresent()) return false;
                state = state.getActualState(world, pos);
                if (color.get() == state.getValue(ChromaColors.PROPERTY)) return false;
                if (world.isRemote) return true;
                IBlockState colorState = state.withProperty(ChromaColors.PROPERTY, color.get());
                if (world.setBlockState(pos, colorState, 3)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                    if (!player.isCreative()) stack.shrink(1);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (ChromaColors color : ChromaColors.VALUES) {
            items.add(new ItemStack(this, 1, color.ordinal()));
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new VineBlockEntity().withColor(state.getValue(ChromaColors.PROPERTY));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        int meta = state.getActualState(world, pos).getValue(ChromaColors.PROPERTY).ordinal();
        return new ItemStack(ChromaItems.CHROMATIC_VINE, 1, meta);
    }

    private boolean isAcceptableNeighbor(World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlockFaceShape(world, pos, side) == BlockFaceShape.SOLID
                && !isExceptBlockForAttaching(state.getBlock());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof VineBlockEntity) {
            ChromaColors color = ((VineBlockEntity) tile).getColor();
            state = state.withProperty(ChromaColors.PROPERTY, color);
        }
        return super.getActualState(state, world, pos);
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, BlockPos pos) {
        return isReplaceable;
    }

    @Override
    public boolean canAttachTo(World world, BlockPos pos, EnumFacing side) {
        Block block = world.getBlockState(pos.up()).getBlock();
        return isAcceptableNeighbor(world, pos.offset(side.getOpposite()), side)
                && (block == Blocks.AIR || block instanceof BlockVine
                || isAcceptableNeighbor(world, pos.up(), EnumFacing.UP));
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!world.isRemote && !recheckGrownSides(world, pos, state)) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

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
            if (stateAt.getValue(NORTH) || stateAt.getValue(EAST)
                    || stateAt.getValue(SOUTH) || stateAt.getValue(WEST)) {
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
                ChromaColors color = state.getActualState(world, pos).getValue(ChromaColors.PROPERTY);
                IBlockState retState = getDefaultState().withProperty(ChromaColors.PROPERTY, color);
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
                if (originalState.getValue(NORTH) || originalState.getValue(EAST)
                        || originalState.getValue(SOUTH) || originalState.getValue(WEST)) {
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
                if (originalState.getValue(NORTH) || originalState.getValue(EAST)
                        || originalState.getValue(SOUTH) || originalState.getValue(WEST)) {
                    world.setBlockState(posDown, originalState, 2);
                }
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer)
                .withProperty(ChromaColors.PROPERTY, ChromaColors.VALUES[meta & 15]);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        if (!world.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(Objects.requireNonNull(StatList.getBlockStats(this)));
            spawnAsEntity(world, pos, new ItemStack(ChromaItems.CHROMATIC_VINE, 1, 0));
        } else super.harvestBlock(world, player, pos, state, te, stack);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ChromaColors.PROPERTY, UP, NORTH, EAST, SOUTH, WEST);
    }

    private boolean recheckGrownSides(World world, BlockPos pos, IBlockState state) {
        state = state.getActualState(world, pos);
        IBlockState originalState = state;
        for (EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
            PropertyBool prop = getPropertyFor(side);
            if (state.getValue(prop) && !canAttachTo(world, pos, side.getOpposite())) {
                IBlockState above = world.getBlockState(pos.up());
                if ((!(above.getBlock() instanceof BlockVine)) || !above.getValue(prop)) {
                    state = state.withProperty(prop, false);
                }
            }
        }
        if (getNumGrownFaces(state) != 0) {
            if (originalState != state) {
                world.setBlockState(pos, state, 2);
            }
            return true;
        }
        return false;
    }

    public static final class VineBlockEntity extends TileEntity {
        private static final String NBT_KEY = "color";

        private ChromaColors color;

        public ChromaColors getColor() {
            return color;
        }

        public VineBlockEntity withColor(ChromaColors color) {
            this.color = color;
            return this;
        }

        @Override
        public void readFromNBT(NBTTagCompound compound) {
            super.readFromNBT(compound);
            int index = compound.getInteger(NBT_KEY);
            color = ChromaColors.VALUES[index];
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            super.writeToNBT(compound);
            compound.setInteger(NBT_KEY, color.ordinal());
            return compound;
        }

        @Override
        public SPacketUpdateTileEntity getUpdatePacket() {
            return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
        }

        @Override
        public NBTTagCompound getUpdateTag() {
            NBTTagCompound compound = super.getUpdateTag();
            compound.setInteger(NBT_KEY, color.ordinal());
            return compound;
        }

        @Override
        public ITextComponent getDisplayName() {
            String name = getBlockType().getUnlocalizedName() + ".name";
            return new TextComponentTranslation(name);
        }

        @Override
        public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
            handleUpdateTag(pkt.getNbtCompound());
        }
    }

}
