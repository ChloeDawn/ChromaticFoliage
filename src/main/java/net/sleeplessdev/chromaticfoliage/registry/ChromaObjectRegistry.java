package net.sleeplessdev.chromaticfoliage.registry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.block.ChromaticGrassBlock;
import net.sleeplessdev.chromaticfoliage.block.ChromaticLeavesBlock;
import net.sleeplessdev.chromaticfoliage.block.ChromaticVineBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveGrassBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveLeavesBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveVineBlock;
import net.sleeplessdev.chromaticfoliage.block.entity.ChromaBlockEntity;
import net.sleeplessdev.chromaticfoliage.config.ChromaFeatureConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import net.sleeplessdev.chromaticfoliage.item.ChromaBlockItem;

import static com.google.common.base.Preconditions.checkState;

@EventBusSubscriber(modid = ChromaticFoliage.ID)
public final class ChromaObjectRegistry {
    private ChromaObjectRegistry() {
        throw new UnsupportedOperationException("Cannot instantiate " + this.getClass());
    }

    @SubscribeEvent
    static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        final IForgeRegistry<Block> registry = event.getRegistry();

        if (ChromaFeatureConfig.grassBlocks) {
            registerBlock(registry, new ChromaticGrassBlock(), "chromatic_grass");
            registerBlock(registry, new EmissiveGrassBlock(), "emissive_grass");
        }

        if (ChromaFeatureConfig.oakLeaves) {
            registerBlock(registry, new ChromaticLeavesBlock(EnumType.OAK), "chromatic_oak_leaves");
            registerBlock(registry, new EmissiveLeavesBlock(EnumType.OAK), "emissive_oak_leaves");
        }

        if (ChromaFeatureConfig.spruceLeaves) {
            registerBlock(registry, new ChromaticLeavesBlock(EnumType.SPRUCE), "chromatic_spruce_leaves");
            registerBlock(registry, new EmissiveLeavesBlock(EnumType.SPRUCE), "emissive_spruce_leaves");
        }

        if (ChromaFeatureConfig.birchLeaves) {
            registerBlock(registry, new ChromaticLeavesBlock(EnumType.BIRCH), "chromatic_birch_leaves");
            registerBlock(registry, new EmissiveLeavesBlock(EnumType.BIRCH), "emissive_birch_leaves");
        }

        if (ChromaFeatureConfig.jungleLeaves) {
            registerBlock(registry, new ChromaticLeavesBlock(EnumType.JUNGLE), "chromatic_jungle_leaves");
            registerBlock(registry, new EmissiveLeavesBlock(EnumType.JUNGLE), "emissive_jungle_leaves");
        }

        if (ChromaFeatureConfig.acaciaLeaves) {
            registerBlock(registry, new ChromaticLeavesBlock(EnumType.ACACIA), "chromatic_acacia_leaves");
            registerBlock(registry, new EmissiveLeavesBlock(EnumType.ACACIA), "emissive_acacia_leaves");
        }

        if (ChromaFeatureConfig.darkOakLeaves) {
            registerBlock(registry, new ChromaticLeavesBlock(EnumType.DARK_OAK), "chromatic_dark_oak_leaves");
            registerBlock(registry, new EmissiveLeavesBlock(EnumType.DARK_OAK), "emissive_dark_oak_leaves");
        }

        if (ChromaFeatureConfig.vines) {
            registerBlock(registry, new ChromaticVineBlock(), "chromatic_vine");
            registerBlock(registry, new EmissiveVineBlock(), "emissive_vine");

            registerBlockEntity(ChromaBlockEntity.class, "block_entity");
        }
    }

    @SubscribeEvent
    static void onItemRegistry(RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();

        if (ChromaFeatureConfig.grassBlocks) {
            registerBlockItem(registry, ChromaBlocks.CHROMATIC_GRASS, "chromatic_grass");
        }

        if (ChromaFeatureConfig.oakLeaves) {
            registerBlockItem(registry, ChromaBlocks.CHROMATIC_OAK_LEAVES, "chromatic_oak_leaves");
        }

        if (ChromaFeatureConfig.spruceLeaves) {
            registerBlockItem(registry, ChromaBlocks.CHROMATIC_SPRUCE_LEAVES, "chromatic_spruce_leaves");
        }

        if (ChromaFeatureConfig.birchLeaves) {
            registerBlockItem(registry, ChromaBlocks.CHROMATIC_BIRCH_LEAVES, "chromatic_birch_leaves");
        }

        if (ChromaFeatureConfig.jungleLeaves) {
            registerBlockItem(registry, ChromaBlocks.CHROMATIC_JUNGLE_LEAVES, "chromatic_jungle_leaves");
        }

        if (ChromaFeatureConfig.acaciaLeaves) {
            registerBlockItem(registry, ChromaBlocks.CHROMATIC_ACACIA_LEAVES, "chromatic_acacia_leaves");
        }

        if (ChromaFeatureConfig.darkOakLeaves) {
            registerBlockItem(registry, ChromaBlocks.CHROMATIC_DARK_OAK_LEAVES, "chromatic_dark_oak_leaves");
        }

        if (ChromaFeatureConfig.vines) {
            registerBlockItem(registry, ChromaBlocks.CHROMATIC_VINE, "chromatic_vine");
        }
    }

    private static void registerBlockEntity(Class<? extends TileEntity> clazz, String key) {
        checkState(key != null && !key.isEmpty(), "Key cannot be null or empty");
        GameRegistry.registerTileEntity(clazz, new ResourceLocation(ChromaticFoliage.ID, key));
    }

    private static void registerBlock(IForgeRegistry<Block> registry, Block block, String name) {
        checkState(block != null && block != Blocks.AIR, "Block cannot be null or empty");
        checkState(name != null && !name.isEmpty(), "Name cannot be null or empty");
        checkState(block.getRegistryName() == null, "Block cannot have existing registry name");
        block.setRegistryName(ChromaticFoliage.ID, name);
        block.setUnlocalizedName(ChromaticFoliage.ID + "." + name);
        registry.register(block);
    }

    private static void registerBlockItem(IForgeRegistry<Item> registry, Block block, String name) {
        checkState(block != null && block != Blocks.AIR, "Block cannot be null or empty");
        checkState(name != null && !name.isEmpty(), "Name cannot be null or empty");
        checkState(block.getRegistryName() != null, "Block requires a registry name");
        final Item item = new ChromaBlockItem(block);
        item.setRegistryName(ChromaticFoliage.ID, name);
        item.setUnlocalizedName(ChromaticFoliage.ID + "." + name);
        registry.register(item);
    }
}
