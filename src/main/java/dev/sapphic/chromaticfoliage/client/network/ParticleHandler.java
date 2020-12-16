package dev.sapphic.chromaticfoliage.client.network;

import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.client.particle.ChromaticDustParticle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Random;
import java.util.function.Consumer;

public final class ParticleHandler implements IMessageHandler<ParticleData, IMessage> {
  @SidedProxy(modId = ChromaticFoliage.ID)
  private static @MonotonicNonNull Consumer<ParticleData> proxy;

  @Override
  public @Nullable IMessage onMessage(final ParticleData data, final MessageContext ctx) {
    proxy.accept(data);
    return null;
  }

  @SideOnly(Side.CLIENT)
  public static final class ClientProxy implements Consumer<ParticleData> {
    private final Random random = new Random();

    @Override
    public void accept(final ParticleData data) {
      final ParticleManager manager = Minecraft.getMinecraft().effectRenderer;
      final WorldClient world = Minecraft.getMinecraft().world;
      final int particleCount = data.getParticleCount();
      final Vec3d position = data.getPosition();
      final Vec3d velocity = data.getVelocity();
      final IBlockState state = data.getBlockState();
      final boolean emissive = data.isEmissive();
      if (particleCount == 0) {
        manager.addEffect(new ChromaticDustParticle(world, position, velocity, state, emissive).init());
      } else {
        final double velocityMultiplier = data.getVelocityMultiplier();
        for (int i = 0; i < particleCount; i++) {
          final double ox = position.x + (this.random.nextGaussian() * velocity.x);
          final double oy = position.y + (this.random.nextGaussian() * velocity.y);
          final double oz = position.z + (this.random.nextGaussian() * velocity.z);
          final Vec3d randPosition = new Vec3d(ox, oy, oz);
          final double sx = this.random.nextGaussian() * velocityMultiplier;
          final double sy = this.random.nextGaussian() * velocityMultiplier;
          final double sz = this.random.nextGaussian() * velocityMultiplier;
          final Vec3d randVelocity = new Vec3d(sx, sy, sz);
          manager.addEffect(new ChromaticDustParticle(world, randPosition, randVelocity, state, emissive).fixedMotion(randVelocity).init());
        }
      }
    }
  }

  public static final class ServerProxy implements Consumer<ParticleData> {
    @Override
    public void accept(final ParticleData data) {
      throw new IllegalStateException("Cannot handle particle data in server context");
    }
  }
}
