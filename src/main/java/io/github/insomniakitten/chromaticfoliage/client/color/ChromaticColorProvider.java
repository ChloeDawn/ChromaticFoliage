package io.github.insomniakitten.chromaticfoliage.client.color;

import io.github.insomniakitten.chromaticfoliage.common.base.ChromaticColor;
import io.github.insomniakitten.chromaticfoliage.common.block.ChromaticBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public final class ChromaticColorProvider implements IBlockColor, IItemColor {
  private static final ChromaticColorProvider INSTANCE = new ChromaticColorProvider();

  private ChromaticColorProvider() {}

  public static ChromaticColorProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess access, @Nullable final BlockPos pos, final int tintIndex) {
    return state.getValue(ChromaticBlock.COLOR).getColorValue();
  }

  @Override
  public int colorMultiplier(final ItemStack stack, final int tintIndex) {
    return ChromaticColor.valueOf(stack.getMetadata() & 15).getColorValue();
  }
}
