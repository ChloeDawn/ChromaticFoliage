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
final class CFRecipes {
  private static final Logger LOGGER = ChromaticFoliage.getLogger("Recipes");

  private static final Converter<String, String> TO_ORE_SUFFIX =
    CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.UPPER_CAMEL);

  private CFRecipes() {
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
    final Item grass = ChromaticFoliage.getItem(CFItems.CHROMATIC_GRASS);
    final Item oakLeaves = ChromaticFoliage.getItem(CFItems.CHROMATIC_OAK_LEAVES);
    final Item spruceLeaves = ChromaticFoliage.getItem(CFItems.CHROMATIC_SPRUCE_LEAVES);
    final Item birchLeaves = ChromaticFoliage.getItem(CFItems.CHROMATIC_BIRCH_LEAVES);
    final Item jungleLeaves = ChromaticFoliage.getItem(CFItems.CHROMATIC_JUNGLE_LEAVES);
    final Item acaciaLeaves = ChromaticFoliage.getItem(CFItems.CHROMATIC_ACACIA_LEAVES);
    final Item darkOakLeaves = ChromaticFoliage.getItem(CFItems.CHROMATIC_DARK_OAK_LEAVES);
    final Item vine = ChromaticFoliage.getItem(CFItems.CHROMATIC_VINE);

    registerOre("treeLeavesOak", new ItemStack(Blocks.LEAVES, 1, 0));
    registerOre("treeLeavesSpruce", new ItemStack(Blocks.LEAVES, 1, 1));
    registerOre("treeLeavesBirch", new ItemStack(Blocks.LEAVES, 1, 2));
    registerOre("treeLeavesJungle", new ItemStack(Blocks.LEAVES, 1, 3));
    registerOre("treeLeavesAcacia", new ItemStack(Blocks.LEAVES2, 1, 0));
    registerOre("treeLeavesDarkOak", new ItemStack(Blocks.LEAVES2, 1, 1));

    registerOreNames(grass, "grass");
    registerOreNames(oakLeaves, "treeLeaves", "treeLeavesOak");
    registerOreNames(spruceLeaves, "treeLeaves", "treeLeavesSpruce");
    registerOreNames(birchLeaves, "treeLeaves", "treeLeavesBirch");
    registerOreNames(jungleLeaves, "treeLeaves", "treeLeavesJungle");
    registerOreNames(acaciaLeaves, "treeLeaves", "treeLeavesAcacia");
    registerOreNames(darkOakLeaves, "treeLeaves", "treeLeavesDarkOak");
    registerOreNames(vine, "vine");

    registerDyeRecipes(registry, grass, new ItemStack(Blocks.GRASS));
    registerDyeRecipes(registry, oakLeaves, new ItemStack(Blocks.LEAVES, 1, 0));
    registerDyeRecipes(registry, spruceLeaves, new ItemStack(Blocks.LEAVES, 1, 1));
    registerDyeRecipes(registry, birchLeaves, new ItemStack(Blocks.LEAVES, 1, 2));
    registerDyeRecipes(registry, jungleLeaves, new ItemStack(Blocks.LEAVES, 1, 3));
    registerDyeRecipes(registry, acaciaLeaves, new ItemStack(Blocks.LEAVES2, 1, 0));
    registerDyeRecipes(registry, darkOakLeaves, new ItemStack(Blocks.LEAVES2, 1, 1));
    registerDyeRecipes(registry, vine, new ItemStack(Blocks.VINE));

    if (ChromaticFoliage.getGeneralConfig().isChromaRecoloringEnabled()) {
      registerReDyeRecipes(registry, grass);
      registerReDyeRecipes(registry, oakLeaves);
      registerReDyeRecipes(registry, spruceLeaves);
      registerReDyeRecipes(registry, birchLeaves);
      registerReDyeRecipes(registry, jungleLeaves);
      registerReDyeRecipes(registry, acaciaLeaves);
      registerReDyeRecipes(registry, darkOakLeaves);
      registerReDyeRecipes(registry, vine);
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
    LOGGER.debug("Registering {} as '{}'", recipe, recipe.getRegistryName());
    registry.register(recipe);
  }

  private static Item item(@Nullable final Item item, final String name) {
    checkState(item != null, "ObjectHolder not injected for '%s'", name);
    return item;
  }
}
