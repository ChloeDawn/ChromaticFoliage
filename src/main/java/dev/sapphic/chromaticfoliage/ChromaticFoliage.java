package dev.sapphic.chromaticfoliage;

import dev.sapphic.chromaticfoliage.client.ChromaticParticles;
import dev.sapphic.chromaticfoliage.init.ChromaticItems;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.checkerframework.checker.nullness.qual.Nullable;

@Mod(modid = ChromaticFoliage.ID, acceptedMinecraftVersions = "[1.12,1.13)", useMetadata = true)
public final class ChromaticFoliage {
  public static final String ID = "chromaticfoliage";

  public static final PropertyEnum<ChromaticColor> COLOR =
    PropertyEnum.create("color", ChromaticColor.class, ChromaticColor.COLORS);

  public static final CreativeTabs TAB = new CreativeTabs(ID) {
    private ItemStack icon = ItemStack.EMPTY;
    private long lastTicks;

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslationKey() {
      return "item_group." + ID + ".label";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIcon() {
      if (this.icon.isEmpty()) {
        this.icon = this.createIcon();
      }
      final @Nullable World world = Minecraft.getMinecraft().world;
      if ((world != null) && world.isRemote) {
        final long ticks = world.getTotalWorldTime();
        if ((this.lastTicks != ticks) && ((ticks % 20) == 0)) {
          this.icon.setItemDamage((1 + this.icon.getItemDamage()) & 15);
          this.lastTicks = ticks;
        }
      }
      return this.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack createIcon() {
      return new ItemStack(ChromaticItems.CHROMATIC_GRASS, 1, 0);
    }
  };

  public ChromaticFoliage() {
    ChromaticParticles.init();
  }
}

