package dev.sapphic.chromaticfoliage.block.entity;

import dev.sapphic.chromaticfoliage.ChromaticColor;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

public class ChromaticBlockEntity extends TileEntity {
  private static final String COLOR = "color";

  private @MonotonicNonNull ChromaticColor color;

  public ChromaticBlockEntity() {
  }

  public ChromaticBlockEntity(final ChromaticColor color) {
    this.color = color;
  }

  public final ChromaticColor getColor() {
    return this.color;
  }

  public void setColor(final ChromaticColor color) {
    this.color = color;
    this.markDirty();
  }

  @Override
  public void readFromNBT(final NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    if (nbt.hasKey(COLOR, NBT.TAG_INT)) {
      this.color = ChromaticColor.of(nbt.getInteger(COLOR));
    }
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    nbt.setInteger(COLOR, this.color.ordinal());
    return nbt;
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.getPos(), 0, this.getUpdateTag());
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    final NBTTagCompound nbt = super.getUpdateTag();
    nbt.setInteger(COLOR, this.color.ordinal());
    return nbt;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new TextComponentTranslation(this.getBlockType().getTranslationKey() + ".name");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void onDataPacket(final NetworkManager manager, final SPacketUpdateTileEntity packet) {
    this.handleUpdateTag(packet.getNbtCompound());
  }

  @Override
  public void handleUpdateTag(final NBTTagCompound nbt) {
    if (nbt.hasKey(COLOR, NBT.TAG_INT)) {
      this.color = ChromaticColor.of(nbt.getInteger(COLOR));
    }
  }

  @Override
  public boolean shouldRefresh(final World world, final BlockPos pos, final IBlockState last, final IBlockState next) {
    return last.getBlock() != next.getBlock();
  }
}
