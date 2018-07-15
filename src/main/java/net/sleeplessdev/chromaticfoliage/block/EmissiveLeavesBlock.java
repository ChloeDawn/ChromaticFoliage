package net.sleeplessdev.chromaticfoliage.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;

public class EmissiveLeavesBlock extends ChromaticLeavesBlock {
    public EmissiveLeavesBlock(EnumType type) {
        super(type);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tile, ItemStack stack) {
        Block.spawnAsEntity(world, pos, new ItemStack(Items.GLOWSTONE_DUST));
        super.harvestBlock(world, player, pos, state, tile, stack);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 7;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) return false;

        final ItemStack stack = player.getHeldItem(hand);

        if (world.isRemote) return stack.isEmpty();
        if (!stack.isEmpty()) return false;
        if (!player.canPlayerEdit(pos, facing, stack)) return false;

        if (world.setBlockState(pos, this.getChromaticState(state), 3)) {
            world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
            final ItemStack glowstone = new ItemStack(Items.GLOWSTONE_DUST);

            if (!player.inventory.addItemStackToInventory(glowstone)) {
                Block.spawnAsEntity(world, pos, glowstone);
            }

            return true;
        }

        return false;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        // No-op
    }

    protected IBlockState getChromaticState(IBlockState state) {
        switch (type) {
            case OAK: return ChromaBlocks.CHROMATIC_OAK_LEAVES.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));

            case SPRUCE: return ChromaBlocks.CHROMATIC_SPRUCE_LEAVES.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));

            case BIRCH: return ChromaBlocks.CHROMATIC_BIRCH_LEAVES.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));

            case JUNGLE: return ChromaBlocks.CHROMATIC_JUNGLE_LEAVES.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));

            case ACACIA: return ChromaBlocks.CHROMATIC_ACACIA_LEAVES.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));

            case DARK_OAK: return ChromaBlocks.CHROMATIC_DARK_OAK_LEAVES.getDefaultState()
                .withProperty(ChromaColor.PROPERTY, state.getValue(ChromaColor.PROPERTY));
        }

        throw new IllegalStateException("Unable to determine chromatic state for type " + this.type);
    }
}
