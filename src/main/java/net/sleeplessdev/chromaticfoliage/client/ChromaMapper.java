package net.sleeplessdev.chromaticfoliage.client;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.block.ChromaticGrassBlock;
import net.sleeplessdev.chromaticfoliage.block.ChromaticVineBlock;
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;

@SideOnly(Side.CLIENT)
public final class ChromaMapper extends StateMapperBase {
    private final ResourceLocation path;

    public ChromaMapper(Block block) {
        Preconditions.checkState(block.getRegistryName() != null, "Block 'block' requires a registry name");
        this.path = new ResourceLocation(ChromaticFoliage.ID, block.getRegistryName().getResourcePath());
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        final StringBuilder variant = new StringBuilder();
        if (state.getBlock() instanceof ChromaticVineBlock) {
            variant.append(this.getPropertyString(state.getProperties()));
        } else {
            variant.append("color=").append(state.getValue(ChromaColor.PROPERTY).getName());
            if (state.getBlock() instanceof ChromaticGrassBlock) {
                variant.append(",snowy=").append(state.getValue(BlockGrass.SNOWY));
            }
        }
        return new ModelResourceLocation(this.path, variant.toString());
    }
}
