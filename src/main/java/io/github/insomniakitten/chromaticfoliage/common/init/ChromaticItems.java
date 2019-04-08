package io.github.insomniakitten.chromaticfoliage.common.init;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.item.ChromaticBlockItem;
import net.minecraft.block.Block;
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

import static com.google.common.base.Preconditions.checkState;
import static io.github.insomniakitten.chromaticfoliage.common.base.ObjectHolders.checkPresent;

@ObjectHolder(ChromaticFoliage.MOD_ID)
@EventBusSubscriber(modid = ChromaticFoliage.MOD_ID)
public final class ChromaticItems {
  static final String CHROMATIC_GRASS = ChromaticBlocks.CHROMATIC_GRASS;
  static final String CHROMATIC_OAK_LEAVES = ChromaticBlocks.CHROMATIC_OAK_LEAVES;
  static final String CHROMATIC_SPRUCE_LEAVES = ChromaticBlocks.CHROMATIC_SPRUCE_LEAVES;
  static final String CHROMATIC_BIRCH_LEAVES = ChromaticBlocks.CHROMATIC_BIRCH_LEAVES;
  static final String CHROMATIC_JUNGLE_LEAVES = ChromaticBlocks.CHROMATIC_JUNGLE_LEAVES;
  static final String CHROMATIC_ACACIA_LEAVES = ChromaticBlocks.CHROMATIC_ACACIA_LEAVES;
  static final String CHROMATIC_DARK_OAK_LEAVES = ChromaticBlocks.CHROMATIC_DARK_OAK_LEAVES;
  static final String CHROMATIC_VINE = ChromaticBlocks.CHROMATIC_VINE;

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

  private ChromaticItems() {
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
      case OAK: return ChromaticItems.chromaticOakLeaves();
      case SPRUCE: return ChromaticItems.chromaticSpruceLeaves();
      case BIRCH: return ChromaticItems.chromaticBirchLeaves();
      case JUNGLE: return ChromaticItems.chromaticJungleLeaves();
      case ACACIA: return ChromaticItems.chromaticAcaciaLeaves();
      case DARK_OAK: return ChromaticItems.chromaticDarkOakLeaves();
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
    register(registry, ChromaticBlocks.chromaticGrass());
    register(registry, ChromaticBlocks.chromaticOakLeaves());
    register(registry, ChromaticBlocks.chromaticSpruceLeaves());
    register(registry, ChromaticBlocks.chromaticBirchLeaves());
    register(registry, ChromaticBlocks.chromaticJungleLeaves());
    register(registry, ChromaticBlocks.chromaticAcaciaLeaves());
    register(registry, ChromaticBlocks.chromaticDarkOakLeaves());
    register(registry, ChromaticBlocks.chromaticVine());
  }

  private static void register(final IForgeRegistry<Item> registry, final Block block) {
    @Nullable final ResourceLocation name = block.getRegistryName();
    checkState(name != null, "Expected registry name for block %s", block);
    final Item item = new ChromaticBlockItem(block);
    final String path = name.getPath();
    item.setRegistryName(ChromaticFoliage.namespace(path));
    item.setTranslationKey(ChromaticFoliage.namespace(path, '.'));
    LOGGER.debug("| Registering {} as '{}'", item, name);
    registry.register(item);
  }
}
