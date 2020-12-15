package dev.sapphic.chromaticfoliage.integration;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.block.ChromaticGrassBlock;
import dev.sapphic.chromaticfoliage.block.ChromaticLeavesBlock;
import dev.sapphic.chromaticfoliage.block.ChromaticVineBlock;
import dev.sapphic.chromaticfoliage.block.EmissiveGrassBlock;
import dev.sapphic.chromaticfoliage.block.EmissiveLeavesBlock;
import dev.sapphic.chromaticfoliage.block.EmissiveVineBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Loader;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.stream.Stream;

@WailaPlugin
public final class WailaTooltips implements IWailaPlugin {
  @Override
  public void register(final IWailaRegistrar registrar) {
    Stream.of(ChromaticGrassBlock.class, ChromaticLeavesBlock.class, ChromaticVineBlock.class).forEach(block ->
      registrar.registerBodyProvider(new IWailaDataProvider() {
        @Override
        public List<String> getWailaBody(final ItemStack stack, final List<String> tooltip, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
          if (ChromaticConfig.Client.INFO.wailaColor) {
            final ChromaticColor color = accessor.getBlockState().getValue(ChromaticFoliage.COLOR);
            tooltip.add(new TextComponentTranslation(color.getTranslationKey())
              .setStyle(new Style().setColor(TextFormatting.DARK_GRAY)).getFormattedText());
          }
          return tooltip;
        }
      }, block));

    Stream.of(EmissiveGrassBlock.class, EmissiveLeavesBlock.class, EmissiveVineBlock.class).forEach(block ->
      registrar.registerBodyProvider(new IWailaDataProvider() {
        @Override
        public List<String> getWailaBody(final ItemStack stack, final List<String> tooltip, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
          if (ChromaticConfig.Client.INFO.wailaIlluminated) {
            tooltip.add(new TextComponentTranslation("tooltip." + ChromaticFoliage.ID + ".emissive")
              .setStyle(new Style().setColor(TextFormatting.GOLD)).getFormattedText());
          }
          return tooltip;
        }
      }, block));

    if (Loader.isModLoaded("inspirations")) {
      final Class<?> enlightenedBushBlock;
      try {
        enlightenedBushBlock = Class.forName("knightminer.inspirations.building.block.BlockEnlightenedBush");
      } catch (final ClassNotFoundException e) {
        throw new IllegalStateException("BlockEnlightenedBush is missing or was moved", e);
      }

      registrar.registerBodyProvider(new IWailaDataProvider() {
        @Override
        public List<String> getWailaBody(
          final ItemStack stack, final List<String> tooltip, final IWailaDataAccessor accessor,
          final IWailaConfigHandler config
        ) {
          if (ChromaticConfig.Client.INFO.wailaColor) {
            final @Nullable NBTTagCompound tag = stack.getTagCompound();
            if ((tag != null) && tag.hasKey("texture", Constants.NBT.TAG_COMPOUND)) {
              final NBTTagCompound texture = tag.getCompoundTag("texture");
              if (texture.getString("id").startsWith(ChromaticFoliage.ID)) {
                tooltip.add(new TextComponentTranslation(
                  ChromaticColor.of(texture.getShort("Damage")).getTranslationKey()
                ).getFormattedText());
              }
            }
          }
          return tooltip;
        }
      }, enlightenedBushBlock);
    }
  }
}
