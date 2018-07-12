package net.sleeplessdev.chromaticfoliage;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.InstanceFactory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.sleeplessdev.chromaticfoliage.config.ChromaClientConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = ChromaticFoliage.ID,
    name = ChromaticFoliage.NAME,
    version = ChromaticFoliage.VERSION,
    acceptedMinecraftVersions = "[1.12,1.13)",
    dependencies = "required-after-client:ctm@[%CTM%,)"
)
@EventBusSubscriber(modid = ChromaticFoliage.ID)
public final class ChromaticFoliage {
    public static final String ID = "chromaticfoliage";
    public static final String NAME = "Chromatic Foliage";
    public static final String VERSION = "%VERSION%";

    public static final Logger LOGGER = LogManager.getLogger(ChromaticFoliage.ID);

    public static final CreativeTabs TAB = new CreativeTabs(ChromaticFoliage.ID) {
        private volatile ItemStack icon = ItemStack.EMPTY;
        private volatile long lastTick;

        @Override
        @SideOnly(Side.CLIENT)
        public String getTranslatedTabLabel() {
            return "item_group." + ChromaticFoliage.ID + ".label";
        }

        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getIconItemStack() {
            final Minecraft mc = FMLClientHandler.instance().getClient();
            if (icon.isEmpty()) icon = getTabIconItem();
            if (mc.world != null) {
                final long tick = mc.world.getTotalWorldTime();
                if (tick % 20 == 0 && lastTick != tick) {
                    final int meta = (icon.getMetadata() + 1) % 16;
                    icon = new ItemStack(ChromaBlocks.CHROMATIC_GRASS, 1, meta);
                    lastTick = tick;
                }
            }
            return icon;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(ChromaBlocks.CHROMATIC_GRASS, 1, 0);
        }
    };

    private static final ChromaticFoliage INSTANCE = new ChromaticFoliage();

    private ChromaticFoliage() {}

    @InstanceFactory
    public static ChromaticFoliage getInstance() {
        return ChromaticFoliage.INSTANCE;
    }

    @SubscribeEvent
    static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (ChromaticFoliage.ID.equals(event.getModID())) {
            ChromaClientConfig.COLORS.onConfigPre();
            ConfigManager.sync(ChromaticFoliage.ID, Config.Type.INSTANCE);
            ChromaClientConfig.COLORS.onConfigPost();
        }
    }
}
