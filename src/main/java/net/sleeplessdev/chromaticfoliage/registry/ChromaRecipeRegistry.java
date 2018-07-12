package net.sleeplessdev.chromaticfoliage.registry;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.config.ChromaGeneralConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;
import net.sleeplessdev.chromaticfoliage.data.ChromaItems;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.minecraftforge.oredict.OreDictionary.registerOre;

@EventBusSubscriber(modid = ChromaticFoliage.ID)
public final class ChromaRecipeRegistry {
    private static final Converter<String, String> CAMEL_CONVERTER =
        CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL);

    private ChromaRecipeRegistry() {
        throw new UnsupportedOperationException("Cannot instantiate " + this.getClass());
    }

    @SubscribeEvent
    static void onRecipeRegistry(RegistryEvent.Register<IRecipe> event) {
        registerOre("treeLeavesOak", new ItemStack(Blocks.LEAVES, 1, 0));
        registerOre("treeLeavesSpruce", new ItemStack(Blocks.LEAVES, 1, 1));
        registerOre("treeLeavesBirch", new ItemStack(Blocks.LEAVES, 1, 2));
        registerOre("treeLeavesJungle", new ItemStack(Blocks.LEAVES, 1, 3));
        registerOre("treeLeavesAcacia", new ItemStack(Blocks.LEAVES2, 1, 0));
        registerOre("treeLeavesDarkOak", new ItemStack(Blocks.LEAVES2, 1, 1));

        registerOreNames(ChromaItems.CHROMATIC_GRASS, "grass");
        registerOreNames(ChromaItems.CHROMATIC_OAK_LEAVES, "treeLeaves", "treeLeavesOak");
        registerOreNames(ChromaItems.CHROMATIC_SPRUCE_LEAVES, "treeLeaves", "treeLeavesSpruce");
        registerOreNames(ChromaItems.CHROMATIC_BIRCH_LEAVES, "treeLeaves", "treeLeavesBirch");
        registerOreNames(ChromaItems.CHROMATIC_JUNGLE_LEAVES, "treeLeaves", "treeLeavesJungle");
        registerOreNames(ChromaItems.CHROMATIC_ACACIA_LEAVES, "treeLeaves", "treeLeavesAcacia");
        registerOreNames(ChromaItems.CHROMATIC_DARK_OAK_LEAVES, "treeLeaves", "treeLeavesDarkOak");
        registerOreNames(ChromaItems.CHROMATIC_VINE, "vine");

        registerDyeRecipes(ChromaItems.CHROMATIC_GRASS, new ItemStack(Blocks.GRASS));
        registerDyeRecipes(ChromaItems.CHROMATIC_OAK_LEAVES, new ItemStack(Blocks.LEAVES, 1, 0));
        registerDyeRecipes(ChromaItems.CHROMATIC_SPRUCE_LEAVES, new ItemStack(Blocks.LEAVES, 1, 1));
        registerDyeRecipes(ChromaItems.CHROMATIC_BIRCH_LEAVES, new ItemStack(Blocks.LEAVES, 1, 2));
        registerDyeRecipes(ChromaItems.CHROMATIC_JUNGLE_LEAVES, new ItemStack(Blocks.LEAVES, 1, 3));
        registerDyeRecipes(ChromaItems.CHROMATIC_ACACIA_LEAVES, new ItemStack(Blocks.LEAVES2, 1, 0));
        registerDyeRecipes(ChromaItems.CHROMATIC_DARK_OAK_LEAVES, new ItemStack(Blocks.LEAVES2, 1, 1));
        registerDyeRecipes(ChromaItems.CHROMATIC_VINE, new ItemStack(Blocks.VINE));

        if (ChromaGeneralConfig.chromaRecoloring) {
            registerReDyeRecipes(ChromaItems.CHROMATIC_GRASS);
            registerReDyeRecipes(ChromaItems.CHROMATIC_OAK_LEAVES);
            registerReDyeRecipes(ChromaItems.CHROMATIC_SPRUCE_LEAVES);
            registerReDyeRecipes(ChromaItems.CHROMATIC_BIRCH_LEAVES);
            registerReDyeRecipes(ChromaItems.CHROMATIC_JUNGLE_LEAVES);
            registerReDyeRecipes(ChromaItems.CHROMATIC_ACACIA_LEAVES);
            registerReDyeRecipes(ChromaItems.CHROMATIC_DARK_OAK_LEAVES);
            registerReDyeRecipes(ChromaItems.CHROMATIC_VINE);
        }
    }

    private static void registerOreNames(Item item, String... prefixes) {
        final ItemStack wildcard = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
        for (final String prefix : prefixes) {
            registerOre(prefix, wildcard);
            registerOre(prefix + "Colored", wildcard);
            for (final ChromaColor color : ChromaColor.VALUES) {
                final ItemStack stack = new ItemStack(item, 1, color.ordinal());
                final String suffix = CAMEL_CONVERTER.convert(color.getName());
                registerOre(prefix + suffix, stack);
            }
        }
    }

    private static void registerDyeRecipes(Item output, ItemStack input) {
        final String prefix = output.getRegistryName() + "_";
        for (final ChromaColor color : ChromaColor.VALUES) {
            final ResourceLocation name = new ResourceLocation(prefix + color.getName());
            GameRegistry.addShapelessRecipe(name, null,
                new ItemStack(output, 1, color.ordinal()),
                Ingredient.fromStacks(input),
                new OreIngredient(color.getOreName())
            );
        }
    }

    private static void registerReDyeRecipes(Item item) {
        final String prefix = item.getRegistryName() + "_recolor_";
        final List<ItemStack> stacks = Stream.of(ChromaColor.VALUES)
            .map(color -> new ItemStack(item, 1, color.ordinal()))
            .collect(Collectors.toList());
        for (final ChromaColor color : ChromaColor.VALUES) {
            final ResourceLocation name = new ResourceLocation(prefix + color.getName());
            final List<ItemStack> inputs = Lists.newArrayList(stacks);
            GameRegistry.addShapelessRecipe(name, null,
                inputs.remove(color.ordinal()),
                Ingredient.fromStacks(inputs.toArray(new ItemStack[0])),
                new OreIngredient(color.getOreName())
            );
        }
    }
}
