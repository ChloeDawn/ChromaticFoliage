package io.github.insomniakitten.chromaticfoliage.common.particle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ChromaticParticles {
  private static final ParticleManager PARTICLE_MANAGER = Minecraft.getMinecraft().effectRenderer;

  private ChromaticParticles() {
    throw new UnsupportedOperationException();
  }

  public static void postHitParticle(final IBlockState state, final World world, final BlockPos pos, final double x, final double y, final double z) {
    final UnTintedDiggingParticle particle = new UnTintedDiggingParticle(state, world, x, y, z, 0.0, 0.0, 0.0);
    particle.setBlockPos(pos);
    particle.multiplyVelocity(0.2F);
    particle.multipleParticleScaleBy(0.6F);
    PARTICLE_MANAGER.addEffect(particle);
  }

  public static void postBreakParticle(final IBlockState state, final World world, final BlockPos pos, final double rX, final double rY, final double rZ) {
    final double x = pos.getX() + rX;
    final double y = pos.getY() + rY;
    final double z = pos.getZ() + rZ;
    final double vX = rX - 0.5;
    final double vY = rY - 0.5;
    final double vZ = rZ - 0.5;
    final UnTintedDiggingParticle particle = new UnTintedDiggingParticle(state, world, x, y, z, vX, vY, vZ);
    particle.setBlockPos(pos);
    PARTICLE_MANAGER.addEffect(particle);
  }
}
