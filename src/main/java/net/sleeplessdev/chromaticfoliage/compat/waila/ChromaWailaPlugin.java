package net.sleeplessdev.chromaticfoliage.compat.waila;

import com.mojang.realmsclient.gui.ChatFormatting;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.sleeplessdev.chromaticfoliage.block.ChromaticGrassBlock;
import net.sleeplessdev.chromaticfoliage.block.ChromaticLeavesBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveGrassBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveLeavesBlock;
import net.sleeplessdev.chromaticfoliage.config.ChromaClientConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;

import java.util.List;

@WailaPlugin
public final class ChromaWailaPlugin implements IWailaPlugin {

    public static final IWailaDataProvider CHROMATIC_BODY_PROVIDER = new IWailaDataProvider() {
        @Override
        public List<String> getWailaBody(ItemStack stack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            if (ChromaClientConfig.INFO.wailaColor) {
                if (accessor.getNBTData().hasKey("color")) {
                    int index = accessor.getNBTData().getInteger("color");
                    ChromaColors color = ChromaColors.VALUES[index & 15];
                    tooltip.add(I18n.format("color.chromaticfoliage." + color.getName() + ".name"));

                } else {
                    ChromaColors color = ChromaColors.VALUES[stack.getMetadata() & 15];
                    tooltip.add(I18n.format("color.chromaticfoliage." + color.getName() + ".name"));
                }
            }
            return tooltip;
        }
    };

    public static final IWailaDataProvider EMISSIVE_BODY_PROVIDER = new IWailaDataProvider() {
        @Override
        public List<String> getWailaBody(ItemStack stack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            if (ChromaClientConfig.INFO.wailaIlluminated) {
                String line = I18n.format("tooltip.chromaticfoliage.emissive.name");
                tooltip.add(ChatFormatting.GOLD + line + ChatFormatting.RESET);
            }
            return tooltip;
        }
    };

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(CHROMATIC_BODY_PROVIDER, ChromaticGrassBlock.class);
        registrar.registerBodyProvider(CHROMATIC_BODY_PROVIDER, ChromaticLeavesBlock.class);
        registrar.registerBodyProvider(EMISSIVE_BODY_PROVIDER, EmissiveGrassBlock.class);
        registrar.registerBodyProvider(EMISSIVE_BODY_PROVIDER, EmissiveLeavesBlock.class);
    }

}
