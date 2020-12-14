package dev.sapphic.chromaticfoliage.init;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

// TODO Migrate to JSON recipes and move ore dictionary to (post) item registry

@EventBusSubscriber(modid = ChromaticFoliage.ID)
final class ChromaticRecipes {
  private static final Converter<String, String> TO_ORE_SUFFIX =
    CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.UPPER_CAMEL);

  private ChromaticRecipes() {
  }

  @SubscribeEvent
  static void registerAll(final RegistryEvent.Register<IRecipe> event) {
    OreDictionary.registerOre("treeLeavesOak", new ItemStack(Blocks.LEAVES, 1, 0));
    OreDictionary.registerOre("treeLeavesSpruce", new ItemStack(Blocks.LEAVES, 1, 1));
    OreDictionary.registerOre("treeLeavesBirch", new ItemStack(Blocks.LEAVES, 1, 2));
    OreDictionary.registerOre("treeLeavesJungle", new ItemStack(Blocks.LEAVES, 1, 3));
    OreDictionary.registerOre("treeLeavesAcacia", new ItemStack(Blocks.LEAVES2, 1, 0));
    OreDictionary.registerOre("treeLeavesDarkOak", new ItemStack(Blocks.LEAVES2, 1, 1));

    registerOreNames(ChromaticItems.CHROMATIC_GRASS, "grass");
    registerOreNames(ChromaticItems.CHROMATIC_OAK_LEAVES, "treeLeaves", "treeLeavesOak");
    registerOreNames(ChromaticItems.CHROMATIC_SPRUCE_LEAVES, "treeLeaves", "treeLeavesSpruce");
    registerOreNames(ChromaticItems.CHROMATIC_BIRCH_LEAVES, "treeLeaves", "treeLeavesBirch");
    registerOreNames(ChromaticItems.CHROMATIC_JUNGLE_LEAVES, "treeLeaves", "treeLeavesJungle");
    registerOreNames(ChromaticItems.CHROMATIC_ACACIA_LEAVES, "treeLeaves", "treeLeavesAcacia");
    registerOreNames(ChromaticItems.CHROMATIC_DARK_OAK_LEAVES, "treeLeaves", "treeLeavesDarkOak");
    registerOreNames(ChromaticItems.CHROMATIC_VINE, "vine");

    final IForgeRegistry<IRecipe> registry = event.getRegistry();

    registerDyeRecipes(registry, ChromaticItems.CHROMATIC_GRASS, new ItemStack(Blocks.GRASS));
    registerDyeRecipes(registry, ChromaticItems.CHROMATIC_OAK_LEAVES, new ItemStack(Blocks.LEAVES, 1, 0));
    registerDyeRecipes(registry, ChromaticItems.CHROMATIC_SPRUCE_LEAVES, new ItemStack(Blocks.LEAVES, 1, 1));
    registerDyeRecipes(registry, ChromaticItems.CHROMATIC_BIRCH_LEAVES, new ItemStack(Blocks.LEAVES, 1, 2));
    registerDyeRecipes(registry, ChromaticItems.CHROMATIC_JUNGLE_LEAVES, new ItemStack(Blocks.LEAVES, 1, 3));
    registerDyeRecipes(registry, ChromaticItems.CHROMATIC_ACACIA_LEAVES, new ItemStack(Blocks.LEAVES2, 1, 0));
    registerDyeRecipes(registry, ChromaticItems.CHROMATIC_DARK_OAK_LEAVES, new ItemStack(Blocks.LEAVES2, 1, 1));
    registerDyeRecipes(registry, ChromaticItems.CHROMATIC_VINE, new ItemStack(Blocks.VINE));

    if (ChromaticConfig.General.recolorRecipes) {
      registerReDyeRecipes(registry, ChromaticItems.CHROMATIC_GRASS);
      registerReDyeRecipes(registry, ChromaticItems.CHROMATIC_OAK_LEAVES);
      registerReDyeRecipes(registry, ChromaticItems.CHROMATIC_SPRUCE_LEAVES);
      registerReDyeRecipes(registry, ChromaticItems.CHROMATIC_BIRCH_LEAVES);
      registerReDyeRecipes(registry, ChromaticItems.CHROMATIC_JUNGLE_LEAVES);
      registerReDyeRecipes(registry, ChromaticItems.CHROMATIC_ACACIA_LEAVES);
      registerReDyeRecipes(registry, ChromaticItems.CHROMATIC_DARK_OAK_LEAVES);
      registerReDyeRecipes(registry, ChromaticItems.CHROMATIC_VINE);
    }

    if (ChromaticConfig.General.illuminationRecipes) {
      registerEmissiveRecipes(registry, ChromaticItems.CHROMATIC_GRASS, ChromaticItems.EMISSIVE_GRASS);
      registerEmissiveRecipes(registry, ChromaticItems.CHROMATIC_OAK_LEAVES, ChromaticItems.EMISSIVE_OAK_LEAVES);
      registerEmissiveRecipes(registry, ChromaticItems.CHROMATIC_SPRUCE_LEAVES, ChromaticItems.EMISSIVE_SPRUCE_LEAVES);
      registerEmissiveRecipes(registry, ChromaticItems.CHROMATIC_BIRCH_LEAVES, ChromaticItems.EMISSIVE_BIRCH_LEAVES);
      registerEmissiveRecipes(registry, ChromaticItems.CHROMATIC_JUNGLE_LEAVES, ChromaticItems.EMISSIVE_JUNGLE_LEAVES);
      registerEmissiveRecipes(registry, ChromaticItems.CHROMATIC_ACACIA_LEAVES, ChromaticItems.EMISSIVE_ACACIA_LEAVES);
      registerEmissiveRecipes(registry, ChromaticItems.CHROMATIC_DARK_OAK_LEAVES, ChromaticItems.EMISSIVE_DARK_OAK_LEAVES);
      registerEmissiveRecipes(registry, ChromaticItems.CHROMATIC_VINE, ChromaticItems.EMISSIVE_VINE);
    }
  }

  private static void registerOreNames(final Item item, final String... prefixes) {
    final ItemStack wildcard = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
    for (final String prefix : prefixes) {
      OreDictionary.registerOre(prefix, wildcard);
      OreDictionary.registerOre(prefix + "Colored", wildcard);
      for (final ChromaticColor color : ChromaticColor.COLORS) {
        final @Nullable String suffix = TO_ORE_SUFFIX.convert(color.getName());
        Preconditions.checkState(suffix != null, "Failed to create ore suffix for %s", color);
        OreDictionary.registerOre(prefix + suffix, new ItemStack(item, 1, color.ordinal()));
      }
    }
  }

  private static void registerDyeRecipes(final IForgeRegistry<IRecipe> registry, final Item out, final ItemStack in) {
    final String prefix = out.getRegistryName() + "_";
    for (final ChromaticColor color : ChromaticColor.COLORS) {
      registry.register(new ShapelessRecipes(
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
    final List<ItemStack> stacks = new ArrayList<>(16);
    for (final ChromaticColor color : ChromaticColor.COLORS) {
      stacks.add(new ItemStack(item, 1, color.ordinal()));
    }
    for (final ChromaticColor color : ChromaticColor.COLORS) {
      final List<ItemStack> in = new ArrayList<>(stacks);
      registry.register(new ShapelessRecipes(
        "", in.remove(color.ordinal()),
        NonNullList.from(
          Ingredient.EMPTY, // default element
          Ingredient.fromStacks(in.toArray(new ItemStack[0])),
          new OreIngredient(color.getOreName())
        )
      ).setRegistryName(prefix + color.getName()));
    }
  }

  private static void registerEmissiveRecipes(final IForgeRegistry<IRecipe> registry, final Item in, final Item out) {
    final String prefix = out.getRegistryName() + "_";
    for (final ChromaticColor color : ChromaticColor.COLORS) {
      registry.register(new ShapelessRecipes(
        "", new ItemStack(out, 1, color.ordinal()),
        NonNullList.from(
          Ingredient.EMPTY, // default element
          Ingredient.fromStacks(new ItemStack(in, 1, color.ordinal())),
          Ingredient.fromItem(Items.GLOWSTONE_DUST)
        )
      ).setRegistryName(prefix + color.getName()));
    }
  }
}
