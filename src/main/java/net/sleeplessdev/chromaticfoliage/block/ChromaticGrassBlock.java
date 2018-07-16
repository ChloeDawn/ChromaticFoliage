package net.sleeplessdev.chromaticfoliage.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.config.ChromaClientConfig;
import net.sleeplessdev.chromaticfoliage.config.ChromaGeneralConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;
import net.sleeplessdev.chromaticfoliage.data.ChromaItems;

import java.util.Optional;
import java.util.Random;

public class ChromaticGrassBlock extends BlockGrass {
    private final boolean checkSnow;
    private final boolean spreadDirt;
    private final boolean spreadGrass;

    public ChromaticGrassBlock() {
        this.checkSnow = !ChromaClientConfig.BLOCKS.snowLayers;
        this.spreadDirt = ChromaGeneralConfig.grassSpreadDirt;
        this.spreadGrass = ChromaGeneralConfig.grassSpreadGrass;
        this.setHardness(0.6F);
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(ChromaticFoliage.TAB);
        this.setTickRandomly(this.spreadDirt || this.spreadGrass);
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        final ItemStack stack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos, facing, stack)) return false;
        if (stack.isEmpty()) return false;

        if (ChromaGeneralConfig.inWorldIllumination && stack.getItem() == Items.GLOWSTONE_DUST) {
            if (world.isRemote) return true;

            final IBlockState emissive = ChromaBlocks.EMISSIVE_GRASS.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));

            if (!world.setBlockState(pos, emissive, 3)) return false;

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
        return new ItemStack(ChromaItems.CHROMATIC_GRASS, 1, state.getValue(ChromaColor.PROPERTY).ordinal());
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (final ChromaColor color : ChromaColor.VALUES) {
            items.add(new ItemStack(this, 1, color.ordinal()));
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ChromaItems.CHROMATIC_GRASS, 1, this.getMetaFromState(state));
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side, IPlantable plant) {
        return Blocks.GRASS.canSustainPlant(Blocks.GRASS.getDefaultState(), access, pos, side, plant);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(ChromaColor.PROPERTY, ChromaColor.VALUES[meta & 15]);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos) {
        return state.withProperty(BlockGrass.SNOWY, this.checkSnow && this.isSnowBlock(access, pos.up()));
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!this.spreadDirt && !this.spreadGrass) return;
        if (world.isRemote) return;
        if (!world.isAreaLoaded(pos, 3)) return;

        final BlockPos up = pos.up();
        final int lightOpacity = world.getBlockState(up).getLightOpacity(world, up);

        if (lightOpacity <= 2 || world.getLightFromNeighbors(up) >= 4) {
            if (world.getLightFromNeighbors(up) < 9) return;

            for (int i = 0; i < 4; ++i) {
                final int x = rand.nextInt(3) - 1;
                final int y = rand.nextInt(5) - 3;
                final int z = rand.nextInt(3) - 1;
                final BlockPos offset = pos.add(x, y, z);

                if (world.isOutsideBuildHeight(offset)) return;
                if (!world.isBlockLoaded(offset)) return;
                if (!canSpreadInto(world, offset)) continue;
                if (world.getLightFromNeighbors(offset.up()) < 4) continue;
                if (lightOpacity > 2) continue;

                world.setBlockState(offset, state, 3);
            }
        } else world.setBlockState(pos, Blocks.DIRT.getDefaultState());
    }

    @Override // TODO Cleanup
    public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
        final BlockPos above = pos.up();
        for (int i = 0; i < 128; ++i) {
            BlockPos targetPos = above;
            int j = 0;
            while (true) {
                if (j >= i / 16) {
                    if (world.isAirBlock(targetPos)) {
                        if (rand.nextInt(8) == 0) {
                            world.getBiome(targetPos).plantFlower(world, rand, targetPos);
                        } else {
                            IBlockState tallGrass = Blocks.TALLGRASS.getDefaultState()
                                .withProperty(BlockTallGrass.TYPE, EnumType.GRASS);
                            if (Blocks.TALLGRASS.canBlockStay(world, targetPos, tallGrass)) {
                                world.setBlockState(targetPos, tallGrass, 3);
                            }
                        }
                    }

                    break;
                }

                targetPos = targetPos.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);

                if (!(world.getBlockState(targetPos.down()).getBlock() instanceof BlockGrass) || world.getBlockState(targetPos).isNormalCube()) {
                    break;
                }

                ++j;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ChromaColor.PROPERTY).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this)
            .add(ChromaColor.PROPERTY)
            .add(BlockGrass.SNOWY)
            .build();
    }

    protected final boolean canSpreadInto(IBlockAccess access, BlockPos pos) {
        final IBlockState state = access.getBlockState(pos);
        if (this.spreadDirt && state.getBlock() == Blocks.DIRT) {
            if (DirtType.DIRT == state.getValue(BlockDirt.VARIANT)) {
                return true;
            }
        }
        return this.spreadGrass && Blocks.GRASS == state.getBlock();
    }

    protected final boolean isSnowBlock(IBlockAccess access, BlockPos pos) {
        final Block block = access.getBlockState(pos).getBlock();
        return Blocks.SNOW == block || Blocks.SNOW_LAYER == block;
    }
}
