package dev.sapphic.chromaticfoliage.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.WeightedBakedModel;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT, modid = ChromaticFoliage.ID)
public final class ChromaticModels {
  private static final MethodHandle BAKED_QUAD_TINT_INDEX_SETTER;
  private static final MethodHandle BAKED_QUAD_SHADE_SETTER;
  private static final MethodHandle WEIGHTED_MODELS_GETTER;
  private static final MethodHandle WEIGHTED_MODEL_DELEGATE_GETTER;

  private static final Pattern DOT = Pattern.compile(".", Pattern.LITERAL);
  private static final String SLASH = Matcher.quoteReplacement("/");
  private static final EnumFacing[] SIDES = EnumFacing.values();

  private static final ImmutableMap<Block, StateMap> STATE_MAPPERS = Streams.concat(
    ChromaticBlocks.CHROMATIC_LEAVES.values().stream(),
    ChromaticBlocks.EMISSIVE_LEAVES.values().stream()
  ).collect(ImmutableMap.toImmutableMap(Function.identity(), block ->
    new StateMap.Builder().ignore(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE).build()
  ));

  static {
    final MethodHandles.Lookup lookup = MethodHandles.lookup();
    final FMLDeobfuscatingRemapper remapper = FMLDeobfuscatingRemapper.INSTANCE;
    try {
      final String owner = remapper.unmap(DOT.matcher(BakedQuad.class.getName()).replaceAll(SLASH));
      final String tintIndexField = remapper.mapFieldName(owner, "field_178213_b", null);
      final Field declaredField = BakedQuad.class.getDeclaredField(tintIndexField);
      declaredField.setAccessible(true);
      BAKED_QUAD_TINT_INDEX_SETTER = lookup.unreflectSetter(declaredField);
    } catch (final NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException("Unable to unreflect for 'tintIndex' in 'BakedQuad'", e);
    }

    try {
      final Field declaredField = BakedQuad.class.getDeclaredField("applyDiffuseLighting");
      declaredField.setAccessible(true);
      BAKED_QUAD_SHADE_SETTER = lookup.unreflectSetter(declaredField);
    } catch (final NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException("Unable to unreflect for 'applyDiffuseLighting' in 'BakedQuad'", e);
    }

    try {
      final String owner = remapper.unmap(DOT.matcher(WeightedBakedModel.class.getName()).replaceAll(SLASH));
      final String modelsField = remapper.mapFieldName(owner, "field_177565_b", null);
      final Field declaredField = WeightedBakedModel.class.getDeclaredField(modelsField);
      declaredField.setAccessible(true);
      WEIGHTED_MODELS_GETTER = lookup.unreflectGetter(declaredField);
    } catch (final NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException("Unable to unreflect 'models' in 'WeightedBakedModel'", e);
    }

    try {
      final Class<?> ownerType = Class.forName("net.minecraft.client.renderer.block.model.WeightedBakedModel$WeightedModel");
      final String owner = remapper.unmap(DOT.matcher(ownerType.getName()).replaceAll(SLASH));
      final String modelField = remapper.mapFieldName(owner, "field_185281_b", null);
      final Field declaredField = ownerType.getDeclaredField(modelField);
      declaredField.setAccessible(true);
      WEIGHTED_MODEL_DELEGATE_GETTER = lookup.unreflectGetter(declaredField);
    } catch (final ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException("Unable to unreflect 'model' in 'WeightedBakedModel$WeightedModel'", e);
    }
  }

  private ChromaticModels() {
  }

  @SubscribeEvent
  public static void registerAll(final ModelRegistryEvent event) {
    for (final Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
      final @Nullable ResourceLocation id = item.getRegistryName();
      if ((id != null) && ChromaticFoliage.ID.equals(id.getNamespace())) {
        final ModelResourceLocation model = new ModelResourceLocation(id, "inventory");
        for (final ChromaticColor color : ChromaticColor.COLORS) {
          ModelLoader.setCustomModelResourceLocation(item, color.ordinal(), model);
        }
      }
    }
    STATE_MAPPERS.forEach(ModelLoader::setCustomStateMapper);
  }

  @SubscribeEvent
  public static void processBakedModels(final ModelBakeEvent event) {
    final ModelManager models = event.getModelManager();
    forEachModel(ChromaticBlocks.EMISSIVE_GRASS, (state, model) -> noShade(state, models.getModel(model)));
    forEachModel(ChromaticBlocks.EMISSIVE_OAK_LEAVES, (state, model) -> noShade(state, models.getModel(model)));
    forEachModel(ChromaticBlocks.EMISSIVE_SPRUCE_LEAVES, (state, model) -> noShade(state, models.getModel(model)));
    forEachModel(ChromaticBlocks.EMISSIVE_BIRCH_LEAVES, (state, model) -> noShade(state, models.getModel(model)));
    forEachModel(ChromaticBlocks.EMISSIVE_JUNGLE_LEAVES, (state, model) -> noShade(state, models.getModel(model)));
    forEachModel(ChromaticBlocks.EMISSIVE_ACACIA_LEAVES, (state, model) -> noShade(state, models.getModel(model)));
    forEachModel(ChromaticBlocks.EMISSIVE_DARK_OAK_LEAVES, (state, model) -> noShade(state, models.getModel(model)));
    forEachModel(ChromaticBlocks.EMISSIVE_VINE, (state, model) -> noShade(state, models.getModel(model)));
    if (ChromaticConfig.Client.BLOCKS.snowLayers) {
      forEachModel(Blocks.SNOW_LAYER, (state, model) -> tintIndex(state, models.getModel(model)));
    }
  }

  private static void forEachModel(final Block block, final BiConsumer<IBlockState, ModelResourceLocation> action) {
    final ResourceLocation id = Objects.requireNonNull(block.getRegistryName());
    new StateMapperBase() {
      @Override
      protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
        return new ModelResourceLocation(id, this.getPropertyString(state.getProperties()));
      }
    }.putStateModelLocations(block).forEach(action);
  }

  private static void noShade(final IBlockState state, final IBakedModel model) {
    if (model instanceof WeightedBakedModel) {
      for (final Object o : getModels((WeightedBakedModel) model)) {
        final IBakedModel delegate = getDelegate(o);
        for (final BakedQuad quad : delegate.getQuads(state, null, 0)) {
          setShade(quad, false);
        }
        for (final EnumFacing side : SIDES) {
          for (final BakedQuad quad : delegate.getQuads(state, side, 0)) {
            setShade(quad, false);
          }
        }
      }
    } else {
      for (final BakedQuad quad : model.getQuads(state, null, 0)) {
        setShade(quad, false);
      }
      for (final EnumFacing side : SIDES) {
        for (final BakedQuad quad : model.getQuads(state, side, 0)) {
          setShade(quad, false);
        }
      }
    }
  }

  private static List<?> getModels(final WeightedBakedModel model) {
    try {
      return (List<?>) WEIGHTED_MODELS_GETTER.invokeExact(model);
    } catch (final Throwable throwable) {
      throw new IllegalStateException("Unable to get weighted models", throwable);
    }
  }

  private static IBakedModel getDelegate(final Object o) {
    try {
      // Cannot be exact as WeightedBakedModel$WeightedModel isn't accessible
      return (IBakedModel) WEIGHTED_MODEL_DELEGATE_GETTER.invoke(o);
    } catch (final Throwable throwable) {
      throw new IllegalStateException("Unable to get weighted model delegate", throwable);
    }
  }

  private static void setShade(final BakedQuad bakedQuad, final boolean shade) {
    try {
      BAKED_QUAD_SHADE_SETTER.invokeExact(bakedQuad, shade);
    } catch (final Throwable throwable) {
      throw new IllegalStateException("Unable to set baked quad tint index", throwable);
    }
  }

  private static void tintIndex(final IBlockState state, final IBakedModel model) {
    for (final BakedQuad quad : model.getQuads(state, null, 0)) {
      setTintIndex(quad, 0);
    }
    for (final EnumFacing side : SIDES) {
      for (final BakedQuad quad : model.getQuads(state, side, 0)) {
        setTintIndex(quad, 0);
      }
    }
  }

  private static void setTintIndex(final BakedQuad bakedQuad, final int tintIndex) {
    try {
      BAKED_QUAD_TINT_INDEX_SETTER.invokeExact(bakedQuad, tintIndex);
    } catch (final Throwable throwable) {
      throw new IllegalStateException("Unable to set baked quad tint index", throwable);
    }
  }
}
