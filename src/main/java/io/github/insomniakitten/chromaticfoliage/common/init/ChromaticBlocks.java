package io.github.insomniakitten.chromaticfoliage.common.init;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.block.ChromaticGrassBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.ChromaticLeavesBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.ChromaticVineBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.EmissiveGrassBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.EmissiveLeavesBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.EmissiveVineBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.entity.ChromaticBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static io.github.insomniakitten.chromaticfoliage.common.base.ObjectHolders.checkPresent;

@ObjectHolder(ChromaticFoliage.MOD_ID)
@EventBusSubscriber(modid = ChromaticFoliage.MOD_ID)
public final class ChromaticBlocks {
  static final String CHROMATIC_GRASS = "chromatic_grass";
  static final String EMISSIVE_GRASS = "emissive_grass";
  static final String CHROMATIC_OAK_LEAVES = "chromatic_oak_leaves";
  static final String EMISSIVE_OAK_LEAVES = "emissive_oak_leaves";
  static final String CHROMATIC_SPRUCE_LEAVES = "chromatic_spruce_leaves";
  static final String EMISSIVE_SPRUCE_LEAVES = "emissive_spruce_leaves";
  static final String CHROMATIC_BIRCH_LEAVES = "chromatic_birch_leaves";
  static final String EMISSIVE_BIRCH_LEAVES = "emissive_birch_leaves";
  static final String CHROMATIC_JUNGLE_LEAVES = "chromatic_jungle_leaves";
  static final String EMISSIVE_JUNGLE_LEAVES = "emissive_jungle_leaves";
  static final String CHROMATIC_ACACIA_LEAVES = "chromatic_acacia_leaves";
  static final String EMISSIVE_ACACIA_LEAVES = "emissive_acacia_leaves";
  static final String CHROMATIC_DARK_OAK_LEAVES = "chromatic_dark_oak_leaves";
  static final String EMISSIVE_DARK_OAK_LEAVES = "emissive_dark_oak_leaves";
  static final String CHROMATIC_VINE = "chromatic_vine";
  static final String EMISSIVE_VINE = "emissive_vine";
  static final String BLOCK_ENTITY = "block_entity";

  private static final Logger LOGGER = ChromaticFoliage.getLogger("Blocks");

  @Nullable
  @ObjectHolder(CHROMATIC_GRASS)
  private static ChromaticGrassBlock chromaticGrass;

  @Nullable
  @ObjectHolder(EMISSIVE_GRASS)
  private static EmissiveGrassBlock emissiveGrass;

  @Nullable
  @ObjectHolder(CHROMATIC_OAK_LEAVES)
  private static ChromaticLeavesBlock chromaticOakLeaves;

  @Nullable
  @ObjectHolder(EMISSIVE_OAK_LEAVES)
  private static EmissiveLeavesBlock emissiveOakLeaves;

  @Nullable
  @ObjectHolder(CHROMATIC_SPRUCE_LEAVES)
  private static ChromaticLeavesBlock chromaticSpruceLeaves;

  @Nullable
  @ObjectHolder(EMISSIVE_SPRUCE_LEAVES)
  private static EmissiveLeavesBlock emissiveSpruceLeaves;

  @Nullable
  @ObjectHolder(CHROMATIC_BIRCH_LEAVES)
  private static ChromaticLeavesBlock chromaticBirchLeaves;

  @Nullable
  @ObjectHolder(EMISSIVE_BIRCH_LEAVES)
  private static EmissiveLeavesBlock emissiveBirchLeaves;

  @Nullable
  @ObjectHolder(CHROMATIC_JUNGLE_LEAVES)
  private static ChromaticLeavesBlock chromaticJungleLeaves;

  @Nullable
  @ObjectHolder(EMISSIVE_JUNGLE_LEAVES)
  private static EmissiveLeavesBlock emissiveJungleLeaves;

  @Nullable
  @ObjectHolder(CHROMATIC_ACACIA_LEAVES)
  private static ChromaticLeavesBlock chromaticAcaciaLeaves;

  @Nullable
  @ObjectHolder(EMISSIVE_ACACIA_LEAVES)
  private static EmissiveLeavesBlock emissiveAcaciaLeaves;

  @Nullable
  @ObjectHolder(CHROMATIC_DARK_OAK_LEAVES)
  private static ChromaticLeavesBlock chromaticDarkOakLeaves;

  @Nullable
  @ObjectHolder(EMISSIVE_DARK_OAK_LEAVES)
  private static EmissiveLeavesBlock emissiveDarkOakLeaves;

  @Nullable
  @ObjectHolder(CHROMATIC_VINE)
  private static ChromaticVineBlock chromaticVine;

  @Nullable
  @ObjectHolder(EMISSIVE_VINE)
  private static EmissiveVineBlock emissiveVine;

  private ChromaticBlocks() {
    throw new UnsupportedOperationException();
  }

  public static ChromaticGrassBlock chromaticGrass() {
    return checkPresent(chromaticGrass, CHROMATIC_GRASS);
  }

  public static EmissiveGrassBlock emissiveGrass() {
    return checkPresent(emissiveGrass, EMISSIVE_GRASS);
  }

  public static ChromaticLeavesBlock chromaticOakLeaves() {
    return checkPresent(chromaticOakLeaves, CHROMATIC_OAK_LEAVES);
  }

  public static EmissiveLeavesBlock emissiveOakLeaves() {
    return checkPresent(emissiveOakLeaves, EMISSIVE_OAK_LEAVES);
  }

  public static ChromaticLeavesBlock chromaticSpruceLeaves() {
    return checkPresent(chromaticSpruceLeaves, CHROMATIC_SPRUCE_LEAVES);
  }

  public static EmissiveLeavesBlock emissiveSpruceLeaves() {
    return checkPresent(emissiveSpruceLeaves, EMISSIVE_SPRUCE_LEAVES);
  }

  public static ChromaticLeavesBlock chromaticBirchLeaves() {
    return checkPresent(chromaticBirchLeaves, CHROMATIC_BIRCH_LEAVES);
  }

  public static EmissiveLeavesBlock emissiveBirchLeaves() {
    return checkPresent(emissiveBirchLeaves, EMISSIVE_BIRCH_LEAVES);
  }

  public static ChromaticLeavesBlock chromaticJungleLeaves() {
    return checkPresent(chromaticJungleLeaves, CHROMATIC_JUNGLE_LEAVES);
  }

  public static EmissiveLeavesBlock emissiveJungleLeaves() {
    return checkPresent(emissiveJungleLeaves, EMISSIVE_JUNGLE_LEAVES);
  }

  public static ChromaticLeavesBlock chromaticAcaciaLeaves() {
    return checkPresent(chromaticAcaciaLeaves, CHROMATIC_ACACIA_LEAVES);
  }

  public static EmissiveLeavesBlock emissiveAcaciaLeaves() {
    return checkPresent(emissiveAcaciaLeaves, EMISSIVE_ACACIA_LEAVES);
  }

  public static ChromaticLeavesBlock chromaticDarkOakLeaves() {
    return checkPresent(chromaticDarkOakLeaves, CHROMATIC_DARK_OAK_LEAVES);
  }

  public static EmissiveLeavesBlock emissiveDarkOakLeaves() {
    return checkPresent(emissiveDarkOakLeaves, EMISSIVE_DARK_OAK_LEAVES);
  }

  public static ChromaticVineBlock chromaticVine() {
    return checkPresent(chromaticVine, CHROMATIC_VINE);
  }

  public static EmissiveVineBlock emissiveVine() {
    return checkPresent(emissiveVine, EMISSIVE_VINE);
  }

  public static ChromaticLeavesBlock chromaticLeaves(final EnumType type) {
    switch (type) {
      case OAK: return ChromaticBlocks.chromaticOakLeaves();
      case SPRUCE: return ChromaticBlocks.chromaticSpruceLeaves();
      case BIRCH: return ChromaticBlocks.chromaticBirchLeaves();
      case JUNGLE: return ChromaticBlocks.chromaticJungleLeaves();
      case ACACIA: return ChromaticBlocks.chromaticAcaciaLeaves();
      case DARK_OAK: return ChromaticBlocks.chromaticDarkOakLeaves();
      default: throw new Error(String.valueOf(type));
    }
  }

  public static EmissiveLeavesBlock emissiveLeaves(final EnumType type) {
    switch (type) {
      case OAK: return ChromaticBlocks.emissiveOakLeaves();
      case SPRUCE: return ChromaticBlocks.emissiveSpruceLeaves();
      case BIRCH: return ChromaticBlocks.emissiveBirchLeaves();
      case JUNGLE: return ChromaticBlocks.emissiveJungleLeaves();
      case ACACIA: return ChromaticBlocks.emissiveAcaciaLeaves();
      case DARK_OAK: return ChromaticBlocks.emissiveDarkOakLeaves();
      default: throw new Error(String.valueOf(type));
    }
  }

  @SubscribeEvent
  static void registerAll(final RegistryEvent.Register<Block> event) {
    final ResourceLocation name = event.getName();
    LOGGER.debug("Beginning registration to {}", name);
    registerAll(event.getRegistry());
    LOGGER.debug("Completed registration to {}", name);
  }

  private static void registerAll(final IForgeRegistry<Block> registry) {
    register(registry, CHROMATIC_GRASS, new ChromaticGrassBlock());
    register(registry, EMISSIVE_GRASS, new EmissiveGrassBlock());
    register(registry, CHROMATIC_OAK_LEAVES, new ChromaticLeavesBlock(EnumType.OAK));
    register(registry, EMISSIVE_OAK_LEAVES, new EmissiveLeavesBlock(EnumType.OAK));
    register(registry, CHROMATIC_SPRUCE_LEAVES, new ChromaticLeavesBlock(EnumType.SPRUCE));
    register(registry, EMISSIVE_SPRUCE_LEAVES, new EmissiveLeavesBlock(EnumType.SPRUCE));
    register(registry, CHROMATIC_BIRCH_LEAVES, new ChromaticLeavesBlock(EnumType.BIRCH));
    register(registry, EMISSIVE_BIRCH_LEAVES, new EmissiveLeavesBlock(EnumType.BIRCH));
    register(registry, CHROMATIC_JUNGLE_LEAVES, new ChromaticLeavesBlock(EnumType.JUNGLE));
    register(registry, EMISSIVE_JUNGLE_LEAVES, new EmissiveLeavesBlock(EnumType.JUNGLE));
    register(registry, CHROMATIC_ACACIA_LEAVES, new ChromaticLeavesBlock(EnumType.ACACIA));
    register(registry, EMISSIVE_ACACIA_LEAVES, new EmissiveLeavesBlock(EnumType.ACACIA));
    register(registry, CHROMATIC_DARK_OAK_LEAVES, new ChromaticLeavesBlock(EnumType.DARK_OAK));
    register(registry, EMISSIVE_DARK_OAK_LEAVES, new EmissiveLeavesBlock(EnumType.DARK_OAK));
    register(registry, CHROMATIC_VINE, new ChromaticVineBlock());
    register(registry, EMISSIVE_VINE, new EmissiveVineBlock());
    register(BLOCK_ENTITY, ChromaticBlockEntity.class);
  }

  private static void register(final IForgeRegistry<Block> registry, final String name, final Block block) {
    block.setRegistryName(ChromaticFoliage.namespace(name));
    block.setTranslationKey(ChromaticFoliage.namespace(name, '.'));
    block.setCreativeTab(ChromaticFoliage.getItemGroup());
    LOGGER.debug("| Registering {} as '{}'", block, name);
    registry.register(block);
  }

  private static void register(final String key, final Class<? extends TileEntity> type) {
    LOGGER.debug("| Registering {} as '{}'", type.getSimpleName(), key);
    GameRegistry.registerTileEntity(type, ChromaticFoliage.namespace(key));
  }
}
