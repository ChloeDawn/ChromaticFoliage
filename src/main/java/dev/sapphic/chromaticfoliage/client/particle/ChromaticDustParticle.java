package dev.sapphic.chromaticfoliage.client.particle;

import dev.sapphic.chromaticfoliage.block.ChromaticGrassBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ChromaticDustParticle extends ParticleDigging {
  private final IBlockState state;
  private final boolean emissive;

  public ChromaticDustParticle(final World world, final Vec3d position, final Vec3d velocity, final IBlockState state, final boolean emissive) {
    super(world, position.x, position.y, position.z, velocity.x, velocity.y, velocity.z, state);
    this.state = state;
    this.emissive = emissive;
  }

  public ChromaticDustParticle fixedMotion(final Vec3d velocity) {
    this.motionX = velocity.x;
    this.motionY = velocity.y;
    this.motionZ = velocity.z;
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
