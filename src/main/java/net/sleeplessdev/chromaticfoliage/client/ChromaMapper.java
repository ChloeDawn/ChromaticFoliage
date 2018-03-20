package net.sleeplessdev.chromaticfoliage.client;

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
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;

@SideOnly(Side.CLIENT)
public final class ChromaMapper extends StateMapperBase {

    private final ResourceLocation path;

    public ChromaMapper(Block block) {
        assert block.getRegistryName() != null;
        String name = block.getRegistryName().getResourcePath();
        name = name.replace("emissive", "chromatic");
        path = new ResourceLocation(ChromaticFoliage.ID, name);
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        if (state.getBlock() instanceof ChromaticVineBlock) {
            String[] str = state.toString().split("\\[");
            String variant = str[1].substring(0, str[1].length() - 1);
            return new ModelResourceLocation(path, variant);
        }
        String variant = "color=" + state.getValue(ChromaColors.PROPERTY).getName();
        if (state.getBlock() instanceof ChromaticGrassBlock) {
            variant += ",snowy=" + state.getValue(BlockGrass.SNOWY);
        }
        return new ModelResourceLocation(path, variant);
    }

}
