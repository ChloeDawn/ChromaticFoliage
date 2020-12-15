package dev.sapphic.chromaticfoliage.client.particle;

import dev.sapphic.chromaticfoliage.block.ChromaticGrassBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ChromaticDustParticle extends ParticleDigging {
  private final IBlockState state;
  private final boolean emissive;

  public ChromaticDustParticle(final World world, final double x, final double y, final double z, final double mx, final double my, final double mz, final IBlockState state, final boolean emissive) {
    super(world, x, y, z, mx, my, mz, state);
    this.state = state;
    this.emissive = emissive;
  }

  public ChromaticDustParticle fixedMotion(final double mx, final double my, final double mz) {
    this.motionX = mx;
    this.motionY = my;
    this.motionZ = mz;
    return this;
  }

  @Override
  protected void multiplyColor(final @Nullable BlockPos pos) {
    if (!(this.state.getBlock() instanceof ChromaticGrassBlock)) {
      super.multiplyColor(pos);
    }
  }

  @Override
  public int getBrightnessForRender(final float delta) {
    return this.emissive ? 0xF000F0 : super.getBrightnessForRender(delta);
  }
}
