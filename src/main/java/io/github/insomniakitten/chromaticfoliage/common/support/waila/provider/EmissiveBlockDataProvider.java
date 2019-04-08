package io.github.insomniakitten.chromaticfoliage.common.support.waila.provider;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.config.ClientConfig;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public final class EmissiveBlockDataProvider implements IWailaDataProvider {
  private static final String TRANSLATION_KEY = "tooltip." + ChromaticFoliage.MOD_ID + ".emissive";
  private static final ClientConfig.InfoConfig CONFIG = ChromaticFoliage.getClientConfig().getInfoConfig();

  @Override
  public List<String> getWailaBody(final ItemStack stack, final List<String> tooltip, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
    if (CONFIG.isWailaIlluminationTooltipEnabled()) {
      tooltip.add(new TextComponentTranslation(TRANSLATION_KEY)
        .setStyle(new Style().setColor(TextFormatting.GOLD))
        .getFormattedText());
    }
    return tooltip;
  }

  @Override
  public String toString() {
    return "EmissiveBlockDataProvider";
  }
}
