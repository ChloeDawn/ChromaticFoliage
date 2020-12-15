package dev.sapphic.chromaticfoliage.client.network;

import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.client.particle.ChromaticDustParticle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleManager;
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
      final int count = data.count();
      final double x = data.x();
      final double y = data.y();
      final double z = data.z();
      final IBlockState state = data.state();
      final boolean emissive = data.emissive();
      if (count == 0) {
        final double mx = data.mx();
        final double my = data.my();
        final double mz = data.mz();
        manager.addEffect(new ChromaticDustParticle(world, x, y, z, mx, my, mz, state, emissive).init());
      } else {
        final double speed = data.speed();
        for (int i = 0; i < count; i++) {
          final double ox = x + (this.random.nextGaussian() * speed);
          final double oy = y + (this.random.nextGaussian() * speed);
          final double oz = z + (this.random.nextGaussian() * speed);
          final double sx = this.random.nextGaussian() * speed;
          final double sy = this.random.nextGaussian() * speed;
          final double sz = this.random.nextGaussian() * speed;
          manager.addEffect(new ChromaticDustParticle(world, ox, oy, oz, sx, sy, sz, state, emissive).fixedMotion(sx, sy, sz).init());
        }
      }
    }
  }

  public static final class ServerProxy implements Consumer<ParticleData> {
    @Override
    public void accept(final ParticleData data) {
      throw new IllegalStateException();
    }
  }
}
