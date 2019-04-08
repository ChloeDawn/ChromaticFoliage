package io.github.insomniakitten.chromaticfoliage.common;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage.MOD_ID;

final class ChromaticItemGroup extends CreativeTabs {
  static final ChromaticItemGroup INSTANCE = new ChromaticItemGroup();

  @Nullable
  @ObjectHolder(MOD_ID + ":chromatic_grass")
  private static Item iconItem;

  private ItemStack icon = ItemStack.EMPTY;
  private long lastTick;

  private ChromaticItemGroup() {
    super(MOD_ID);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public String getTranslationKey() {
    return "item_group." + MOD_ID + ".label";
  }

  @Override
  @SideOnly(Side.CLIENT)
  public ItemStack getIcon() {
    if (icon.isEmpty()) icon = createIcon();
    @Nullable final World world = Minecraft.getMinecraft().world;
    if (world != null && world.isRemote) {
      final long currentTick = world.getTotalWorldTime();
      if (lastTick != currentTick && currentTick % 20 == 0) {
        icon.setItemDamage(1 + icon.getItemDamage() & 15);
        lastTick = currentTick;
      }
    }
    return icon;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public ItemStack createIcon() {
    return new ItemStack(checkNotNull(iconItem), 1, 0);
  }

  @Override
  public String toString() {
    return "ChromaticFoliageItemGroup";
  }
}
