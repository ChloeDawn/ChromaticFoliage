package net.sleeplessdev.chromaticfoliage.registry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.block.ChromaticGrassBlock;
import net.sleeplessdev.chromaticfoliage.block.ChromaticLeavesBlock;
import net.sleeplessdev.chromaticfoliage.block.ChromaticVineBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveGrassBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveLeavesBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveVineBlock;
import net.sleeplessdev.chromaticfoliage.config.ChromaFeatureConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import net.sleeplessdev.chromaticfoliage.item.ChromaBlockItem;

@Mod.EventBusSubscriber(modid = ChromaticFoliage.ID)
public final class ChromaObjectRegistry {

    private ChromaObjectRegistry() {}

    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        if (ChromaFeatureConfig.grassBlocks) {
            event.getRegistry().register(new ChromaticGrassBlock()
                    .setRegistryName("chromatic_grass")
            );
            event.getRegistry().register(new EmissiveGrassBlock()
                    .setRegistryName("emissive_grass")
            );
        }
        if (ChromaFeatureConfig.oakLeaves) {
            event.getRegistry().register(new ChromaticLeavesBlock(EnumType.OAK)
                    .setRegistryName("chromatic_oak_leaves")
            );
            event.getRegistry().register(new EmissiveLeavesBlock(EnumType.OAK)
                    .setRegistryName("emissive_oak_leaves")
            );
        }
        if (ChromaFeatureConfig.spruceLeaves) {
            event.getRegistry().register(new ChromaticLeavesBlock(EnumType.SPRUCE)
                    .setRegistryName("chromatic_spruce_leaves")
            );
            event.getRegistry().register(new EmissiveLeavesBlock(EnumType.SPRUCE)
                    .setRegistryName("emissive_spruce_leaves")
            );
        }
        if (ChromaFeatureConfig.birchLeaves) {
            event.getRegistry().register(new ChromaticLeavesBlock(EnumType.BIRCH)
                    .setRegistryName("chromatic_birch_leaves")
            );
            event.getRegistry().register(new EmissiveLeavesBlock(EnumType.BIRCH)
                    .setRegistryName("emissive_birch_leaves")
            );
        }
        if (ChromaFeatureConfig.jungleLeaves) {
            event.getRegistry().register(new ChromaticLeavesBlock(EnumType.JUNGLE)
                    .setRegistryName("chromatic_jungle_leaves")
            );
            event.getRegistry().register(new EmissiveLeavesBlock(EnumType.JUNGLE)
                    .setRegistryName("emissive_jungle_leaves")
            );
        }
        if (ChromaFeatureConfig.acaciaLeaves) {
            event.getRegistry().register(new ChromaticLeavesBlock(EnumType.ACACIA)
                    .setRegistryName("chromatic_acacia_leaves")
            );
            event.getRegistry().register(new EmissiveLeavesBlock(EnumType.ACACIA)
                    .setRegistryName("emissive_acacia_leaves")
            );
        }
        if (ChromaFeatureConfig.darkOakLeaves) {
            event.getRegistry().register(new ChromaticLeavesBlock(EnumType.DARK_OAK)
                    .setRegistryName("chromatic_dark_oak_leaves")
            );
            event.getRegistry().register(new EmissiveLeavesBlock(EnumType.DARK_OAK)
                    .setRegistryName("emissive_dark_oak_leaves")
            );
        }
        if (ChromaFeatureConfig.vines) {
            GameRegistry.registerTileEntity(
                    ChromaticVineBlock.VineBlockEntity.class,
                    ChromaticFoliage.ID + ":chromatic_vine"
            );
            event.getRegistry().register(new ChromaticVineBlock()
                    .setRegistryName("chromatic_vine"));
            event.getRegistry().register(new EmissiveVineBlock()
                    .setRegistryName("emissive_vine"));
        }
    }

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event) {
        if (ChromaFeatureConfig.grassBlocks) {
            event.getRegistry().register(new ChromaBlockItem(ChromaBlocks.CHROMATIC_GRASS)
                    .setRegistryName("chromatic_grass")
            );
        }
        if (ChromaFeatureConfig.oakLeaves) {
            event.getRegistry().register(new ChromaBlockItem(ChromaBlocks.CHROMATIC_OAK_LEAVES)
                    .setRegistryName("chromatic_oak_leaves")
            );
        }
        if (ChromaFeatureConfig.spruceLeaves) {
            event.getRegistry().register(new ChromaBlockItem(ChromaBlocks.CHROMATIC_SPRUCE_LEAVES)
                    .setRegistryName("chromatic_spruce_leaves")
            );
        }
        if (ChromaFeatureConfig.birchLeaves) {
            event.getRegistry().register(new ChromaBlockItem(ChromaBlocks.CHROMATIC_BIRCH_LEAVES)
                    .setRegistryName("chromatic_birch_leaves")
            );
        }
        if (ChromaFeatureConfig.jungleLeaves) {
            event.getRegistry().register(new ChromaBlockItem(ChromaBlocks.CHROMATIC_JUNGLE_LEAVES)
                    .setRegistryName("chromatic_jungle_leaves")
            );
        }
        if (ChromaFeatureConfig.acaciaLeaves) {
            event.getRegistry().register(new ChromaBlockItem(ChromaBlocks.CHROMATIC_ACACIA_LEAVES)
                    .setRegistryName("chromatic_acacia_leaves")
            );
        }
        if (ChromaFeatureConfig.darkOakLeaves) {
            event.getRegistry().register(new ChromaBlockItem(ChromaBlocks.CHROMATIC_DARK_OAK_LEAVES)
                    .setRegistryName("chromatic_dark_oak_leaves")
            );
        }
        if (ChromaFeatureConfig.vines) {
            event.getRegistry().register(new ChromaBlockItem(ChromaBlocks.CHROMATIC_VINE)
                    .setRegistryName("chromatic_vine"));
        }
    }

}
