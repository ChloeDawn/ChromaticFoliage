package dev.sapphic.chromaticfoliage.init;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.item.ChromaticBlockItem;
import dev.sapphic.chromaticfoliage.item.EmissiveBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = ChromaticFoliage.ID)
public final class ChromaticItems {
  public static final Item CHROMATIC_GRASS = chromatic(ChromaticBlocks.CHROMATIC_GRASS);
  public static final Item CHROMATIC_OAK_LEAVES = chromatic(ChromaticBlocks.CHROMATIC_OAK_LEAVES);
  public static final Item CHROMATIC_SPRUCE_LEAVES = chromatic(ChromaticBlocks.CHROMATIC_SPRUCE_LEAVES);
  public static final Item CHROMATIC_BIRCH_LEAVES = chromatic(ChromaticBlocks.CHROMATIC_BIRCH_LEAVES);
  public static final Item CHROMATIC_JUNGLE_LEAVES = chromatic(ChromaticBlocks.CHROMATIC_JUNGLE_LEAVES);
  public static final Item CHROMATIC_ACACIA_LEAVES = chromatic(ChromaticBlocks.CHROMATIC_ACACIA_LEAVES);
  public static final Item CHROMATIC_DARK_OAK_LEAVES = chromatic(ChromaticBlocks.CHROMATIC_DARK_OAK_LEAVES);
  public static final Item CHROMATIC_VINE = chromatic(ChromaticBlocks.CHROMATIC_VINE);
  public static final Item EMISSIVE_GRASS = emissive(ChromaticBlocks.EMISSIVE_GRASS);
  public static final Item EMISSIVE_OAK_LEAVES = emissive(ChromaticBlocks.EMISSIVE_OAK_LEAVES);
  public static final Item EMISSIVE_SPRUCE_LEAVES = emissive(ChromaticBlocks.EMISSIVE_SPRUCE_LEAVES);
  public static final Item EMISSIVE_BIRCH_LEAVES = emissive(ChromaticBlocks.EMISSIVE_BIRCH_LEAVES);
  public static final Item EMISSIVE_JUNGLE_LEAVES = emissive(ChromaticBlocks.EMISSIVE_JUNGLE_LEAVES);
  public static final Item EMISSIVE_ACACIA_LEAVES = emissive(ChromaticBlocks.EMISSIVE_ACACIA_LEAVES);
  public static final Item EMISSIVE_DARK_OAK_LEAVES = emissive(ChromaticBlocks.EMISSIVE_DARK_OAK_LEAVES);
  public static final Item EMISSIVE_VINE = emissive(ChromaticBlocks.EMISSIVE_VINE);

  public static final ImmutableMap<EnumType, Item> CHROMATIC_LEAVES =
    Maps.immutableEnumMap(ImmutableMap.<EnumType, Item>builder()
      .put(EnumType.OAK, CHROMATIC_OAK_LEAVES)
      .put(EnumType.SPRUCE, CHROMATIC_SPRUCE_LEAVES)
      .put(EnumType.BIRCH, CHROMATIC_BIRCH_LEAVES)
      .put(EnumType.JUNGLE, CHROMATIC_JUNGLE_LEAVES)
      .put(EnumType.ACACIA, CHROMATIC_ACACIA_LEAVES)
      .put(EnumType.DARK_OAK, CHROMATIC_DARK_OAK_LEAVES)
      .build());

  private static final Converter<String, String> TO_ORE_SUFFIX =
    CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.UPPER_CAMEL);

  private ChromaticItems() {
  }

  @SubscribeEvent
  public static void registerAll(final RegistryEvent.Register<Item> event) {
    final IForgeRegistry<Item> registry = event.getRegistry();

    register(registry, "chromatic_grass", CHROMATIC_GRASS);
    register(registry, "chromatic_oak_leaves", CHROMATIC_OAK_LEAVES);
    register(registry, "chromatic_spruce_leaves", CHROMATIC_SPRUCE_LEAVES);
    register(registry, "chromatic_birch_leaves", CHROMATIC_BIRCH_LEAVES);
    register(registry, "chromatic_jungle_leaves", CHROMATIC_JUNGLE_LEAVES);
    register(registry, "chromatic_acacia_leaves", CHROMATIC_ACACIA_LEAVES);
    register(registry, "chromatic_dark_oak_leaves", CHROMATIC_DARK_OAK_LEAVES);
    register(registry, "chromatic_vine", CHROMATIC_VINE);
    register(registry, "emissive_grass", EMISSIVE_GRASS);
    register(registry, "emissive_oak_leaves", EMISSIVE_OAK_LEAVES);
    register(registry, "emissive_spruce_leaves", EMISSIVE_SPRUCE_LEAVES);
    register(registry, "emissive_birch_leaves", EMISSIVE_BIRCH_LEAVES);
    register(registry, "emissive_jungle_leaves", EMISSIVE_JUNGLE_LEAVES);
    register(registry, "emissive_acacia_leaves", EMISSIVE_ACACIA_LEAVES);
    register(registry, "emissive_dark_oak_leaves", EMISSIVE_DARK_OAK_LEAVES);
    register(registry, "emissive_vine", EMISSIVE_VINE);

    OreDictionary.registerOre("treeLeavesOak", new ItemStack(Blocks.LEAVES, 1, 0));
    OreDictionary.registerOre("treeLeavesSpruce", new ItemStack(Blocks.LEAVES, 1, 1));
    OreDictionary.registerOre("treeLeavesBirch", new ItemStack(Blocks.LEAVES, 1, 2));
    OreDictionary.registerOre("treeLeavesJungle", new ItemStack(Blocks.LEAVES, 1, 3));
    OreDictionary.registerOre("treeLeavesAcacia", new ItemStack(Blocks.LEAVES2, 1, 0));
    OreDictionary.registerOre("treeLeavesDarkOak", new ItemStack(Blocks.LEAVES2, 1, 1));

    final int wildcard = OreDictionary.WILDCARD_VALUE;

    OreDictionary.registerOre("grass", new ItemStack(CHROMATIC_GRASS, 1, wildcard));
    OreDictionary.registerOre("treeLeaves", new ItemStack(CHROMATIC_OAK_LEAVES, 1, wildcard));
    OreDictionary.registerOre("treeLeavesOak", new ItemStack(CHROMATIC_OAK_LEAVES, 1, wildcard));
    OreDictionary.registerOre("treeLeaves", new ItemStack(CHROMATIC_SPRUCE_LEAVES, 1, wildcard));
    OreDictionary.registerOre("treeLeavesSpruce", new ItemStack(CHROMATIC_SPRUCE_LEAVES, 1, wildcard));
    OreDictionary.registerOre("treeLeaves", new ItemStack(CHROMATIC_BIRCH_LEAVES, 1, wildcard));
    OreDictionary.registerOre("treeLeavesBirch", new ItemStack(CHROMATIC_BIRCH_LEAVES, 1, wildcard));
    OreDictionary.registerOre("treeLeaves", new ItemStack(CHROMATIC_JUNGLE_LEAVES, 1, wildcard));
    OreDictionary.registerOre("treeLeavesJungle", new ItemStack(CHROMATIC_JUNGLE_LEAVES, 1, wildcard));
    OreDictionary.registerOre("treeLeaves", new ItemStack(CHROMATIC_ACACIA_LEAVES, 1, wildcard));
    OreDictionary.registerOre("treeLeavesAcacia", new ItemStack(CHROMATIC_ACACIA_LEAVES, 1, wildcard));
    OreDictionary.registerOre("treeLeaves", new ItemStack(CHROMATIC_DARK_OAK_LEAVES, 1, wildcard));
    OreDictionary.registerOre("treeLeavesDarkOak", new ItemStack(CHROMATIC_DARK_OAK_LEAVES, 1, wildcard));
    OreDictionary.registerOre("vine", new ItemStack(CHROMATIC_VINE, 1, wildcard));
  }

  private static Item chromatic(final Block block) {
    return new ChromaticBlockItem(block);
  }

  private static Item emissive(final Block block) {
    return new EmissiveBlockItem(block);
  }

  private static void register(final IForgeRegistry<Item> registry, final String name, final Item item) {
    registry.register(item.setRegistryName(new ResourceLocation(ChromaticFoliage.ID, name)));
  }
}
