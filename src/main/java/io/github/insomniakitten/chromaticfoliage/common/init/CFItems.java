package io.github.insomniakitten.chromaticfoliage.common.init;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.item.ChromaticBlockItem;
import net.minecraft.block.BlockPlanks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static io.github.insomniakitten.chromaticfoliage.common.base.ObjectHolders.checkPresent;

@ObjectHolder(ChromaticFoliage.MOD_ID)
@EventBusSubscriber(modid = ChromaticFoliage.MOD_ID)
public final class CFItems {
  static final String CHROMATIC_GRASS = CFBlocks.CHROMATIC_GRASS;
  static final String CHROMATIC_OAK_LEAVES = CFBlocks.CHROMATIC_OAK_LEAVES;
  static final String CHROMATIC_SPRUCE_LEAVES = CFBlocks.CHROMATIC_SPRUCE_LEAVES;
  static final String CHROMATIC_BIRCH_LEAVES = CFBlocks.CHROMATIC_BIRCH_LEAVES;
  static final String CHROMATIC_JUNGLE_LEAVES = CFBlocks.CHROMATIC_JUNGLE_LEAVES;
  static final String CHROMATIC_ACACIA_LEAVES = CFBlocks.CHROMATIC_ACACIA_LEAVES;
  static final String CHROMATIC_DARK_OAK_LEAVES = CFBlocks.CHROMATIC_DARK_OAK_LEAVES;
  static final String CHROMATIC_VINE = CFBlocks.CHROMATIC_VINE;

  private static final Logger LOGGER = ChromaticFoliage.getLogger("Items");

  @Nullable
  @ObjectHolder(CHROMATIC_GRASS)
  private static ChromaticBlockItem chromaticGrass;

  @Nullable
  @ObjectHolder(CHROMATIC_OAK_LEAVES)
  private static ChromaticBlockItem chromaticOakLeaves;

  @Nullable
  @ObjectHolder(CHROMATIC_SPRUCE_LEAVES)
  private static ChromaticBlockItem chromaticSpruceLeaves;

  @Nullable
  @ObjectHolder(CHROMATIC_BIRCH_LEAVES)
  private static ChromaticBlockItem chromaticBirchLeaves;

  @Nullable
  @ObjectHolder(CHROMATIC_JUNGLE_LEAVES)
  private static ChromaticBlockItem chromaticJungleLeaves;

  @Nullable
  @ObjectHolder(CHROMATIC_ACACIA_LEAVES)
  private static ChromaticBlockItem chromaticAcaciaLeaves;

  @Nullable
  @ObjectHolder(CHROMATIC_DARK_OAK_LEAVES)
  private static ChromaticBlockItem chromaticDarkOakLeaves;

  @Nullable
  @ObjectHolder(CHROMATIC_VINE)
  private static ChromaticBlockItem chromaticVine;

  private CFItems() {
    throw new UnsupportedOperationException();
  }

  public static ChromaticBlockItem chromaticGrass() {
    return checkPresent(chromaticGrass, CHROMATIC_GRASS);
  }

  public static ChromaticBlockItem chromaticOakLeaves() {
    return checkPresent(chromaticOakLeaves, CHROMATIC_OAK_LEAVES);
  }

  public static ChromaticBlockItem chromaticSpruceLeaves() {
    return checkPresent(chromaticSpruceLeaves, CHROMATIC_SPRUCE_LEAVES);
  }

  public static ChromaticBlockItem chromaticBirchLeaves() {
    return checkPresent(chromaticBirchLeaves, CHROMATIC_BIRCH_LEAVES);
  }

  public static ChromaticBlockItem chromaticJungleLeaves() {
    return checkPresent(chromaticJungleLeaves, CHROMATIC_JUNGLE_LEAVES);
  }

  public static ChromaticBlockItem chromaticAcaciaLeaves() {
    return checkPresent(chromaticAcaciaLeaves, CHROMATIC_ACACIA_LEAVES);
  }

  public static ChromaticBlockItem chromaticDarkOakLeaves() {
    return checkPresent(chromaticDarkOakLeaves, CHROMATIC_DARK_OAK_LEAVES);
  }

  public static ChromaticBlockItem chromaticVine() {
    return checkPresent(chromaticVine, CHROMATIC_VINE);
  }

  public static ChromaticBlockItem chromaticLeaves(final BlockPlanks.EnumType type) {
    switch (type) {
      case OAK: return CFItems.chromaticOakLeaves();
      case SPRUCE: return CFItems.chromaticSpruceLeaves();
      case BIRCH: return CFItems.chromaticBirchLeaves();
      case JUNGLE: return CFItems.chromaticJungleLeaves();
      case ACACIA: return CFItems.chromaticAcaciaLeaves();
      case DARK_OAK: return CFItems.chromaticDarkOakLeaves();
      default: throw new Error(String.valueOf(type));
    }
  }

  @SubscribeEvent
  public static void registerAll(final RegistryEvent.Register<Item> event) {
    final ResourceLocation name = event.getName();
    LOGGER.debug("Beginning registration to {}", name);
    registerAll(event.getRegistry());
    LOGGER.debug("Completed registration to {}", name);
  }

  private static void registerAll(final IForgeRegistry<Item> registry) {
    register(registry, CHROMATIC_GRASS);
    register(registry, CHROMATIC_OAK_LEAVES);
    register(registry, CHROMATIC_SPRUCE_LEAVES);
    register(registry, CHROMATIC_BIRCH_LEAVES);
    register(registry, CHROMATIC_JUNGLE_LEAVES);
    register(registry, CHROMATIC_ACACIA_LEAVES);
    register(registry, CHROMATIC_DARK_OAK_LEAVES);
    register(registry, CHROMATIC_VINE);
  }

  private static void register(final IForgeRegistry<Item> registry, final String name) {
    final Item item = new ChromaticBlockItem(ChromaticFoliage.getBlock(name));
    item.setRegistryName(ChromaticFoliage.namespace(name));
    item.setTranslationKey(ChromaticFoliage.namespace(name, '.'));
    LOGGER.debug("| Registering {} as '{}'", item, name);
    registry.register(item);
  }
}
