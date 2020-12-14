package dev.sapphic.chromaticfoliage.item;

import dev.sapphic.chromaticfoliage.ChromaticConfig;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class EmissiveBlockItem extends ChromaticBlockItem {
  public EmissiveBlockItem(final Block block) {
    super(block);
  }

  @Override
  public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
    if (ChromaticConfig.General.listEmissiveBlocks) {
      super.getSubItems(tab, items);
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(final ItemStack stack, final @Nullable World world, final List<String> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);
    if (ChromaticConfig.Client.INFO.tooltipIlluminated) {
      tooltip.add(new TextComponentTranslation("tooltip." + ChromaticFoliage.ID + ".emissive")
        .setStyle(new Style().setColor(TextFormatting.GOLD)).getFormattedText());
    }
  }
}
