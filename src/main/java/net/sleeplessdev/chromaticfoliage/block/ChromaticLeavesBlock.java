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
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ChromaticLeavesBlock extends BlockLeaves implements IShearable {
    protected final EnumType type;

    public ChromaticLeavesBlock(EnumType type) {
        this.type = type;
        this.setTickRandomly(false);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(ChromaticFoliage.TAB);
        this.setDefaultState(this.getDefaultState()
            .withProperty(CHECK_DECAY, false)
            .withProperty(DECAYABLE, false)
        );
    }

    private boolean isFancyGraphics() {
        return !Blocks.LEAVES.getDefaultState().isOpaqueCube();
    }

    @Override
    @Deprecated
    public MapColor getMapColor(IBlockState state, IBlockAccess access, BlockPos pos) {
        return state.getValue(ChromaColor.PROPERTY).getMapColor();
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ChromaColor.PROPERTY, ChromaColor.VALUES[meta & 15]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ChromaColor.PROPERTY).ordinal();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        final ItemStack stack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos, facing, stack)) return false;
        if (stack.isEmpty()) return false;

        if (ChromaGeneralConfig.inWorldIllumination && stack.getItem() == Items.GLOWSTONE_DUST) {
            if (world.isRemote) return true;
            if (!world.setBlockState(pos, this.getEmissiveState(state), 3)) return false;

            world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);

            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }

            return true;
        } else if (ChromaGeneralConfig.chromaRecoloring) {
            final Optional<ChromaColor> optionalColor = ChromaColor.from(stack);

            if (!optionalColor.isPresent()) return false;

            final ChromaColor color = optionalColor.get();

            if (color == state.getValue(ChromaColor.PROPERTY)) return false;
            if (world.isRemote) return true;
            if (!world.setBlockState(pos, state.withProperty(ChromaColor.PROPERTY, color), 3)) return false;

            world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);

            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }

            return true;
        }

        return false;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        final int meta = this.getMetaFromState(state);
        switch (this.type) {
            case OAK: return new ItemStack(ChromaBlocks.CHROMATIC_OAK_LEAVES, 1, meta);

            case SPRUCE: return new ItemStack(ChromaBlocks.CHROMATIC_SPRUCE_LEAVES, 1, meta);

            case BIRCH: return new ItemStack(ChromaBlocks.CHROMATIC_BIRCH_LEAVES, 1, meta);

            case JUNGLE: return new ItemStack(ChromaBlocks.CHROMATIC_JUNGLE_LEAVES, 1, meta);

            case ACACIA: return new ItemStack(ChromaBlocks.CHROMATIC_ACACIA_LEAVES, 1, meta);

            case DARK_OAK: return new ItemStack(ChromaBlocks.CHROMATIC_DARK_OAK_LEAVES, 1, meta);
        }

        throw new IllegalStateException("Unable to determine silk touch drop for type \"" + this.type + '"');
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (final ChromaColor color : ChromaColor.VALUES) {
            items.add(new ItemStack(this, 1, color.ordinal()));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this)
            .add(ChromaColor.PROPERTY)
            .add(BlockLeaves.CHECK_DECAY)
            .add(BlockLeaves.DECAYABLE)
            .build();
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
        return !this.isFancyGraphics();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return this.getSilkTouchDrop(state);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(ChromaColor.PROPERTY, ChromaColor.VALUES[meta & 15]);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        // No-op
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        // No-op
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (world.isRainingAt(pos.up())) {
            final BlockPos down = pos.down();
            final IBlockState below = world.getBlockState(down);
            final BlockFaceShape shape = below.getBlockFaceShape(world, down, EnumFacing.UP);
            if (shape != BlockFaceShape.SOLID && rand.nextInt(15) == 1) {
                final double x = (double) ((float) pos.getX() + rand.nextFloat());
                final double y = (double) pos.getY() - 0.05D;
                final double z = (double) ((float) pos.getZ() + rand.nextFloat());
                world.spawnParticle(EnumParticleTypes.DRIP_WATER, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return !this.isFancyGraphics();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return this.isFancyGraphics() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }

    @Override
    public EnumType getWoodType(int meta) {
        return this.type;
    }

    @Override
    public void beginLeavesDecay(IBlockState state, World world, BlockPos pos) {
        // No-op
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess access, BlockPos pos, IBlockState state, int fortune) {
        // No-op
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Deprecated
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
        final BlockPos offset = pos.offset(side);
        return !access.getBlockState(offset).doesSideBlockRendering(access, offset, side.getOpposite());
    }

    protected final IBlockState getEmissiveState(IBlockState state) {
        switch (this.type) {
            case OAK: return ChromaBlocks.EMISSIVE_OAK_LEAVES.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));

            case SPRUCE: return ChromaBlocks.EMISSIVE_SPRUCE_LEAVES.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));

            case BIRCH: return ChromaBlocks.EMISSIVE_BIRCH_LEAVES.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));

            case JUNGLE: return ChromaBlocks.EMISSIVE_JUNGLE_LEAVES.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));

            case ACACIA: return ChromaBlocks.EMISSIVE_ACACIA_LEAVES.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));

            case DARK_OAK: return ChromaBlocks.EMISSIVE_DARK_OAK_LEAVES.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));
        }

        throw new IllegalStateException("Unable to determine emissive state for type " + this.type);
    }

    @Override
    public List<ItemStack> onSheared(ItemStack stack, IBlockAccess access, BlockPos pos, int fortune) {
        return Collections.singletonList(this.getSilkTouchDrop(access.getBlockState(pos)));
    }
}
