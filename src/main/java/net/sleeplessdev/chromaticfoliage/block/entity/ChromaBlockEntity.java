package net.sleeplessdev.chromaticfoliage.block.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;

public final class ChromaBlockEntity extends TileEntity {

    public static final String NBT_KEY_COLOR = "color";

    private ChromaColors color;

    public ChromaColors getColor() {
        return color;
    }

    public ChromaBlockEntity withColor(ChromaColors color) {
        this.color = color;
        return this;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        int index = compound.getInteger(NBT_KEY_COLOR);
        color = ChromaColors.VALUES[index];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger(NBT_KEY_COLOR, color.ordinal());
        return compound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();
        compound.setInteger(NBT_KEY_COLOR, color.ordinal());
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

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

}
