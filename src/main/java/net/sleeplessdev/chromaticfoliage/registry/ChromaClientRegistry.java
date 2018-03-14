package net.sleeplessdev.chromaticfoliage.registry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDoublePlant.EnumBlockHalf;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.block.ChromaticGrassBlock;
import net.sleeplessdev.chromaticfoliage.client.ChromaColorizer;
import net.sleeplessdev.chromaticfoliage.client.ChromaMapper;
import net.sleeplessdev.chromaticfoliage.config.ChromaClientConfig;
import net.sleeplessdev.chromaticfoliage.config.ChromaFeatureConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;
import net.sleeplessdev.chromaticfoliage.data.ChromaItems;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = ChromaticFoliage.ID, value = Side.CLIENT)
public final class ChromaClientRegistry {

    private ChromaClientRegistry() {}

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        if (ChromaFeatureConfig.grassBlocks) {
            registerModelsFor(ChromaBlocks.CHROMATIC_GRASS);
            registerMapperFor(ChromaBlocks.EMISSIVE_GRASS);
        }
        if (ChromaFeatureConfig.oakLeaves) {
            registerModelsFor(ChromaBlocks.CHROMATIC_OAK_LEAVES);
            registerMapperFor(ChromaBlocks.EMISSIVE_OAK_LEAVES);
        }
        if (ChromaFeatureConfig.spruceLeaves) {
            registerModelsFor(ChromaBlocks.CHROMATIC_SPRUCE_LEAVES);
            registerMapperFor(ChromaBlocks.EMISSIVE_SPRUCE_LEAVES);
        }
        if (ChromaFeatureConfig.birchLeaves) {
            registerModelsFor(ChromaBlocks.CHROMATIC_BIRCH_LEAVES);
            registerMapperFor(ChromaBlocks.EMISSIVE_BIRCH_LEAVES);
        }
        if (ChromaFeatureConfig.jungleLeaves) {
            registerModelsFor(ChromaBlocks.CHROMATIC_JUNGLE_LEAVES);
            registerMapperFor(ChromaBlocks.EMISSIVE_JUNGLE_LEAVES);
        }
        if (ChromaFeatureConfig.acaciaLeaves) {
            registerModelsFor(ChromaBlocks.CHROMATIC_ACACIA_LEAVES);
            registerMapperFor(ChromaBlocks.EMISSIVE_ACACIA_LEAVES);
        }
        if (ChromaFeatureConfig.darkOakLeaves) {
            registerModelsFor(ChromaBlocks.CHROMATIC_DARK_OAK_LEAVES);
            registerMapperFor(ChromaBlocks.EMISSIVE_DARK_OAK_LEAVES);
        }
    }

    @SubscribeEvent
    public static void onBlockColorRegistry(ColorHandlerEvent.Block event) {
        if (ChromaFeatureConfig.grassBlocks) {
            registerColorFor(event, ChromaBlocks.CHROMATIC_GRASS);
            registerColorFor(event, ChromaBlocks.EMISSIVE_GRASS);
        }
        if (ChromaFeatureConfig.oakLeaves) {
            registerColorFor(event, ChromaBlocks.CHROMATIC_OAK_LEAVES);
            registerColorFor(event, ChromaBlocks.EMISSIVE_OAK_LEAVES);
        }
        if (ChromaFeatureConfig.spruceLeaves) {
            registerColorFor(event, ChromaBlocks.CHROMATIC_SPRUCE_LEAVES);
            registerColorFor(event, ChromaBlocks.EMISSIVE_SPRUCE_LEAVES);
        }
        if (ChromaFeatureConfig.birchLeaves) {
            registerColorFor(event, ChromaBlocks.CHROMATIC_BIRCH_LEAVES);
            registerColorFor(event, ChromaBlocks.EMISSIVE_BIRCH_LEAVES);
        }
        if (ChromaFeatureConfig.jungleLeaves) {
            registerColorFor(event, ChromaBlocks.CHROMATIC_JUNGLE_LEAVES);
            registerColorFor(event, ChromaBlocks.EMISSIVE_JUNGLE_LEAVES);
        }
        if (ChromaFeatureConfig.acaciaLeaves) {
            registerColorFor(event, ChromaBlocks.CHROMATIC_ACACIA_LEAVES);
            registerColorFor(event, ChromaBlocks.EMISSIVE_ACACIA_LEAVES);
        }
        if (ChromaFeatureConfig.darkOakLeaves) {
            registerColorFor(event, ChromaBlocks.CHROMATIC_DARK_OAK_LEAVES);
            registerColorFor(event, ChromaBlocks.EMISSIVE_DARK_OAK_LEAVES);
        }
        if (ChromaClientConfig.BLOCKS.snowLayers) {
            event.getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) -> {
                if (world != null && pos != null && pos.down().getY() >= 0) {
                    IBlockState below = world.getBlockState(pos.down());
                    if (below.getBlock() instanceof ChromaticGrassBlock) {
                        return below.getValue(ChromaColors.PROPERTY).getColorValue();
                    } else if (below.getBlock() == Blocks.SNOW_LAYER) {
                        return event.getBlockColors().colorMultiplier(below, world, pos.down(), tintIndex);
                    }
                }
                return -1;
            }, Blocks.SNOW_LAYER);
        }
        if (ChromaClientConfig.BLOCKS.grassPlants) {
            event.getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) -> {
                if (world != null && pos != null) {
                    BlockPos target = pos;
                    EnumPlantType variant = state.getValue(BlockDoublePlant.VARIANT);
                    if (variant == EnumPlantType.GRASS || variant == EnumPlantType.FERN) {
                        if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) {
                            target = target.down();
                        }
                    }
                    if (target.down().getY() >= 0) {
                        IBlockState below = world.getBlockState(target.down());
                        if (below.getBlock() instanceof ChromaticGrassBlock) {
                            return below.getValue(ChromaColors.PROPERTY).getColorValue();
                        }
                    }
                    return BiomeColorHelper.getGrassColorAtPos(world, target);
                }
                return -1;
            }, Blocks.DOUBLE_PLANT);
            event.getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) -> {
                if (world != null && pos != null) {
                    if (pos.down().getY() >= 0) {
                        IBlockState below = world.getBlockState(pos.down());
                        if (below.getBlock() instanceof ChromaticGrassBlock) {
                            return below.getValue(ChromaColors.PROPERTY).getColorValue();
                        }
                    }
                    return BiomeColorHelper.getGrassColorAtPos(world, pos);
                }
                return state.getValue(BlockTallGrass.TYPE) != EnumType.DEAD_BUSH
                       ? ColorizerGrass.getGrassColor(0.5D, 1.0D) : 0xFFFFFF;
            }, Blocks.TALLGRASS);
        }
    }

    @SubscribeEvent
    public static void onItemColorRegistry(ColorHandlerEvent.Item event) {
        if (ChromaFeatureConfig.grassBlocks) {
            registerColorFor(event, ChromaItems.CHROMATIC_GRASS);
        }
        if (ChromaFeatureConfig.oakLeaves) {
            registerColorFor(event, ChromaItems.CHROMATIC_OAK_LEAVES);
        }
        if (ChromaFeatureConfig.spruceLeaves) {
            registerColorFor(event, ChromaItems.CHROMATIC_SPRUCE_LEAVES);
        }
        if (ChromaFeatureConfig.birchLeaves) {
            registerColorFor(event, ChromaItems.CHROMATIC_BIRCH_LEAVES);
        }
        if (ChromaFeatureConfig.jungleLeaves) {
            registerColorFor(event, ChromaItems.CHROMATIC_JUNGLE_LEAVES);
        }
        if (ChromaFeatureConfig.acaciaLeaves) {
            registerColorFor(event, ChromaItems.CHROMATIC_ACACIA_LEAVES);
        }
        if (ChromaFeatureConfig.darkOakLeaves) {
            registerColorFor(event, ChromaItems.CHROMATIC_DARK_OAK_LEAVES);
        }
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        if (ChromaClientConfig.BLOCKS.snowLayers) {
            ChromaticFoliage.LOGGER.info("Injecting tint index data for {}", Blocks.SNOW_LAYER.getRegistryName());
            ResourceLocation name = Blocks.SNOW_LAYER.getRegistryName();
            String prefix = BlockSnow.LAYERS.getName() + "=";
            for (int value : BlockSnow.LAYERS.getAllowedValues()) {
                IBlockState state = Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, value);
                String variant = prefix + BlockSnow.LAYERS.getName(value);
                ModelResourceLocation mrl = new ModelResourceLocation(name, variant);
                IBakedModel model = event.getModelManager().getModel(mrl);
                for (BakedQuad quad : model.getQuads(state, null, 0)) {
                    if (tintQuadOrError(model, quad)) return;
                }
                for (EnumFacing side : EnumFacing.VALUES) {
                    for (BakedQuad quad : model.getQuads(state, side, 0)) {
                        if (tintQuadOrError(model, quad)) return;
                    }
                }
            }
        }
    }

    private static void registerModelsFor(Block block) {
        Item item = Item.getItemFromBlock(block);
        for (IBlockState state : block.getBlockState().getValidStates()) {
            String[] str = state.toString().split("\\[");
            String variant = str[1].substring(0, str[1].length() - 1);
            ModelResourceLocation mrl = new ModelResourceLocation(str[0], variant);
            ModelLoader.setCustomModelResourceLocation(item, block.getMetaFromState(state), mrl);
        }
    }

    private static void registerMapperFor(Block block) {
        ModelLoader.setCustomStateMapper(block, new ChromaMapper(block));
    }

    private static void registerColorFor(ColorHandlerEvent.Block event, Block block) {
        event.getBlockColors().registerBlockColorHandler(ChromaColorizer.INSTANCE, block);
    }

    private static void registerColorFor(ColorHandlerEvent.Item event, Item item) {
        event.getItemColors().registerItemColorHandler(ChromaColorizer.INSTANCE, item);
    }

    private static boolean tintQuadOrError(IBakedModel model, BakedQuad quad) {
        try {
            ReflectionHelper.setPrivateValue(BakedQuad.class, quad, 0, "field_178213_b", "tintIndex");
            return false;
        } catch (Exception e) {
            ChromaticFoliage.LOGGER.error("Failed to inject tint index for \"" + model.toString() + "\"!", e);
            return true;
        }
    }

}
