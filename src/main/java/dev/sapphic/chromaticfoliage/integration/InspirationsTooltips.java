package dev.sapphic.chromaticfoliage.integration;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@EventBusSubscriber(modid = ChromaticFoliage.ID, value = Side.CLIENT)
final class InspirationsTooltips {
  private InspirationsTooltips() {
  }

  static void register(final IWailaRegistrar registrar) {
    final Class<?> enlightenedBushBlock;
    try {
      enlightenedBushBlock = Class.forName("knightminer.inspirations.building.block.BlockEnlightenedBush");
    } catch (final ClassNotFoundException e) {
      throw new IllegalStateException("BlockEnlightenedBush is missing or was moved", e);
    }

    registrar.registerBodyProvider(new IWailaDataProvider() {
      @Override
      public List<String> getWailaBody(final ItemStack stack, final List<String> tooltip, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
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

  @SubscribeEvent
  static void appendToItemTooltip(final ItemTooltipEvent event) {
    if (ChromaticConfig.Client.INFO.tooltipColor) {
      final ItemStack stack = event.getItemStack();
      final @Nullable ResourceLocation id = stack.getItem().getRegistryName();
      if ((id != null) && "inspirations:enlightened_bush".equals(id.toString())) {
        final @Nullable NBTTagCompound tag = stack.getTagCompound();
        if ((tag != null) && tag.hasKey("texture", Constants.NBT.TAG_COMPOUND)) {
          final NBTTagCompound texture = tag.getCompoundTag("texture");
          if (texture.getString("id").startsWith(ChromaticFoliage.ID)) {
            final ChromaticColor color = ChromaticColor.of(texture.getShort("Damage"));
            event.getToolTip().add(new TextComponentTranslation(color.getTranslationKey())
              .setStyle(new Style().setColor(TextFormatting.DARK_GRAY))
              .getFormattedText());
          }
        }
      }
    }
  }
}
