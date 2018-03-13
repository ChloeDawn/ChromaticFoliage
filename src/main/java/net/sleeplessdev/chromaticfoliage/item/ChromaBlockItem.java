package net.sleeplessdev.chromaticfoliage.item;

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
import net.sleeplessdev.chromaticfoliage.config.ChromaClientConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;

import java.util.List;

public final class ChromaBlockItem extends ItemBlock {

    public ChromaBlockItem(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (ChromaColors color : ChromaColors.VALUES) {
                items.add(new ItemStack(this, 1, color.ordinal()));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (ChromaClientConfig.INFO.itemTooltip) {
            ChromaColors color = ChromaColors.VALUES[stack.getMetadata() & 15];
            tooltip.add(I18n.format("color.chromaticfoliage." + color.getName() + ".name"));
        }
    }

}
