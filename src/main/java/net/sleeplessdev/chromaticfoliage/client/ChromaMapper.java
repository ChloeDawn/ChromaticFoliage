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

import static net.sleeplessdev.chromaticfoliage.data.ChromaColors.PROPERTY;

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
        String variant;
        if (state.getBlock() instanceof ChromaticVineBlock) {
            variant = getPropertyString(state.getProperties());
        } else {
            variant = "color=" + state.getValue(PROPERTY).getName();
            if (state.getBlock() instanceof ChromaticGrassBlock) {
                variant += ",snowy=" + state.getValue(BlockGrass.SNOWY);
            }
        }
        return new ModelResourceLocation(path, variant);
    }

}
