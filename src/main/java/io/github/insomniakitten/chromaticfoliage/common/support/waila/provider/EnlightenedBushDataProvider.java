package io.github.insomniakitten.chromaticfoliage.common.support.waila.provider;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.base.ChromaticColor;
import io.github.insomniakitten.chromaticfoliage.common.config.ClientConfig;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants.NBT;

import javax.annotation.Nullable;
import java.util.List;

public final class EnlightenedBushDataProvider implements IWailaDataProvider {
  private static final ClientConfig.InfoConfig CONFIG = ChromaticFoliage.getClientConfig().getInfoConfig();

  @Override
  public List<String> getWailaBody(final ItemStack stack, final List<String> tooltip, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
    if (CONFIG.isWailaColorVariantTooltipEnabled()) {
      @Nullable final NBTTagCompound nbt = stack.getTagCompound();
      if (nbt != null && nbt.hasKey("texture", NBT.TAG_COMPOUND)) {
        final NBTTagCompound item = nbt.getCompoundTag("texture");
        if (item.getString("id").startsWith(ChromaticFoliage.MOD_ID)) {
          final ChromaticColor color = ChromaticColor.valueOf(item.getShort("Damage"));
          tooltip.add(new TextComponentTranslation(color.getTranslationKey()).getFormattedText());
        }
      }
    }
    return tooltip;
  }

  @Override
  public String toString() {
    return "EnlightenedBushDataProvider";
  }
}
