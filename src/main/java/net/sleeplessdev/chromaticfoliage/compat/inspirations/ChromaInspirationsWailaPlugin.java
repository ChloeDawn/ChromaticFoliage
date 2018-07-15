package net.sleeplessdev.chromaticfoliage.compat.inspirations;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Loader;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.config.ChromaClientConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;

import java.util.List;

@WailaPlugin
public final class ChromaInspirationsWailaPlugin implements IWailaPlugin {
    private static final String BUSH_BLOCK_CLASS = "knightminer.inspirations.building.block.BlockEnlightenedBush";
    private static final String TEXTURE_KEY = "texture";
    private static final String ID_KEY = "id";
    private static final String DAMAGE_KEY = "Damage";

    public static final IWailaDataProvider BUSH_BODY_PROVIDER = new IWailaDataProvider() {
        @Override
        public List<String> getWailaBody(ItemStack stack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            if (ChromaClientConfig.INFO.wailaColor) {
                final NBTTagCompound compound = stack.getTagCompound();
                if (compound != null && compound.hasKey(TEXTURE_KEY, Constants.NBT.TAG_COMPOUND)) {
                    final NBTTagCompound item = compound.getCompoundTag(TEXTURE_KEY);
                    if (item.getString(ID_KEY).contains(ChromaticFoliage.ID)) {
                        final int damage = Math.max(0, item.getShort(DAMAGE_KEY));
                        final ChromaColor color = ChromaColor.VALUES[damage & 15];
                        final String key = "color.chromaticfoliage." + color.getName() + ".name";
                        tooltip.add(new TextComponentTranslation(key).getFormattedText());
                    }
                }
            }
            return tooltip;
        }
    };

    @Override
    public void register(IWailaRegistrar registrar) {
        if (Loader.isModLoaded("inspirations")) try {
            //noinspection unchecked
            final Class<?> clazz = Class.forName(BUSH_BLOCK_CLASS);
            if (clazz != null && Block.class.isAssignableFrom(clazz)) {
                registrar.registerBodyProvider(BUSH_BODY_PROVIDER, clazz);
            } else throw new IllegalStateException(clazz + " is not an instance of " + Block.class);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to register Waila body provider", exception);
        }
    }
}
