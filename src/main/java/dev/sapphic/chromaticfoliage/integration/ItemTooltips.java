package dev.sapphic.chromaticfoliage.integration;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
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

@EventBusSubscriber(modid = ChromaticFoliage.ID, value = Side.CLIENT)
public final class ItemTooltips {
  private ItemTooltips() {
  }

  @SubscribeEvent
  public static void appendTooltip(final ItemTooltipEvent event) {
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
