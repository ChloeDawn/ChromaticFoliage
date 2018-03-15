package net.sleeplessdev.chromaticfoliage.compat;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.config.ChromaClientConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = ChromaticFoliage.ID, value = Side.CLIENT)
public final class ExtendedTooltipEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!ChromaClientConfig.INFO.itemTooltip) return;
        ResourceLocation name = event.getItemStack().getItem().getRegistryName();
        if (name != null && "inspirations:enlightened_bush".equals(name.toString())) {
            NBTTagCompound nbt = event.getItemStack().getTagCompound();
            if (nbt != null && nbt.hasKey("texture")) {
                NBTTagCompound item = nbt.getCompoundTag("texture");
                if (item.getString("id").contains(ChromaticFoliage.ID)) {
                    int damage = Math.max(0, item.getShort("Damage"));
                    ChromaColors color = ChromaColors.VALUES[damage & 15];
                    String line = createTooltipFor(color);
                    event.getToolTip().add(line);
                }
            }
        }
    }

    private static String createTooltipFor(ChromaColors color) {
        String key = "color.chromaticfoliage." + color.getName() + ".name";
        return ChatFormatting.DARK_GRAY + I18n.format(key) + ChatFormatting.RESET;
    }

}
