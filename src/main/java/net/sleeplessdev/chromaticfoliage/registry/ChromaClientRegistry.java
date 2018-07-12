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
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
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
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;
import net.sleeplessdev.chromaticfoliage.data.ChromaItems;

import static com.google.common.base.Preconditions.checkState;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = ChromaticFoliage.ID, value = Side.CLIENT)
public final class ChromaClientRegistry {
    private static String tintIndexFieldName;

    private ChromaClientRegistry() {
        throw new UnsupportedOperationException("Cannot instantiate " + this.getClass());
    }

    @SubscribeEvent
    static void onModelRegistry(ModelRegistryEvent event) {
        if (ChromaFeatureConfig.grassBlocks) {
            registerStateMapper(ChromaBlocks.CHROMATIC_GRASS);
            registerStateMapper(ChromaBlocks.EMISSIVE_GRASS);
            registerModels(ChromaItems.CHROMATIC_GRASS);
        }

        if (ChromaFeatureConfig.oakLeaves) {
            registerStateMapper(ChromaBlocks.CHROMATIC_OAK_LEAVES);
            registerStateMapper(ChromaBlocks.EMISSIVE_OAK_LEAVES);
            registerModels(ChromaItems.CHROMATIC_OAK_LEAVES);
        }

        if (ChromaFeatureConfig.spruceLeaves) {
            registerStateMapper(ChromaBlocks.CHROMATIC_SPRUCE_LEAVES);
            registerStateMapper(ChromaBlocks.EMISSIVE_SPRUCE_LEAVES);
            registerModels(ChromaItems.CHROMATIC_SPRUCE_LEAVES);
        }

        if (ChromaFeatureConfig.birchLeaves) {
            registerStateMapper(ChromaBlocks.CHROMATIC_BIRCH_LEAVES);
            registerStateMapper(ChromaBlocks.EMISSIVE_BIRCH_LEAVES);
            registerModels(ChromaItems.CHROMATIC_BIRCH_LEAVES);
        }

        if (ChromaFeatureConfig.jungleLeaves) {
            registerStateMapper(ChromaBlocks.CHROMATIC_JUNGLE_LEAVES);
            registerStateMapper(ChromaBlocks.EMISSIVE_JUNGLE_LEAVES);
            registerModels(ChromaItems.CHROMATIC_JUNGLE_LEAVES);
        }

        if (ChromaFeatureConfig.acaciaLeaves) {
            registerStateMapper(ChromaBlocks.CHROMATIC_ACACIA_LEAVES);
            registerStateMapper(ChromaBlocks.EMISSIVE_ACACIA_LEAVES);
            registerModels(ChromaItems.CHROMATIC_ACACIA_LEAVES);
        }

        if (ChromaFeatureConfig.darkOakLeaves) {
            registerStateMapper(ChromaBlocks.CHROMATIC_DARK_OAK_LEAVES);
            registerStateMapper(ChromaBlocks.EMISSIVE_DARK_OAK_LEAVES);
            registerModels(ChromaItems.CHROMATIC_DARK_OAK_LEAVES);
        }

        if (ChromaFeatureConfig.vines) {
            registerStateMapper(ChromaBlocks.CHROMATIC_VINE);
            registerStateMapper(ChromaBlocks.EMISSIVE_VINE);
            registerModels(ChromaItems.CHROMATIC_VINE);
        }
    }

    @SubscribeEvent
    static void onBlockColorRegistry(ColorHandlerEvent.Block event) {
        if (ChromaFeatureConfig.grassBlocks) {
            registerBlockColor(event, ChromaBlocks.CHROMATIC_GRASS);
            registerBlockColor(event, ChromaBlocks.EMISSIVE_GRASS);
        }

        if (ChromaFeatureConfig.oakLeaves) {
            registerBlockColor(event, ChromaBlocks.CHROMATIC_OAK_LEAVES);
            registerBlockColor(event, ChromaBlocks.EMISSIVE_OAK_LEAVES);
        }

        if (ChromaFeatureConfig.spruceLeaves) {
            registerBlockColor(event, ChromaBlocks.CHROMATIC_SPRUCE_LEAVES);
            registerBlockColor(event, ChromaBlocks.EMISSIVE_SPRUCE_LEAVES);
        }

        if (ChromaFeatureConfig.birchLeaves) {
            registerBlockColor(event, ChromaBlocks.CHROMATIC_BIRCH_LEAVES);
            registerBlockColor(event, ChromaBlocks.EMISSIVE_BIRCH_LEAVES);
        }

        if (ChromaFeatureConfig.jungleLeaves) {
            registerBlockColor(event, ChromaBlocks.CHROMATIC_JUNGLE_LEAVES);
            registerBlockColor(event, ChromaBlocks.EMISSIVE_JUNGLE_LEAVES);
        }

        if (ChromaFeatureConfig.acaciaLeaves) {
            registerBlockColor(event, ChromaBlocks.CHROMATIC_ACACIA_LEAVES);
            registerBlockColor(event, ChromaBlocks.EMISSIVE_ACACIA_LEAVES);
        }

        if (ChromaFeatureConfig.darkOakLeaves) {
            registerBlockColor(event, ChromaBlocks.CHROMATIC_DARK_OAK_LEAVES);
            registerBlockColor(event, ChromaBlocks.EMISSIVE_DARK_OAK_LEAVES);
        }

        if (ChromaFeatureConfig.vines) {
            registerBlockColor(event, ChromaBlocks.CHROMATIC_VINE);
            registerBlockColor(event, ChromaBlocks.EMISSIVE_VINE);
        }

        if (ChromaClientConfig.BLOCKS.snowLayers) {
            event.getBlockColors().registerBlockColorHandler((state, world, pos, tint) -> {
                if (world != null && pos != null && pos.down().getY() >= 0) {
                    final IBlockState below = world.getBlockState(pos.down());
                    if (below.getBlock() instanceof ChromaticGrassBlock) {
                        return below.getValue(ChromaColor.PROPERTY).getColorValue();
                    } else if (below.getBlock() == Blocks.SNOW_LAYER) {
                        return event.getBlockColors().colorMultiplier(below, world, pos.down(), tint);
                    }
                }
                return 0xFFFFFF;
            }, Blocks.SNOW_LAYER);
        }

        if (ChromaClientConfig.BLOCKS.grassPlants) {
            event.getBlockColors().registerBlockColorHandler((state, world, pos, tint) -> {
                if (world != null && pos != null) {
                    final EnumPlantType variant = state.getValue(BlockDoublePlant.VARIANT);
                    BlockPos target = pos;
                    if (variant == EnumPlantType.GRASS || variant == EnumPlantType.FERN) {
                        if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) {
                            target = target.down();
                        }
                    }
                    if (target.down().getY() >= 0) {
                        final IBlockState below = world.getBlockState(target.down());
                        if (below.getBlock() instanceof ChromaticGrassBlock) {
                            return below.getValue(ChromaColor.PROPERTY).getColorValue();
                        }
                    }
                    return BiomeColorHelper.getGrassColorAtPos(world, target);
                }
                return 0xFFFFFF;
            }, Blocks.DOUBLE_PLANT);

            event.getBlockColors().registerBlockColorHandler((state, world, pos, tint) -> {
                if (world != null && pos != null) {
                    if (pos.down().getY() >= 0) {
                        final IBlockState below = world.getBlockState(pos.down());
                        if (below.getBlock() instanceof ChromaticGrassBlock) {
                            return below.getValue(ChromaColor.PROPERTY).getColorValue();
                        }
                    }
                    return BiomeColorHelper.getGrassColorAtPos(world, pos);
                }
                if (state.getValue(BlockTallGrass.TYPE) != EnumType.DEAD_BUSH) {
                    return ColorizerGrass.getGrassColor(0.5D, 1.0D);
                } else return 0xFFFFFF;
            }, Blocks.TALLGRASS);
        }
    }

    @SubscribeEvent
    static void onItemColorRegistry(ColorHandlerEvent.Item event) {
        if (ChromaFeatureConfig.grassBlocks) {
            registerItemColor(event, ChromaItems.CHROMATIC_GRASS);
        }

        if (ChromaFeatureConfig.oakLeaves) {
            registerItemColor(event, ChromaItems.CHROMATIC_OAK_LEAVES);
        }

        if (ChromaFeatureConfig.spruceLeaves) {
            registerItemColor(event, ChromaItems.CHROMATIC_SPRUCE_LEAVES);
        }

        if (ChromaFeatureConfig.birchLeaves) {
            registerItemColor(event, ChromaItems.CHROMATIC_BIRCH_LEAVES);
        }

        if (ChromaFeatureConfig.jungleLeaves) {
            registerItemColor(event, ChromaItems.CHROMATIC_JUNGLE_LEAVES);
        }

        if (ChromaFeatureConfig.acaciaLeaves) {
            registerItemColor(event, ChromaItems.CHROMATIC_ACACIA_LEAVES);
        }

        if (ChromaFeatureConfig.darkOakLeaves) {
            registerItemColor(event, ChromaItems.CHROMATIC_DARK_OAK_LEAVES);
        }

        if (ChromaFeatureConfig.vines) {
            registerItemColor(event, ChromaItems.CHROMATIC_VINE);
        }
    }

    @SubscribeEvent
    static void onModelBake(ModelBakeEvent event) {
        if (ChromaClientConfig.BLOCKS.snowLayers) {
            final Block block = Blocks.SNOW_LAYER;
            checkState(block != null, "net.minecraft.init.Blocks.SNOW_LAYER is null");
            checkState(block != Blocks.AIR, "net.minecraft.init.Blocks.SNOW_LAYER is empty");
            final ResourceLocation name = block.getRegistryName();
            checkState(name != null, "net.minecraft.init.Blocks.SNOW_LAYER is missing a registry name");
            for (final int value : BlockSnow.LAYERS.getAllowedValues()) {
                final IBlockState state = block.getDefaultState().withProperty(BlockSnow.LAYERS, value);
                final String variant = BlockSnow.LAYERS.getName() + "=" + BlockSnow.LAYERS.getName(value);
                final ModelResourceLocation mrl = new ModelResourceLocation(name, variant);
                final IBakedModel model = event.getModelManager().getModel(mrl);
                for (final BakedQuad quad : model.getQuads(state, null, 0)) {
                    if (tintQuadOrError(model, quad)) {
                        return;
                    }
                }
                for (final EnumFacing side : EnumFacing.VALUES) {
                    for (BakedQuad quad : model.getQuads(state, side, 0)) {
                        if (tintQuadOrError(model, quad)) {
                            return;
                        }
                    }
                }

            }
        }
    }

    private static void registerModels(Item item) {
        checkState(item != null && item != Items.AIR, "Item instance cannot be null or empty");
        final ResourceLocation name = item.getRegistryName();
        checkState(name != null, "Item registry name cannot be null");
        for (final ChromaColor color : ChromaColor.VALUES) {
            final StringBuilder variant = new StringBuilder()
                .append("color=").append(color.getName());
            if (item instanceof ItemBlock) {
                final Block block = ((ItemBlock) item).getBlock();
                if (block instanceof ChromaticGrassBlock) {
                    variant.append(",snowy=false");
                }
            }
            final ModelResourceLocation mrl = new ModelResourceLocation(name, variant.toString());
            ModelLoader.setCustomModelResourceLocation(item, color.ordinal(), mrl);
        }
    }

    private static void registerStateMapper(Block block) {
        checkState(block != null && block != Blocks.AIR, "Block instance cannot be null or empty");
        final IStateMapper mapper = new ChromaMapper(block);
        ModelLoader.setCustomStateMapper(block, mapper);
    }

    private static void registerBlockColor(ColorHandlerEvent.Block event, Block block) {
        checkState(block != null && block != Blocks.AIR, "Block cannot be null or empty");
        final BlockColors blockColors = event.getBlockColors();
        checkState(blockColors != null, "BlockColors instance cannot be null");
        blockColors.registerBlockColorHandler(ChromaColorizer.INSTANCE, block);
    }

    private static void registerItemColor(ColorHandlerEvent.Item event, Item item) {
        checkState(item != null && item != Items.AIR, "Item cannot be null or empty");
        final ItemColors itemColors = event.getItemColors();
        checkState(itemColors != null, "ItemColors instance cannot be null");
        itemColors.registerItemColorHandler(ChromaColorizer.INSTANCE, item);
    }

    private static boolean tintQuadOrError(IBakedModel model, BakedQuad quad) {
        try {
            final String fieldName = ChromaClientRegistry.getTintIndexFieldName();
            checkState(fieldName != null && !fieldName.isEmpty(), "String 'fieldName' cannot be null or empty");
            ReflectionHelper.setPrivateValue(BakedQuad.class, quad, 0, fieldName);
            return false;
        } catch (Exception exception) {
            ChromaticFoliage.LOGGER.error("Failed to inject tint index for model <{}>", model.toString());
            ChromaticFoliage.LOGGER.debug("Tint index injection failed for the following reason:", exception);
            return true;
        }
    }

    private static String getTintIndexFieldName() {
        if (tintIndexFieldName == null) {
            try {
                FMLDeobfuscatingRemapper remapper = FMLDeobfuscatingRemapper.INSTANCE;
                final String clazz = remapper.unmap("net/minecraft/client/renderer/block/model/BakedQuad");
                tintIndexFieldName = remapper.mapFieldName(clazz, "field_178213_b", null);
            } catch (Exception exception) {
                throw new RuntimeException("Failed to map obfuscated field name `field_178213_b`", exception);
            }
        }
        return tintIndexFieldName;
    }
}
