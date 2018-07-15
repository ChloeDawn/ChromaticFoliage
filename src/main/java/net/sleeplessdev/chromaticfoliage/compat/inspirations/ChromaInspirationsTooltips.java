package net.sleeplessdev.chromaticfoliage.compat.inspirations;

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
public final class ChromaInspirationsTooltips {
    private static final String ID_ENLIGHTENED_BUSH = "inspirations:enlightened_bush";
    private static final String KEY_TEXTURE = "texture";
    private static final String KEY_ID = "id";
    private static final String KEY_DAMAGE = "Damage";

    private ChromaInspirationsTooltips() {
        throw new UnsupportedOperationException("Cannot instantiate " + this.getClass());
    }

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event) {
        if (!ChromaClientConfig.INFO.itemTooltip) return;
        final ResourceLocation name = event.getItemStack().getItem().getRegistryName();
        if (name != null && ID_ENLIGHTENED_BUSH.equals(name.toString())) {
            final NBTTagCompound compound = event.getItemStack().getTagCompound();
            if (compound != null && compound.hasKey(KEY_TEXTURE, Constants.NBT.TAG_COMPOUND)) {
                final NBTTagCompound item = compound.getCompoundTag(KEY_TEXTURE);
                if (item.getString(KEY_ID).contains(ChromaticFoliage.ID)) {
                    final int damage = Math.max(0, item.getShort(KEY_DAMAGE));
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
