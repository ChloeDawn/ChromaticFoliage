package dev.sapphic.chromaticfoliage.init;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.block.EmissiveVineBlock;
import dev.sapphic.chromaticfoliage.block.entity.ChromaticBlockEntity;
import dev.sapphic.chromaticfoliage.block.ChromaticGrassBlock;
import dev.sapphic.chromaticfoliage.block.ChromaticLeavesBlock;
import dev.sapphic.chromaticfoliage.block.ChromaticVineBlock;
import dev.sapphic.chromaticfoliage.block.EmissiveGrassBlock;
import dev.sapphic.chromaticfoliage.block.EmissiveLeavesBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = ChromaticFoliage.ID)
public final class ChromaticBlocks {
  public static final Block CHROMATIC_GRASS = new ChromaticGrassBlock();
  public static final Block CHROMATIC_OAK_LEAVES = new ChromaticLeavesBlock(EnumType.OAK);
  public static final Block CHROMATIC_SPRUCE_LEAVES = new ChromaticLeavesBlock(EnumType.SPRUCE);
  public static final Block CHROMATIC_BIRCH_LEAVES = new ChromaticLeavesBlock(EnumType.BIRCH);
  public static final Block CHROMATIC_JUNGLE_LEAVES = new ChromaticLeavesBlock(EnumType.JUNGLE);
  public static final Block CHROMATIC_ACACIA_LEAVES = new ChromaticLeavesBlock(EnumType.ACACIA);
  public static final Block CHROMATIC_DARK_OAK_LEAVES = new ChromaticLeavesBlock(EnumType.DARK_OAK);
  public static final Block CHROMATIC_VINE = new ChromaticVineBlock();
  public static final Block EMISSIVE_GRASS = new EmissiveGrassBlock();
  public static final Block EMISSIVE_OAK_LEAVES = new EmissiveLeavesBlock(EnumType.OAK);
  public static final Block EMISSIVE_SPRUCE_LEAVES = new EmissiveLeavesBlock(EnumType.SPRUCE);
  public static final Block EMISSIVE_BIRCH_LEAVES = new EmissiveLeavesBlock(EnumType.BIRCH);
  public static final Block EMISSIVE_JUNGLE_LEAVES = new EmissiveLeavesBlock(EnumType.JUNGLE);
  public static final Block EMISSIVE_ACACIA_LEAVES = new EmissiveLeavesBlock(EnumType.ACACIA);
  public static final Block EMISSIVE_DARK_OAK_LEAVES = new EmissiveLeavesBlock(EnumType.DARK_OAK);
  public static final Block EMISSIVE_VINE = new EmissiveVineBlock();

  public static final ImmutableMap<EnumType, Block> CHROMATIC_LEAVES =
    Maps.immutableEnumMap(ImmutableMap.<EnumType, Block>builder()
      .put(EnumType.OAK, CHROMATIC_OAK_LEAVES)
      .put(EnumType.SPRUCE, CHROMATIC_SPRUCE_LEAVES)
      .put(EnumType.BIRCH, CHROMATIC_BIRCH_LEAVES)
      .put(EnumType.JUNGLE, CHROMATIC_JUNGLE_LEAVES)
      .put(EnumType.ACACIA, CHROMATIC_ACACIA_LEAVES)
      .put(EnumType.DARK_OAK, CHROMATIC_DARK_OAK_LEAVES)
      .build());

  public static final ImmutableMap<EnumType, Block> EMISSIVE_LEAVES =
    Maps.immutableEnumMap(ImmutableMap.<EnumType, Block>builder()
      .put(EnumType.OAK, EMISSIVE_OAK_LEAVES)
      .put(EnumType.SPRUCE, EMISSIVE_SPRUCE_LEAVES)
      .put(EnumType.BIRCH, EMISSIVE_BIRCH_LEAVES)
      .put(EnumType.JUNGLE, EMISSIVE_JUNGLE_LEAVES)
      .put(EnumType.ACACIA, EMISSIVE_ACACIA_LEAVES)
      .put(EnumType.DARK_OAK, EMISSIVE_DARK_OAK_LEAVES)
      .build());

  private ChromaticBlocks() {
  }

  @SubscribeEvent
  static void registerAll(final RegistryEvent.Register<Block> event) {
    final IForgeRegistry<Block> registry = event.getRegistry();

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

    register("block_entity", ChromaticBlockEntity.class);
  }

  private static void register(final IForgeRegistry<Block> registry, final String name, final Block block) {
    block.setRegistryName(new ResourceLocation(ChromaticFoliage.ID, name));
    block.setTranslationKey(ChromaticFoliage.ID + '.' + name);
    block.setCreativeTab(ChromaticFoliage.TAB);
    registry.register(block);
  }

  private static void register(final String key, final Class<? extends TileEntity> type) {
    GameRegistry.registerTileEntity(type, new ResourceLocation(ChromaticFoliage.ID, key));
  }
}
