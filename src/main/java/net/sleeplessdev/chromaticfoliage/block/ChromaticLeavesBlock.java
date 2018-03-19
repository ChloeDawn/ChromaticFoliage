package net.sleeplessdev.chromaticfoliage.block;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
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
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.config.ChromaGeneralConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ChromaticLeavesBlock extends BlockLeaves implements IShearable {

    protected final EnumType type;

    public ChromaticLeavesBlock(EnumType type) {
        this.type = type;
        String name = ".chromatic_" + type.getName() + "_leaves";
        setUnlocalizedName(ChromaticFoliage.ID + name);
        setTickRandomly(false);
        setHardness(0.2F);
        setLightOpacity(1);
        setSoundType(SoundType.PLANT);
        setDefaultState(getDefaultState()
                .withProperty(CHECK_DECAY, false)
                .withProperty(DECAYABLE, false)
        );
    }

    private boolean isFancyGraphics() {
        return !Blocks.LEAVES.getDefaultState().isOpaqueCube();
    }

    @Override
    @Deprecated
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(ChromaColors.PROPERTY).getMapColor();
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ChromaColors.PROPERTY, ChromaColors.VALUES[meta & 15]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ChromaColors.PROPERTY).ordinal();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.canPlayerEdit(pos, facing, stack) && !stack.isEmpty()) {
            if (ChromaGeneralConfig.inWorldIllumination && stack.getItem() == Items.GLOWSTONE_DUST) {
                IBlockState emissive = getEmissiveState(state);
                if (emissive.getBlock() != Blocks.AIR) {
                    if (world.isRemote) return true;
                    if (world.setBlockState(pos, emissive, 3)) {
                        world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                        if (!player.isCreative()) stack.shrink(1);
                        return true;
                    }
                }
            } else if (ChromaGeneralConfig.chromaRecoloring) {
                Optional<ChromaColors> color = ChromaColors.getColorFor(stack);
                if (!color.isPresent()) return false;
                if (color.get() == state.getValue(ChromaColors.PROPERTY)) return false;
                if (world.isRemote) return true;
                if (world.setBlockState(pos, state.withProperty(ChromaColors.PROPERTY, color.get()), 3)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                    if (!player.isCreative()) stack.shrink(1);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        switch (type) {
            case OAK: return new ItemStack(ChromaBlocks.CHROMATIC_OAK_LEAVES, 1, getMetaFromState(state));
            case SPRUCE: return new ItemStack(ChromaBlocks.CHROMATIC_SPRUCE_LEAVES, 1, getMetaFromState(state));
            case BIRCH: return new ItemStack(ChromaBlocks.CHROMATIC_BIRCH_LEAVES, 1, getMetaFromState(state));
            case JUNGLE: return new ItemStack(ChromaBlocks.CHROMATIC_JUNGLE_LEAVES, 1, getMetaFromState(state));
            case ACACIA: return new ItemStack(ChromaBlocks.CHROMATIC_ACACIA_LEAVES, 1, getMetaFromState(state));
            case DARK_OAK: return new ItemStack(ChromaBlocks.CHROMATIC_DARK_OAK_LEAVES, 1, getMetaFromState(state));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (ChromaColors color : ChromaColors.VALUES) {
            items.add(new ItemStack(this, 1, color.ordinal()));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE, ChromaColors.PROPERTY);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return getSilkTouchDrop(state);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(ChromaColors.PROPERTY, ChromaColors.VALUES[meta & 15]);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {

    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (world.isRainingAt(pos.up())) {
            IBlockState below = world.getBlockState(pos.down());
            BlockFaceShape shape = below.getBlockFaceShape(world, pos.down(), EnumFacing.UP);
            if (shape != BlockFaceShape.SOLID && rand.nextInt(15) == 1) {
                double x = (double) ((float) pos.getX() + rand.nextFloat());
                double y = (double) pos.getY() - 0.05D;
                double z = (double) ((float) pos.getZ() + rand.nextFloat());
                world.spawnParticle(EnumParticleTypes.DRIP_WATER, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return !isFancyGraphics();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return isFancyGraphics() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }

    @Override
    public EnumType getWoodType(int meta) {
        return type;
    }

    @Override
    public void beginLeavesDecay(IBlockState state, World world, BlockPos pos) {

    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        BlockPos posAt = pos.offset(side);
        IBlockState stateAt = world.getBlockState(posAt);
        return isFancyGraphics() && stateAt.getBlock() == this || !stateAt.doesSideBlockRendering(world, posAt, side.getOpposite());

    }

    protected IBlockState getEmissiveState(IBlockState state) {
        switch (type) {
            case OAK: {
                IBlockState emissive = ChromaBlocks.EMISSIVE_OAK_LEAVES.getDefaultState();
                return emissive.withProperty(ChromaColors.PROPERTY, state.getValue(ChromaColors.PROPERTY));
            }
            case SPRUCE: {
                IBlockState emissive = ChromaBlocks.EMISSIVE_SPRUCE_LEAVES.getDefaultState();
                return emissive.withProperty(ChromaColors.PROPERTY, state.getValue(ChromaColors.PROPERTY));
            }
            case BIRCH: {
                IBlockState emissive = ChromaBlocks.EMISSIVE_BIRCH_LEAVES.getDefaultState();
                return emissive.withProperty(ChromaColors.PROPERTY, state.getValue(ChromaColors.PROPERTY));
            }
            case JUNGLE: {
                IBlockState emissive = ChromaBlocks.EMISSIVE_JUNGLE_LEAVES.getDefaultState();
                return emissive.withProperty(ChromaColors.PROPERTY, state.getValue(ChromaColors.PROPERTY));
            }
            case ACACIA: {
                IBlockState emissive = ChromaBlocks.EMISSIVE_ACACIA_LEAVES.getDefaultState();
                return emissive.withProperty(ChromaColors.PROPERTY, state.getValue(ChromaColors.PROPERTY));
            }
            case DARK_OAK: {
                IBlockState emissive = ChromaBlocks.EMISSIVE_DARK_OAK_LEAVES.getDefaultState();
                return emissive.withProperty(ChromaColors.PROPERTY, state.getValue(ChromaColors.PROPERTY));
            }
        }
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public List<ItemStack> onSheared(ItemStack held, IBlockAccess world, BlockPos pos, int fortune) {
        return Collections.singletonList(new ItemStack(this));
    }

}
