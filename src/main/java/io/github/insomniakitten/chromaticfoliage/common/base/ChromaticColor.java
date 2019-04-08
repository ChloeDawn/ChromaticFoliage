package io.github.insomniakitten.chromaticfoliage.common.base;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.config.ClientConfig;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.oredict.DyeUtils;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntSupplier;

import static com.google.common.base.Preconditions.checkArgument;

public enum ChromaticColor implements IStringSerializable {
  BLACK("dyeBlack", MapColor.BLACK, EnumDyeColor.BLACK, config()::getBlack),
  RED("dyeRed", MapColor.RED, EnumDyeColor.RED, config()::getRed),
  GREEN("dyeGreen", MapColor.GREEN, EnumDyeColor.GREEN, config()::getGreen),
  BROWN("dyeBrown", MapColor.BROWN, EnumDyeColor.BROWN, config()::getBrown),
  BLUE("dyeBlue", MapColor.BLUE, EnumDyeColor.BLUE, config()::getBlue),
  PURPLE("dyePurple", MapColor.PURPLE, EnumDyeColor.PURPLE, config()::getPurple),
  CYAN("dyeCyan", MapColor.CYAN, EnumDyeColor.CYAN, config()::getCyan),
  LIGHT_GRAY("dyeLightGray", MapColor.SILVER, EnumDyeColor.SILVER, config()::getLightGray),
  GRAY("dyeGray", MapColor.GRAY, EnumDyeColor.GRAY, config()::getGray),
  PINK("dyePink", MapColor.PINK, EnumDyeColor.PINK, config()::getPink),
  LIME("dyeLime", MapColor.LIME, EnumDyeColor.LIME, config()::getLime),
  YELLOW("dyeYellow", MapColor.YELLOW, EnumDyeColor.YELLOW, config()::getYellow),
  LIGHT_BLUE("dyeLightBlue", MapColor.LIGHT_BLUE, EnumDyeColor.LIGHT_BLUE, config()::getLightBlue),
  MAGENTA("dyeMagenta", MapColor.MAGENTA, EnumDyeColor.MAGENTA, config()::getMagenta),
  ORANGE("dyeOrange", MapColor.GOLD, EnumDyeColor.ORANGE, config()::getOrange),
  WHITE("dyeWhite", MapColor.SNOW, EnumDyeColor.WHITE, config()::getWhite);

  private static final ChromaticColor[] VALUES = values();

  private static final ImmutableSet<ChromaticColor> COLORS =
    Sets.immutableEnumSet(EnumSet.allOf(ChromaticColor.class));

  private static final ImmutableMap<String, ChromaticColor> BY_NAME =
    COLORS.stream().collect(ImmutableMap.toImmutableMap(ChromaticColor::getName, Function.identity()));

  private static final ImmutableMap<EnumDyeColor, ChromaticColor> BY_DYE_COLOR =
    COLORS.stream().collect(Maps.toImmutableEnumMap(ChromaticColor::getDyeColor, Function.identity()));

  private final String translationKey;
  private final String oreName;
  private final MapColor mapColor;
  private final EnumDyeColor dyeColor;
  private final IntSupplier colorValue;

  ChromaticColor(final String oreName, final MapColor mapColor, final EnumDyeColor dyeColor, final IntSupplier colorValue) {
    this.translationKey = "color." + ChromaticFoliage.MOD_ID + '.' + getName();
    this.oreName = oreName;
    this.mapColor = mapColor;
    this.dyeColor = dyeColor;
    this.colorValue = colorValue;
  }

  public static ChromaticColor valueOf(final int ordinal) {
    return VALUES[ordinal % VALUES.length];
  }

  @Nullable
  public static ChromaticColor byName(final String name) {
    return BY_NAME.get(name);
  }

  public static ChromaticColor byDyeColor(final EnumDyeColor dyeColor) {
    @Nullable final ChromaticColor color = BY_DYE_COLOR.get(dyeColor);
    checkArgument(color != null, "dye color: %s", dyeColor);
    return color;
  }

  public static Optional<ChromaticColor> byDyeColor(final ItemStack stack) {
    if (stack.isEmpty()) {
      return Optional.empty();
    }

    return DyeUtils.colorFromStack(stack).map(ChromaticColor::byDyeColor);
  }

  public static ImmutableSet<ChromaticColor> colors() {
    return ChromaticColor.COLORS;
  }

  private static ClientConfig.ColorConfig config() {
    return ChromaticFoliage.getClientConfig().getColorsConfig();
  }

  public String getTranslationKey() {
    return translationKey;
  }

  public final String getOreName() {
    return oreName;
  }

  public final MapColor getMapColor() {
    return mapColor;
  }

  public final EnumDyeColor getDyeColor() {
    return dyeColor;
  }

  public final int getColorValue() {
    return colorValue.getAsInt();
  }

  @Override
  public final String getName() {
    return name().toLowerCase(Locale.ROOT);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(getName())
      .add("oreName", getOreName())
      .add("mapColor", getMapColor())
      .add("dyeColor", getDyeColor())
      .add("colorValue", getColorValue())
      .toString();
  }

}
