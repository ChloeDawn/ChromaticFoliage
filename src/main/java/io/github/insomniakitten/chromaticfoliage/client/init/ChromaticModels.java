package io.github.insomniakitten.chromaticfoliage.client.init;

import io.github.insomniakitten.chromaticfoliage.client.invoke.BakedQuads;
import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.base.ChromaticColor;
import io.github.insomniakitten.chromaticfoliage.common.block.ChromaticLeavesBlock;
import io.github.insomniakitten.chromaticfoliage.common.init.ChromaticBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage.MOD_ID;
import static io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage.isNamespaced;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT, modid = MOD_ID)
final class ChromaticModels {
  private static final Logger LOGGER = ChromaticFoliage.getLogger("Models");

  private ChromaticModels() {
    throw new UnsupportedOperationException();
  }

  @SubscribeEvent
  static void registerAll(final ModelRegistryEvent event) {
    LOGGER.debug("Beginning registration to '{}'", event);
    for (final Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
      if (isNamespaced(item)) {
        register(item);
      }
    }
    for (EnumType type : EnumType.values()) {
      register(ChromaticBlocks.chromaticLeaves(type));
      register(ChromaticBlocks.emissiveLeaves(type));
    }
    LOGGER.debug("Completed registration to '{}'", event);
  }

  @SubscribeEvent
  static void processBakedModels(final ModelBakeEvent event) {
    if (ChromaticFoliage.getClientConfig().getBlockConfig().isSnowLayerTintingEnabled()) {
      final Block block = Blocks.SNOW_LAYER;
      LOGGER.debug("Beginning tint index overriding for {}", block);
      @Nullable final ResourceLocation path = block.getRegistryName();
      checkState(path != null, "Expected registry name for item %s", block);
      for (final int value : BlockSnow.LAYERS.getAllowedValues()) {
        final IBlockState state = block.getDefaultState().withProperty(BlockSnow.LAYERS, value);
        final String variant = BlockSnow.LAYERS.getName() + "=" + BlockSnow.LAYERS.getName(value);
        final ModelResourceLocation mrl = new ModelResourceLocation(path, variant);
        final IBakedModel model = event.getModelManager().getModel(mrl);
        for (final BakedQuad quad : model.getQuads(state, null, 0)) {
          LOGGER.debug("| Setting tint index of {} to 0", quad);
          BakedQuads.setTintIndex(quad, 0);
        }
        for (final EnumFacing side : EnumFacing.VALUES) {
          for (final BakedQuad quad : model.getQuads(state, side, 0)) {
            LOGGER.debug("| Setting tint index of {} to 0", quad);
            BakedQuads.setTintIndex(quad, 0);
          }
        }
      }
      LOGGER.debug("Completed tint index overriding for {}", block);
    }
  }

  private static void register(final Item item) {
    @Nullable final ResourceLocation path = item.getRegistryName();
    checkArgument(path != null, "Expected registry name for item %s", item);
    checkArgument(isNamespaced(path), "Unrecognized namespace in '%s'", path);
    for (final ChromaticColor color : ChromaticColor.colors()) {
      final ModelResourceLocation model = new ModelResourceLocation(path + "_" + color.getName(), "inventory");
      LOGGER.debug("| Binding model {} with data value {} to item {}", model, color.ordinal(), item);
      ModelLoader.setCustomModelResourceLocation(item, color.ordinal(), model);
    }
  }

  private static void register(ChromaticLeavesBlock block) {
    ModelLoader.setCustomStateMapper(block, new StateMap.Builder()
      .ignore(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE)
      .build());
  }
}
