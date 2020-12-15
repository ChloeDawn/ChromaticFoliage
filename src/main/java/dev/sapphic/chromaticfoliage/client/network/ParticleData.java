package dev.sapphic.chromaticfoliage.client.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

public final class ParticleData implements IMessage {
  private int count;
  private double x;
  private double y;
  private double z;
  private double speed;
  private double mx;
  private double my;
  private double mz;
  private @MonotonicNonNull IBlockState state;
  private boolean emissive;

  public ParticleData(final int count, final double x, final double y, final double z, final double speed, final double mx, final double my, final double mz, final IBlockState state, final boolean emissive) {
    this.count = count;
    this.x = x;
    this.y = y;
    this.z = z;
    this.speed = speed;
    this.mx = mx;
    this.my = my;
    this.mz = mz;
    this.state = state;
    this.emissive = emissive;
  }

  public ParticleData(final double x, final double y, final double z, final double speed, final double mx, final double my, final double mz, final IBlockState state, final boolean emissive) {
    this(0, x, y, z, speed, mx, my, mz, state, emissive);
  }

  public ParticleData() {
  }

  public int count() {
    return this.count;
  }

  public double x() {
    return this.x;
  }

  public double y() {
    return this.y;
  }

  public double z() {
    return this.z;
  }

  public double speed() {
    return this.speed;
  }

  public double mx() {
    return this.mx;
  }

  public double my() {
    return this.my;
  }

  public double mz() {
    return this.mz;
  }

  public IBlockState state() {
    return this.state;
  }

  public boolean emissive() {
    return this.emissive;
  }

  @Override
  public void fromBytes(final ByteBuf buf) {
    this.count = buf.readInt();
    this.x = buf.readDouble();
    this.y = buf.readDouble();
    this.z = buf.readDouble();
    this.speed = buf.readDouble();
    this.mx = buf.readDouble();
    this.my = buf.readDouble();
    this.mz = buf.readDouble();
    this.state = Block.getStateById(buf.readInt());
    this.emissive = buf.readBoolean();
  }

  @Override
  public void toBytes(final ByteBuf buf) {
    buf.writeInt(this.count);
    buf.writeDouble(this.x);
    buf.writeDouble(this.y);
    buf.writeDouble(this.z);
    buf.writeDouble(this.speed);
    buf.writeDouble(this.mx);
    buf.writeDouble(this.my);
    buf.writeDouble(this.mz);
    buf.writeInt(Block.getStateId(this.state));
    buf.writeBoolean(this.emissive);
  }
}
