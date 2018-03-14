package net.sleeplessdev.chromaticfoliage.registry;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.config.ChromaGeneralConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;
import net.sleeplessdev.chromaticfoliage.data.ChromaItems;

@Mod.EventBusSubscriber(modid = ChromaticFoliage.ID)
public final class ChromaRecipeRegistry {

    private ChromaRecipeRegistry() {} // TODO Convert generated recipes to JSON before production

    @SubscribeEvent
    public static void onRecipeRegistry(RegistryEvent.Register<IRecipe> event) {
        OreDictionary.registerOre("treeLeavesOak", new ItemStack(Blocks.LEAVES, 1, 0));
        OreDictionary.registerOre("treeLeavesSpruce", new ItemStack(Blocks.LEAVES, 1, 1));
        OreDictionary.registerOre("treeLeavesBirch", new ItemStack(Blocks.LEAVES, 1, 2));
        OreDictionary.registerOre("treeLeavesJungle", new ItemStack(Blocks.LEAVES, 1, 3));
        OreDictionary.registerOre("treeLeavesAcacia", new ItemStack(Blocks.LEAVES2, 1, 0));
        OreDictionary.registerOre("treeLeavesDarkOak", new ItemStack(Blocks.LEAVES2, 1, 1));

        registerOreEntries(ChromaItems.CHROMATIC_GRASS, "grass", "grassColored");
        registerOreEntries(ChromaItems.CHROMATIC_OAK_LEAVES, "treeLeaves", "treeLeavesOak");
        registerOreEntries(ChromaItems.CHROMATIC_SPRUCE_LEAVES, "treeLeaves", "treeLeavesSpruce");
        registerOreEntries(ChromaItems.CHROMATIC_BIRCH_LEAVES, "treeLeaves", "treeLeavesBirch");
        registerOreEntries(ChromaItems.CHROMATIC_JUNGLE_LEAVES, "treeLeaves", "treeLeavesJungle");
        registerOreEntries(ChromaItems.CHROMATIC_ACACIA_LEAVES, "treeLeaves", "treeLeavesAcacia");
        registerOreEntries(ChromaItems.CHROMATIC_DARK_OAK_LEAVES, "treeLeaves", "treeLeavesDarkOak");

        registerDyeRecipes(ChromaItems.CHROMATIC_GRASS, new ItemStack(Blocks.GRASS));
        registerDyeRecipes(ChromaItems.CHROMATIC_OAK_LEAVES, new ItemStack(Blocks.LEAVES, 1, 0));
        registerDyeRecipes(ChromaItems.CHROMATIC_SPRUCE_LEAVES, new ItemStack(Blocks.LEAVES, 1, 1));
        registerDyeRecipes(ChromaItems.CHROMATIC_BIRCH_LEAVES, new ItemStack(Blocks.LEAVES, 1, 2));
        registerDyeRecipes(ChromaItems.CHROMATIC_JUNGLE_LEAVES, new ItemStack(Blocks.LEAVES, 1, 3));
        registerDyeRecipes(ChromaItems.CHROMATIC_ACACIA_LEAVES, new ItemStack(Blocks.LEAVES2, 1, 0));
        registerDyeRecipes(ChromaItems.CHROMATIC_DARK_OAK_LEAVES, new ItemStack(Blocks.LEAVES2, 1, 1));

        if (ChromaGeneralConfig.chromaRecoloring) {
            registerReDyeRecipes(ChromaItems.CHROMATIC_GRASS, "grass");
            registerReDyeRecipes(ChromaItems.CHROMATIC_OAK_LEAVES, "treeLeavesOak");
            registerReDyeRecipes(ChromaItems.CHROMATIC_SPRUCE_LEAVES, "treeLeavesSpruce");
            registerReDyeRecipes(ChromaItems.CHROMATIC_BIRCH_LEAVES, "treeLeavesBirch");
            registerReDyeRecipes(ChromaItems.CHROMATIC_JUNGLE_LEAVES, "treeLeavesJungle");
            registerReDyeRecipes(ChromaItems.CHROMATIC_ACACIA_LEAVES, "treeLeavesAcacia");
            registerReDyeRecipes(ChromaItems.CHROMATIC_DARK_OAK_LEAVES, "treeLeavesDarkOak");
        }
    }

    private static void registerOreEntries(Item item, String... prefixes) {
        Converter<String, String> converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL);
        ItemStack wildcard = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
        for (String prefix : prefixes) {
            OreDictionary.registerOre(prefix, wildcard);
            OreDictionary.registerOre(prefix + "Colored", wildcard);
            for (ChromaColors color : ChromaColors.VALUES) {
                ItemStack stack = new ItemStack(item, 1, color.ordinal());
                String suffix = converter.convert(color.getName());
                OreDictionary.registerOre(prefix + suffix, stack);
            }
        }
    }

    private static void registerDyeRecipes(Item output, ItemStack input) {
        String prefix = output.getRegistryName() + "_";
        for (ChromaColors color : ChromaColors.VALUES) {
            ResourceLocation name = new ResourceLocation(prefix + color.getName());
            GameRegistry.addShapelessRecipe(name, null, new ItemStack(output, 1, color.ordinal()),
                    Ingredient.fromStacks(input), new OreIngredient(color.getOreName())
            );
        }
    }

    private static void registerReDyeRecipes(Item output, String input) {
        String prefix = output.getRegistryName() + "_recolor_";
        for (ChromaColors color : ChromaColors.VALUES) {
            ResourceLocation name = new ResourceLocation(prefix + color.getName());
            GameRegistry.addShapelessRecipe(name, null, new ItemStack(output, 1, color.ordinal()),
                    new OreIngredient(input), new OreIngredient(color.getOreName())
            );
        }
    }

}
