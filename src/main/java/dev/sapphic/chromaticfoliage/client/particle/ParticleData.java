package dev.sapphic.chromaticfoliage.client.particle;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

public final class ParticleData implements IMessage {
  private int particleCount;
  private @MonotonicNonNull Vec3d position;
  private @MonotonicNonNull Vec3d velocity;
  private double velocityMultiplier;
  private @MonotonicNonNull IBlockState blockState;
  private boolean emissive;

  public ParticleData(final int particleCount, final @MonotonicNonNull Vec3d position, final @MonotonicNonNull Vec3d velocity, final double velocityMultiplier, final @MonotonicNonNull IBlockState blockState, final boolean emissive) {
    this.particleCount = particleCount;
    this.position = position;
    this.velocity = velocity;
    this.velocityMultiplier = velocityMultiplier;
    this.blockState = blockState;
    this.emissive = emissive;
  }

  public ParticleData(final Vec3d position, final Vec3d velocity, final double velocityMultiplier, final IBlockState blockState, final boolean emissive) {
    this(0, position, velocity, velocityMultiplier, blockState, emissive);
  }

  public ParticleData() {
  }

  public int getParticleCount() {
    return this.particleCount;
  }

  public Vec3d getPosition() {
    return this.position;
  }

  public Vec3d getVelocity() {
    return this.velocity;
  }

  public double getVelocityMultiplier() {
    return this.velocityMultiplier;
  }


  public IBlockState getBlockState() {
    return this.blockState;
  }

  public boolean isEmissive() {
    return this.emissive;
  }

  @Override
  public void fromBytes(final ByteBuf buf) {
    this.particleCount = buf.readInt();
    this.position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    this.velocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    this.velocityMultiplier = buf.readDouble();
    this.blockState = Block.getStateById(buf.readInt());
    this.emissive = buf.readBoolean();
  }

  @Override
  public void toBytes(final ByteBuf buf) {
    buf.writeInt(this.particleCount);
    buf.writeDouble(this.position.x).writeDouble(this.position.y).writeDouble(this.position.z);
    buf.writeDouble(this.velocity.x).writeDouble(this.velocity.y).writeDouble(this.velocity.z);
    buf.writeDouble(this.velocityMultiplier);
    buf.writeInt(Block.getStateId(this.blockState));
    buf.writeBoolean(this.emissive);
  }
}
