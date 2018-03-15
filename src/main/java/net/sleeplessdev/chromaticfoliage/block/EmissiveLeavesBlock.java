package net.sleeplessdev.chromaticfoliage.block;

import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;

public class EmissiveLeavesBlock extends ChromaticLeavesBlock {

    public EmissiveLeavesBlock(EnumType type) {
        super(type);
        setLightLevel(0.5F);
    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0xF000F0;
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tile, ItemStack stack) {
        spawnAsEntity(world, pos, new ItemStack(Items.GLOWSTONE_DUST));
        super.harvestBlock(world, player, pos, state, tile, stack);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) return false;
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) return stack.isEmpty();
        if (player.canPlayerEdit(pos, facing, stack) && stack.isEmpty()) {
            IBlockState chroma = getChromaticState(state);
            if (chroma.getBlock() != Blocks.AIR && world.setBlockState(pos, chroma, 3)) {
                world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                ItemStack glowstone = new ItemStack(Items.GLOWSTONE_DUST);
                if (!player.inventory.addItemStackToInventory(glowstone)) {
                    spawnAsEntity(world, pos, glowstone);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {

    }

    protected IBlockState getChromaticState(IBlockState state) {
        switch (type) {
            case OAK: {
                IBlockState emissive = ChromaBlocks.CHROMATIC_OAK_LEAVES.getDefaultState();
                return emissive.withProperty(ChromaColors.PROPERTY, state.getValue(ChromaColors.PROPERTY));
            }
            case SPRUCE: {
                IBlockState emissive = ChromaBlocks.CHROMATIC_SPRUCE_LEAVES.getDefaultState();
                return emissive.withProperty(ChromaColors.PROPERTY, state.getValue(ChromaColors.PROPERTY));
            }
            case BIRCH: {
                IBlockState emissive = ChromaBlocks.CHROMATIC_BIRCH_LEAVES.getDefaultState();
                return emissive.withProperty(ChromaColors.PROPERTY, state.getValue(ChromaColors.PROPERTY));
            }
            case JUNGLE: {
                IBlockState emissive = ChromaBlocks.CHROMATIC_JUNGLE_LEAVES.getDefaultState();
                return emissive.withProperty(ChromaColors.PROPERTY, state.getValue(ChromaColors.PROPERTY));
            }
            case ACACIA: {
                IBlockState emissive = ChromaBlocks.CHROMATIC_ACACIA_LEAVES.getDefaultState();
                return emissive.withProperty(ChromaColors.PROPERTY, state.getValue(ChromaColors.PROPERTY));
            }
            case DARK_OAK: {
                IBlockState emissive = ChromaBlocks.CHROMATIC_DARK_OAK_LEAVES.getDefaultState();
                return emissive.withProperty(ChromaColors.PROPERTY, state.getValue(ChromaColors.PROPERTY));
            }
        }
        return Blocks.AIR.getDefaultState();
    }

}
