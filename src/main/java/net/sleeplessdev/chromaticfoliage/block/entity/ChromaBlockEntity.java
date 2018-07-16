package net.sleeplessdev.chromaticfoliage.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;

import static com.google.common.base.Preconditions.checkState;

public class ChromaBlockEntity extends TileEntity {
    public static final String NBT_KEY_COLOR = "color";

    private ChromaColor color;

    public ChromaColor getColor() {
        return color;
    }

    public ChromaBlockEntity withColor(ChromaColor color) {
        this.color = color;
        return this;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.requireCompoundKey(compound, ChromaBlockEntity.NBT_KEY_COLOR, Constants.NBT.TAG_INT);
        this.color = ChromaColor.VALUES[compound.getInteger(ChromaBlockEntity.NBT_KEY_COLOR)];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        checkState(this.color != null, "ChromaBlockEntity is missing a ChromaColor reference");
        compound.setInteger(ChromaBlockEntity.NBT_KEY_COLOR, this.color.ordinal());
        return compound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        final BlockPos pos = this.pos;
        checkState(pos != null, "ChromaBlockEntity is missing a BlockPos reference");
        return new SPacketUpdateTileEntity(pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        final NBTTagCompound compound = super.getUpdateTag();
        checkState(this.color != null, "ChromaBlockEntity is missing a ChromaColor reference");
        compound.setInteger(ChromaBlockEntity.NBT_KEY_COLOR, this.color.ordinal());
        return compound;
    }

    @Override
    public ITextComponent getDisplayName() {
        final Block block = this.blockType;
        checkState(block != null, "ChromaBlockEntity is missing a Block reference");
        return new TextComponentTranslation(block.getUnlocalizedName() + ".name");
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    private void requireCompoundKey(NBTTagCompound compound, String key, int type) {
        final String typeName = NBTBase.getTagTypeName(type);
        checkState(!"UNKNOWN".equals(typeName), "Unknown tag type ordinal \"" + Integer.toString(type) + "\"");
        checkState(compound.hasKey(key, type), "Missing " + typeName + "\"" + key + "\" in compound " + compound);
    }
}
