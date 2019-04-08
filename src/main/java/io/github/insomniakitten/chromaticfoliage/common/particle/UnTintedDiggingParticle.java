package io.github.insomniakitten.chromaticfoliage.common.particle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
final class UnTintedDiggingParticle extends ParticleDigging {
  UnTintedDiggingParticle(final IBlockState state, final World world, final double x, final double y, final double z, final double vX, final double vY, final double vZ) {
    super(world, x, y, z, vX, vY, vZ, state);
  }

  @Override
  protected void multiplyColor(@Nullable BlockPos pos) {

  }
}
