package io.github.insomniakitten.chromaticfoliage.common;

import io.github.insomniakitten.chromaticfoliage.common.config.ClientConfig;
import io.github.insomniakitten.chromaticfoliage.common.config.FeaturesConfig;
import io.github.insomniakitten.chromaticfoliage.common.config.GeneralConfig;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.InstanceFactory;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

@Immutable
@Mod(modid = ChromaticFoliage.MOD_ID, acceptedMinecraftVersions = "[1.12,1.13)", useMetadata = true)
public final class ChromaticFoliage {
  public static final String MOD_ID = "chromaticfoliage";
  public static final String NAME = "ChromaticFoliage";

  private ChromaticFoliage() {}

  @InstanceFactory
  public static Object getModInstance() {
    return ModInstance.CHROMATIC_FOLIAGE;
  }

  public static CreativeTabs getItemGroup() {
    return CFItemGroup.INSTANCE;
  }

  public static ClientConfig getClientConfig() {
    return CFConfig.CLIENT;
  }

  public static FeaturesConfig getFeaturesConfig() {
    return CFConfig.FEATURES;
  }

  public static GeneralConfig getGeneralConfig() {
    return CFConfig.GENERAL;
  }

  public static Logger getLogger(final String topic) {
    checkArgument(!isNullOrEmpty(topic), "Topic cannot be blank");
    return LogManager.getLogger(NAME + '|' + topic);
  }

  public static Block getBlock(final String name, final IForgeRegistry<Block> registry) {
    final ResourceLocation key = namespace(name);
    @Nullable final Block block = registry.getValue(key);
    if (block == null || block == Blocks.AIR) {
      throw new NoSuchElementException("'" + key + "' in " + registry);
    }
    return block;
  }

  public static Block getBlock(final String name) {
    return getBlock(name, checkNotNull(ForgeRegistries.BLOCKS, "ForgeRegistries.BLOCKS"));
  }

  public static Item getItem(final String name, final IForgeRegistry<Item> registry) {
    final ResourceLocation key = namespace(name);
    @Nullable final Item item = registry.getValue(key);
    if (item == null || item == Items.AIR) {
      throw new NoSuchElementException("'" + key + "' in " + registry);
    }
    return item;
  }

  public static Item getItem(final String name) {
    return getItem(name, checkNotNull(ForgeRegistries.ITEMS, "ForgeRegistries.ITEMS"));
  }

  public static ResourceLocation namespace(final String path) {
    checkArgument(!isNullOrEmpty(path), "Path cannot be blank");
    return new ResourceLocation(MOD_ID, path);
  }

  public static String namespace(final String path, final char delimiter) {
    checkArgument(!isNullOrEmpty(path), "Path cannot be blank");
    return MOD_ID + delimiter + path;
  }

  public static boolean isNamespaced(@Nullable final IForgeRegistryEntry<?> entry) {
    return entry != null && isNamespaced(entry.getRegistryName());
  }

  public static boolean isNamespaced(@Nullable final ResourceLocation identifier) {
    return identifier != null && MOD_ID.equals(identifier.getNamespace());
  }

  @Override
  public String toString() {
    return NAME;
  }

  private static final class ModInstance {
    static final ChromaticFoliage CHROMATIC_FOLIAGE = new ChromaticFoliage();

    ModInstance() {
      throw new UnsupportedOperationException();
    }
  }
}
