package io.github.insomniakitten.chromaticfoliage.common.init;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.base.ChromaticColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static net.minecraftforge.oredict.OreDictionary.registerOre;

// todo json recipes

@ObjectHolder(ChromaticFoliage.MOD_ID)
@EventBusSubscriber(modid = ChromaticFoliage.MOD_ID)
final class ChromaticRecipes {
  private static final Logger LOGGER = ChromaticFoliage.getLogger("Recipes");

  private static final Converter<String, String> TO_ORE_SUFFIX =
    CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.UPPER_CAMEL);

  private ChromaticRecipes() {
    throw new UnsupportedOperationException();
  }

  @SubscribeEvent
  static void registerAll(final RegistryEvent.Register<IRecipe> event) {
    final ResourceLocation name = event.getName();
    LOGGER.debug("Beginning registration to {}", name);
    registerAll(event.getRegistry());
    LOGGER.debug("Completed registration to {}", name);
  }

  private static void registerAll(final IForgeRegistry<IRecipe> registry) {
    registerOre("treeLeavesOak", new ItemStack(Blocks.LEAVES, 1, 0));
    registerOre("treeLeavesSpruce", new ItemStack(Blocks.LEAVES, 1, 1));
    registerOre("treeLeavesBirch", new ItemStack(Blocks.LEAVES, 1, 2));
    registerOre("treeLeavesJungle", new ItemStack(Blocks.LEAVES, 1, 3));
    registerOre("treeLeavesAcacia", new ItemStack(Blocks.LEAVES2, 1, 0));
    registerOre("treeLeavesDarkOak", new ItemStack(Blocks.LEAVES2, 1, 1));

    registerOreNames(ChromaticItems.chromaticGrass(), "grass");
    registerOreNames(ChromaticItems.chromaticOakLeaves(), "treeLeaves", "treeLeavesOak");
    registerOreNames(ChromaticItems.chromaticSpruceLeaves(), "treeLeaves", "treeLeavesSpruce");
    registerOreNames(ChromaticItems.chromaticBirchLeaves(), "treeLeaves", "treeLeavesBirch");
    registerOreNames(ChromaticItems.chromaticJungleLeaves(), "treeLeaves", "treeLeavesJungle");
    registerOreNames(ChromaticItems.chromaticAcaciaLeaves(), "treeLeaves", "treeLeavesAcacia");
    registerOreNames(ChromaticItems.chromaticDarkOakLeaves(), "treeLeaves", "treeLeavesDarkOak");
    registerOreNames(ChromaticItems.chromaticVine(), "vine");

    registerDyeRecipes(registry, ChromaticItems.chromaticGrass(), new ItemStack(Blocks.GRASS));
    registerDyeRecipes(registry, ChromaticItems.chromaticOakLeaves(), new ItemStack(Blocks.LEAVES, 1, 0));
    registerDyeRecipes(registry, ChromaticItems.chromaticSpruceLeaves(), new ItemStack(Blocks.LEAVES, 1, 1));
    registerDyeRecipes(registry, ChromaticItems.chromaticBirchLeaves(), new ItemStack(Blocks.LEAVES, 1, 2));
    registerDyeRecipes(registry, ChromaticItems.chromaticJungleLeaves(), new ItemStack(Blocks.LEAVES, 1, 3));
    registerDyeRecipes(registry, ChromaticItems.chromaticAcaciaLeaves(), new ItemStack(Blocks.LEAVES2, 1, 0));
    registerDyeRecipes(registry, ChromaticItems.chromaticDarkOakLeaves(), new ItemStack(Blocks.LEAVES2, 1, 1));
    registerDyeRecipes(registry, ChromaticItems.chromaticVine(), new ItemStack(Blocks.VINE));

    if (ChromaticFoliage.getGeneralConfig().isChromaRecoloringEnabled()) {
      registerReDyeRecipes(registry, ChromaticItems.chromaticGrass());
      registerReDyeRecipes(registry, ChromaticItems.chromaticOakLeaves());
      registerReDyeRecipes(registry, ChromaticItems.chromaticSpruceLeaves());
      registerReDyeRecipes(registry, ChromaticItems.chromaticBirchLeaves());
      registerReDyeRecipes(registry, ChromaticItems.chromaticJungleLeaves());
      registerReDyeRecipes(registry, ChromaticItems.chromaticAcaciaLeaves());
      registerReDyeRecipes(registry, ChromaticItems.chromaticDarkOakLeaves());
      registerReDyeRecipes(registry, ChromaticItems.chromaticVine());
    }
  }

  private static void registerOreNames(final Item item, final String... prefixes) {
    final ItemStack wildcard = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
    for (final String prefix : prefixes) {
      registerOre(prefix, wildcard);
      registerOre(prefix + "Colored", wildcard);
      for (final ChromaticColor color : ChromaticColor.colors()) {
        @Nullable final String suffix = TO_ORE_SUFFIX.convert(color.getName());
        checkState(suffix != null, "Failed to create ore suffix for %s", color);
        registerOre(prefix + suffix, new ItemStack(item, 1, color.ordinal()));
      }
    }
  }

  private static void registerDyeRecipes(final IForgeRegistry<IRecipe> registry, final Item out, final ItemStack in) {
    final String prefix = out.getRegistryName() + "_";
    for (final ChromaticColor color : ChromaticColor.colors()) {
      registerRecipe(registry, new ShapelessRecipes(
        "", new ItemStack(out, 1, color.ordinal()),
        NonNullList.from(
          Ingredient.EMPTY, // default element
          Ingredient.fromStacks(in),
          new OreIngredient(color.getOreName())
        )
      ).setRegistryName(prefix + color.getName()));
    }
  }

  private static void registerReDyeRecipes(final IForgeRegistry<IRecipe> registry, final Item item) {
    final String prefix = item.getRegistryName() + "_recolor_";
    final List<ItemStack> stacks = new ArrayList<>();
    for (final ChromaticColor color : ChromaticColor.colors()) {
      stacks.add(new ItemStack(item, 1, color.ordinal()));
    }
    for (final ChromaticColor color : ChromaticColor.colors()) {
      final List<ItemStack> in = Lists.newArrayList(stacks);
      registerRecipe(registry, new ShapelessRecipes(
        "", in.remove(color.ordinal()),
        NonNullList.from(
          Ingredient.EMPTY, // default element
          Ingredient.fromStacks(in.toArray(new ItemStack[0])),
          new OreIngredient(color.getOreName())
        )
      ).setRegistryName(prefix + color.getName()));
    }
  }

  private static void registerRecipe(final IForgeRegistry<IRecipe> registry, IRecipe recipe) {
    LOGGER.debug("| Registering {} as '{}'", recipe, recipe.getRegistryName());
    registry.register(recipe);
  }
}
