package io.github.insomniakitten.chromaticfoliage.common.support.waila.provider;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.base.ChromaticColor;
import io.github.insomniakitten.chromaticfoliage.common.config.ClientConfig;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;

public final class ChromaticBlockDataProvider implements IWailaDataProvider {
  private static final ClientConfig.InfoConfig CONFIG = ChromaticFoliage.getClientConfig().getInfoConfig();

  @Override
  public List<String> getWailaBody(final ItemStack stack, final List<String> tooltip, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
    if (CONFIG.isWailaColorVariantTooltipEnabled()) {
      final ChromaticColor color = ChromaticColor.valueOf(stack.getMetadata());
      tooltip.add(new TextComponentTranslation(color.getTranslationKey()).getFormattedText());
    }
    return tooltip;
  }

  @Override
  public String toString() {
    return "ChromaticBlockDataProvider";
  }
}
