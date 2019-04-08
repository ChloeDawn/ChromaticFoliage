package io.github.insomniakitten.chromaticfoliage.common.block.entity;

import com.google.common.base.MoreObjects;
import io.github.insomniakitten.chromaticfoliage.common.base.ChromaticColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import static com.google.common.base.Preconditions.checkState;

public class ChromaticBlockEntity extends TileEntity {
  private static final String COLOR = "color";

  private int ordinal = -1;

  public ChromaticBlockEntity() {}

  public ChromaticBlockEntity(final ChromaticColor color) {
    ordinal = color.ordinal();
  }

  public final ChromaticColor getColor() {
    checkState(ordinal >= 0, "Color undefined");
    return ChromaticColor.valueOf(ordinal);
  }

  public void setColor(final ChromaticColor color) {
    ordinal = color.ordinal();
    markDirty();
  }

  @Override
  public void readFromNBT(final NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    if (nbt.hasKey(COLOR, NBT.TAG_INT)) {
      ordinal = nbt.getInteger(COLOR);
    }
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    nbt.setInteger(COLOR, ordinal);
    return nbt;
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    final NBTTagCompound nbt = super.getUpdateTag();
    nbt.setInteger(COLOR, ordinal);
    return nbt;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new TextComponentTranslation(getBlockType().getTranslationKey() + ".name");
  }

  @Override
  public void onDataPacket(final NetworkManager manager, final SPacketUpdateTileEntity packet) {
    handleUpdateTag(packet.getNbtCompound());
  }

  @Override
  public void handleUpdateTag(final NBTTagCompound nbt) {
    if (nbt.hasKey(COLOR, NBT.TAG_INT)) {
      ordinal = nbt.getInteger(COLOR);
    }
  }

  @Override
  public boolean shouldRefresh(final World world, final BlockPos pos, final IBlockState last, final IBlockState next) {
    return last.getBlock() != next.getBlock();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("color", ordinal)
      .add("world", world)
      .add("pos", pos)
      .toString();
  }
}
