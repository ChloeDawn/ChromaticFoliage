package net.sleeplessdev.chromaticfoliage.registry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.block.ChromaticGrassBlock;
import net.sleeplessdev.chromaticfoliage.block.ChromaticLeavesBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveGrassBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveLeavesBlock;
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
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("chromatic_grass")
            );
            event.getRegistry().register(new EmissiveGrassBlock()
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("emissive_grass")
            );
        }
        if (ChromaFeatureConfig.oakLeaves) {
            event.getRegistry().register(new ChromaticLeavesBlock(BlockPlanks.EnumType.OAK)
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("chromatic_oak_leaves")
            );
            event.getRegistry().register(new EmissiveLeavesBlock(BlockPlanks.EnumType.OAK)
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("emissive_oak_leaves")
            );
        }
        if (ChromaFeatureConfig.spruceLeaves) {
            event.getRegistry().register(new ChromaticLeavesBlock(BlockPlanks.EnumType.SPRUCE)
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("chromatic_spruce_leaves")
            );
            event.getRegistry().register(new EmissiveLeavesBlock(BlockPlanks.EnumType.SPRUCE)
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("emissive_spruce_leaves")
            );
        }
        if (ChromaFeatureConfig.birchLeaves) {
            event.getRegistry().register(new ChromaticLeavesBlock(BlockPlanks.EnumType.BIRCH)
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("chromatic_birch_leaves")
            );
            event.getRegistry().register(new EmissiveLeavesBlock(BlockPlanks.EnumType.BIRCH)
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("emissive_birch_leaves")
            );
        }
        if (ChromaFeatureConfig.jungleLeaves) {
            event.getRegistry().register(new ChromaticLeavesBlock(BlockPlanks.EnumType.JUNGLE)
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("chromatic_jungle_leaves")
            );
            event.getRegistry().register(new EmissiveLeavesBlock(BlockPlanks.EnumType.JUNGLE)
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("emissive_jungle_leaves")
            );
        }
        if (ChromaFeatureConfig.acaciaLeaves) {
            event.getRegistry().register(new ChromaticLeavesBlock(BlockPlanks.EnumType.ACACIA)
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("chromatic_acacia_leaves")
            );
            event.getRegistry().register(new EmissiveLeavesBlock(BlockPlanks.EnumType.ACACIA)
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("emissive_acacia_leaves")
            );
        }
        if (ChromaFeatureConfig.darkOakLeaves) {
            event.getRegistry().register(new ChromaticLeavesBlock(BlockPlanks.EnumType.DARK_OAK)
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("chromatic_dark_oak_leaves")
            );
            event.getRegistry().register(new EmissiveLeavesBlock(BlockPlanks.EnumType.DARK_OAK)
                    .setCreativeTab(ChromaticFoliage.TAB).setRegistryName("emissive_dark_oak_leaves")
            );
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
    }

}
