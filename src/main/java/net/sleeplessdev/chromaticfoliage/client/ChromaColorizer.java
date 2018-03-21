package net.sleeplessdev.chromaticfoliage.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public final class ChromaColorizer {

    public static final Impl INSTANCE = new Impl() {
        @Override
        protected int apply(@Nullable IBlockState state, ItemStack stack) {
            if (state != null) {
                ChromaColors color = state.getValue(ChromaColors.PROPERTY);
                return color.getColorValue();
            }

            if (!stack.isEmpty()) {
                int meta = stack.getMetadata() & 15;
                return ChromaColors.VALUES[meta].getColorValue();
            }

            throw new IllegalStateException("Something went wrong! Failed to infer a color from the given object");
        }
    };

    private static abstract class Impl implements IBlockColor, IItemColor {
        protected abstract int apply(@Nullable IBlockState state, ItemStack stack);

        @Override
        public int colorMultiplier(@Nullable IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
            return apply(state, ItemStack.EMPTY);
        }

        @Override
        public int colorMultiplier(ItemStack stack, int tintIndex) {
            return apply(null, stack);
        }
    }

}
