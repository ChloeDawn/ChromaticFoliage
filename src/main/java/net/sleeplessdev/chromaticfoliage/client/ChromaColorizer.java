package net.sleeplessdev.chromaticfoliage.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public final class ChromaColorizer implements IBlockColor, IItemColor {
    public static final ChromaColorizer INSTANCE = new ChromaColorizer();

    private ChromaColorizer() {}

    @Override
    public int colorMultiplier(@Nullable IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
        return apply(state, ItemStack.EMPTY);
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        return apply(null, stack);
    }

    private int apply(@Nullable IBlockState state, ItemStack stack) {
        if (state != null) {
            return state.getValue(ChromaColor.PROPERTY).getColorValue();
        } else if (!stack.isEmpty()) {
            return ChromaColor.VALUES[stack.getMetadata() & 15].getColorValue();
        } else throw new IllegalStateException("Failed to infer a color from the given objects");
    }
}
