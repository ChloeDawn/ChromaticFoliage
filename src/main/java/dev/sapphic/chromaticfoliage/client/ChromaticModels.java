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
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT, modid = ChromaticFoliage.ID)
final class ChromaticModels {
  private static final MethodHandle BAKED_QUAD_TINT_INDEX_SETTER;
  private static final Pattern DOT = Pattern.compile(".", Pattern.LITERAL);
  private static final String SLASH = Matcher.quoteReplacement("/");

  private static final ImmutableMap<Block, StateMap> STATE_MAPPERS = Streams.concat(
    ChromaticBlocks.CHROMATIC_LEAVES.values().stream(),
    ChromaticBlocks.EMISSIVE_LEAVES.values().stream()
  ).collect(ImmutableMap.toImmutableMap(Function.identity(), block ->
    new StateMap.Builder().ignore(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE).build()
  ));

  static {
    final MethodHandles.Lookup lookup = MethodHandles.lookup();
    try {
      final FMLDeobfuscatingRemapper remapper = FMLDeobfuscatingRemapper.INSTANCE;
      final String owner = remapper.unmap(DOT.matcher(BakedQuad.class.getName()).replaceAll(SLASH));
      final String field = remapper.mapFieldName(owner, "field_178213_b", null);
      final Field declaredField = BakedQuad.class.getDeclaredField(field);
      declaredField.setAccessible(true);
      BAKED_QUAD_TINT_INDEX_SETTER = lookup.unreflectSetter(declaredField);
    } catch (final NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException("Unable to unreflect for 'tintIndex' in 'BakedQuad'", e);
    }
  }

  private ChromaticModels() {
  }

  @SubscribeEvent
  static void registerAll(final ModelRegistryEvent event) {
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
  static void processBakedModels(final ModelBakeEvent event) {
    if (ChromaticConfig.Client.BLOCKS.snowLayers) {
      new StateMapperBase() {
        @Override
        protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
          final @Nullable ResourceLocation id = state.getBlock().getRegistryName();
          if (id != null) {
            return new ModelResourceLocation(id, this.getPropertyString(state.getProperties()));
          }
          return ModelBakery.MODEL_MISSING;
        }
      }.putStateModelLocations(Blocks.SNOW_LAYER).forEach((state, model) -> {
        final IBakedModel bakedModel = event.getModelManager().getModel(model);
        for (final BakedQuad quad : bakedModel.getQuads(state, null, 0)) {
          setTintIndex(quad, 0);
        }
        for (final EnumFacing side : EnumFacing.values()) {
          for (final BakedQuad quad : bakedModel.getQuads(state, side, 0)) {
            setTintIndex(quad, 0);
          }
        }
      });
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
