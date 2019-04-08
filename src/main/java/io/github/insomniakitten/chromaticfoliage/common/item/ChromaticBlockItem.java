package io.github.insomniakitten.chromaticfoliage.common.item;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.base.ChromaticColor;
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

import javax.annotation.Nullable;
import java.util.List;

public final class ChromaticBlockItem extends ItemBlock {
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
  public void getSubItems(final CreativeTabs group, final NonNullList<ItemStack> items) {
    if (isInCreativeTab(group)) {
      for (final ChromaticColor color : ChromaticColor.colors()) {
        items.add(new ItemStack(this, 1, color.ordinal()));
      }
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<String> tooltip, final ITooltipFlag flag) {
    if (ChromaticFoliage.getClientConfig().getInfoConfig().isItemTooltipEnabled()) {
      tooltip.add(I18n.format(ChromaticColor.valueOf(stack.getMetadata()).getTranslationKey()));
    }
  }

  @Override
  public String toString() {
    return "ChromaticBlockItem(" + block + ')';
  }
}
