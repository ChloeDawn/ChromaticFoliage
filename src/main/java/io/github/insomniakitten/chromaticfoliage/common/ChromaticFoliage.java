package io.github.insomniakitten.chromaticfoliage.common;

import io.github.insomniakitten.chromaticfoliage.common.config.ClientConfig;
import io.github.insomniakitten.chromaticfoliage.common.config.FeaturesConfig;
import io.github.insomniakitten.chromaticfoliage.common.config.GeneralConfig;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.InstanceFactory;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkArgument;
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
    return ChromaticItemGroup.INSTANCE;
  }

  public static ClientConfig getClientConfig() {
    return ChromaticConfig.CLIENT;
  }

  public static FeaturesConfig getFeaturesConfig() {
    return ChromaticConfig.FEATURES;
  }

  public static GeneralConfig getGeneralConfig() {
    return ChromaticConfig.GENERAL;
  }

  public static Logger getLogger(final String topic) {
    checkArgument(!isNullOrEmpty(topic), "Topic cannot be blank");
    return LogManager.getLogger(NAME + '|' + topic);
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
