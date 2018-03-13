package net.sleeplessdev.chromaticfoliage.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;

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
        String[] str = state.toString().split("\\[");
        String variant = str[1].substring(0, str[1].length() - 1);
        return new ModelResourceLocation(path, variant);
    }

}
