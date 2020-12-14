package dev.sapphic.chromaticfoliage.item;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class ChromaticBlockItem extends ItemBlock {
  public ChromaticBlockItem(final Block block) {
    super(block);
  }

  @Override
  public int getMetadata(final int damage) {
    return damage;
  }

  @Override
  public boolean getHasSubtypes() {
    return true;
  }

  @Override
  public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
    if (this.isInCreativeTab(tab)) {
      for (final ChromaticColor color : ChromaticColor.COLORS) {
        items.add(new ItemStack(this, 1, color.ordinal()));
      }
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(final ItemStack stack, final @Nullable World world, final List<String> tooltip, final ITooltipFlag flag) {
    if (ChromaticConfig.Client.INFO.tooltipColor) {
      tooltip.add(I18n.format(ChromaticColor.of(stack.getMetadata()).getTranslationKey()));
    }
  }
}
