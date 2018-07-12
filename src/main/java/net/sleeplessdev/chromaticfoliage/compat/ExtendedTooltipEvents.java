package net.sleeplessdev.chromaticfoliage.compat;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.config.ChromaClientConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = ChromaticFoliage.ID, value = Side.CLIENT)
public final class ExtendedTooltipEvents {
    private ExtendedTooltipEvents() {
        throw new UnsupportedOperationException("Cannot instantiate " + this.getClass());
    }

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event) {
        if (!ChromaClientConfig.INFO.itemTooltip) return;
        final ResourceLocation name = event.getItemStack().getItem().getRegistryName();
        if (name != null && "inspirations:enlightened_bush".equals(name.toString())) {
            final NBTTagCompound compound = event.getItemStack().getTagCompound();
            if (compound != null && compound.hasKey("texture", Constants.NBT.TAG_COMPOUND)) {
                final NBTTagCompound item = compound.getCompoundTag("texture");
                if (item.getString("id").contains(ChromaticFoliage.ID)) {
                    final int damage = Math.max(0, item.getShort("Damage"));
                    final ChromaColor color = ChromaColor.VALUES[damage & 15];
                    final String key = "color.chromaticfoliage." + color.getName() + ".name";
                    final ITextComponent component = new TextComponentTranslation(key);
                    component.getStyle().setColor(TextFormatting.DARK_GRAY);
                    event.getToolTip().add(component.getFormattedText());
                }
            }
        }
    }
}
