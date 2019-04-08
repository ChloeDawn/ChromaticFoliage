package io.github.insomniakitten.chromaticfoliage.common.support.inspirations;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.base.ChromaticColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = ChromaticFoliage.MOD_ID, value = Side.CLIENT)
final class ChromaticInspirationsSupport {
  private static final String ENLIGHTENED_BUSH = "inspirations:enlightened_bush";

  private ChromaticInspirationsSupport() {}

  @SubscribeEvent
  static void appendToItemTooltip(final ItemTooltipEvent event) {
    if (ChromaticFoliage.getClientConfig().getInfoConfig().isItemTooltipEnabled()) {
      final ItemStack stack = event.getItemStack();
      if (ENLIGHTENED_BUSH.equals(String.valueOf(stack.getItem().getRegistryName()))) {
        @Nullable final NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null && nbt.hasKey("texture", Constants.NBT.TAG_COMPOUND)) {
          final NBTTagCompound item = nbt.getCompoundTag("texture");
          if (item.getString("id").startsWith(ChromaticFoliage.MOD_ID)) {
            final ChromaticColor color = ChromaticColor.valueOf(item.getShort("Damage"));
            event.getToolTip().add(new TextComponentTranslation(color.getTranslationKey())
              .setStyle(new Style().setColor(TextFormatting.DARK_GRAY))
              .getFormattedText());
          }
        }
      }
    }
  }
}
