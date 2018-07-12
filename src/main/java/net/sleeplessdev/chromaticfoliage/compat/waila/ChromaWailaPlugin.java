package net.sleeplessdev.chromaticfoliage.compat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.sleeplessdev.chromaticfoliage.block.ChromaticGrassBlock;
import net.sleeplessdev.chromaticfoliage.block.ChromaticLeavesBlock;
import net.sleeplessdev.chromaticfoliage.block.ChromaticVineBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveGrassBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveLeavesBlock;
import net.sleeplessdev.chromaticfoliage.block.EmissiveVineBlock;
import net.sleeplessdev.chromaticfoliage.config.ChromaClientConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;

import java.util.List;

@WailaPlugin
public final class ChromaWailaPlugin implements IWailaPlugin {
    public static final IWailaDataProvider CHROMATIC_BODY_PROVIDER = new IWailaDataProvider() {
        @Override
        public List<String> getWailaBody(ItemStack stack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            if (ChromaClientConfig.INFO.wailaColor) {
                tooltip.add(ChromaColor.VALUES[stack.getMetadata() & 15].getTooltip());
            }
            return tooltip;
        }
    };

    public static final IWailaDataProvider EMISSIVE_BODY_PROVIDER = new IWailaDataProvider() {
        @Override
        public List<String> getWailaBody(ItemStack stack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            if (ChromaClientConfig.INFO.wailaIlluminated) {
                final String key = "tooltip.chromaticfoliage.emissive.name";
                final ITextComponent component = new TextComponentTranslation(key);
                component.getStyle().setColor(TextFormatting.GOLD);
                tooltip.add(component.getFormattedText());
            }
            return tooltip;
        }
    };

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(CHROMATIC_BODY_PROVIDER, ChromaticGrassBlock.class);
        registrar.registerBodyProvider(CHROMATIC_BODY_PROVIDER, ChromaticLeavesBlock.class);
        registrar.registerBodyProvider(CHROMATIC_BODY_PROVIDER, ChromaticVineBlock.class);
        registrar.registerBodyProvider(EMISSIVE_BODY_PROVIDER, EmissiveGrassBlock.class);
        registrar.registerBodyProvider(EMISSIVE_BODY_PROVIDER, EmissiveLeavesBlock.class);
        registrar.registerBodyProvider(EMISSIVE_BODY_PROVIDER, EmissiveVineBlock.class);
    }
}
