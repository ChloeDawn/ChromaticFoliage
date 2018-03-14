package net.sleeplessdev.chromaticfoliage.block;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.config.ChromaClientConfig;
import net.sleeplessdev.chromaticfoliage.config.ChromaGeneralConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;

import java.util.Optional;
import java.util.Random;

public class ChromaticGrassBlock extends BlockGrass {

    private final boolean checkSnow, spreadDirt, spreadGrass;

    public ChromaticGrassBlock() {
        this.checkSnow = !ChromaClientConfig.BLOCKS.snowLayers;
        this.spreadDirt = ChromaGeneralConfig.grassSpreadDirt;
        this.spreadGrass = ChromaGeneralConfig.grassSpreadGrass;
        setHardness(0.6F);
        setSoundType(SoundType.PLANT);
        setUnlocalizedName(ChromaticFoliage.ID + ".chromatic_grass");
        setTickRandomly(spreadDirt || spreadGrass);
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.canPlayerEdit(pos, facing, stack) && !stack.isEmpty()) {
            if (ChromaGeneralConfig.inWorldIllumination && stack.getItem() == Items.GLOWSTONE_DUST) {
                ChromaColors color = state.getValue(ChromaColors.PROPERTY);
                IBlockState emissive = ChromaBlocks.EMISSIVE_GRASS.getDefaultState()
                        .withProperty(ChromaColors.PROPERTY, color);
                if (world.setBlockState(pos, emissive, 3)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                    if (!player.isCreative()) stack.shrink(1);
                    return true;
                }
            } else if (ChromaGeneralConfig.chromaRecoloring) {
                Optional<ChromaColors> color = ChromaColors.getColorFor(stack);
                if (!color.isPresent()) return false;
                if (world.setBlockState(pos, getDefaultState().withProperty(ChromaColors.PROPERTY, color.get()))) {
                    world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                    player.swingArm(hand);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ChromaBlocks.CHROMATIC_GRASS, 1, getMetaFromState(state));
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side, IPlantable plant) {
        return Blocks.GRASS.canSustainPlant(Blocks.GRASS.getDefaultState(), world, pos, side, plant);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(ChromaColors.PROPERTY, ChromaColors.VALUES[meta & 15]);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return checkSnow ? super.getActualState(state, world, pos) : state.withProperty(SNOWY, false);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!spreadDirt && !spreadGrass) return;
        if (world.isRemote) return;
        if (!world.isAreaLoaded(pos, 3)) return;
        if (world.getLightFromNeighbors(pos.up()) < 4 && world.getBlockState(pos.up()).getLightOpacity(world, pos.up()) > 2) {
            world.setBlockState(pos, Blocks.DIRT.getDefaultState());
        } else if (world.getLightFromNeighbors(pos.up()) >= 9) {
            ChromaColors color = state.getValue(ChromaColors.PROPERTY);
            IBlockState spreader = ChromaBlocks.CHROMATIC_GRASS
                    .getDefaultState().withProperty(ChromaColors.PROPERTY, color);
            for (int i = 0; i < 4; ++i) {
                BlockPos target = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                if (world.isOutsideBuildHeight(target) || !world.isBlockLoaded(target)) return;
                if (canSpreadInto(world, target) && world.getLightFromNeighbors(target.up()) >= 4
                        && world.getBlockState(pos.up()).getLightOpacity(world, pos.up()) <= 2) {
                    world.setBlockState(target, spreader, 3);
                }
            }
        }
    }

    @Override // TODO Cleanup this copypasta
    public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
        BlockPos above = pos.up();
        for (int i = 0; i < 128; ++i) {
            BlockPos targetPos = above;
            int j = 0;
            while (true) {
                if (j >= i / 16) {
                    if (world.isAirBlock(targetPos)) {
                        if (rand.nextInt(8) == 0) {
                            world.getBiome(targetPos).plantFlower(world, rand, targetPos);
                        } else {
                            IBlockState iblockstate1 = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, EnumType.GRASS);

                            if (Blocks.TALLGRASS.canBlockStay(world, targetPos, iblockstate1)) {
                                world.setBlockState(targetPos, iblockstate1, 3);
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
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ChromaColors.PROPERTY).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockGrass.SNOWY, ChromaColors.PROPERTY);
    }

    protected boolean canSpreadInto(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (spreadDirt && state.getBlock() == Blocks.DIRT) {
            DirtType type = state.getValue(BlockDirt.VARIANT);
            return type == DirtType.DIRT;
        }
        return spreadGrass && state.getBlock() == Blocks.GRASS;
    }

}
