package dev.sapphic.chromaticfoliage;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.oredict.OreDictionary;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.ToIntFunction;

@SuppressWarnings("NonSerializableFieldInSerializableClass")
public enum ChromaticColor implements IStringSerializable {
  BLACK("dyeBlack", MapColor.BLACK, EnumDyeColor.BLACK, config -> config.black),
  RED("dyeRed", MapColor.RED, EnumDyeColor.RED, config -> config.red),
  GREEN("dyeGreen", MapColor.GREEN, EnumDyeColor.GREEN, config -> config.green),
  BROWN("dyeBrown", MapColor.BROWN, EnumDyeColor.BROWN, config -> config.brown),
  BLUE("dyeBlue", MapColor.BLUE, EnumDyeColor.BLUE, config -> config.blue),
  PURPLE("dyePurple", MapColor.PURPLE, EnumDyeColor.PURPLE, config -> config.purple),
  CYAN("dyeCyan", MapColor.CYAN, EnumDyeColor.CYAN, config -> config.cyan),
  LIGHT_GRAY("dyeLightGray", MapColor.SILVER, EnumDyeColor.SILVER, config -> config.lightGray),
  GRAY("dyeGray", MapColor.GRAY, EnumDyeColor.GRAY, config -> config.gray),
  PINK("dyePink", MapColor.PINK, EnumDyeColor.PINK, config -> config.pink),
  LIME("dyeLime", MapColor.LIME, EnumDyeColor.LIME, config -> config.lime),
  YELLOW("dyeYellow", MapColor.YELLOW, EnumDyeColor.YELLOW, config -> config.yellow),
  LIGHT_BLUE("dyeLightBlue", MapColor.LIGHT_BLUE, EnumDyeColor.LIGHT_BLUE, config -> config.lightBlue),
  MAGENTA("dyeMagenta", MapColor.MAGENTA, EnumDyeColor.MAGENTA, config -> config.magenta),
  ORANGE("dyeOrange", MapColor.GOLD, EnumDyeColor.ORANGE, config -> config.orange),
  WHITE("dyeWhite", MapColor.SNOW, EnumDyeColor.WHITE, config -> config.white);

  private static final ChromaticColor[] VALUES = values();

  public static final ImmutableSet<ChromaticColor> COLORS = Sets.immutableEnumSet(EnumSet.allOf(ChromaticColor.class));

  private static final ImmutableMap<EnumDyeColor, ChromaticColor> BY_DYE_COLOR =
    COLORS.stream().collect(Maps.toImmutableEnumMap(ChromaticColor::getDyeColor, Function.identity()));

  private static final ImmutableMap<String, ChromaticColor> BY_ORE_NAME =
    COLORS.stream().collect(ImmutableMap.toImmutableMap(ChromaticColor::getOreName, Function.identity()));

  private final String translationKey;
  private final String oreName;
  private final MapColor mapColor;
  private final EnumDyeColor dyeColor;
  private final IntSupplier intColor;

  ChromaticColor(
    final String oreName, final MapColor mapColor, final EnumDyeColor dyeColor,
    final ToIntFunction<ChromaticConfig.Client.Colors> intColor
  ) {
    this.translationKey = "color." + ChromaticFoliage.ID + '.' + this.getName();
    this.oreName = oreName;
    this.mapColor = mapColor;
    this.dyeColor = dyeColor;
    this.intColor = () -> intColor.applyAsInt(ChromaticConfig.Client.COLORS);
  }

  public String getTranslationKey() {
    return this.translationKey;
  }

  public final String getOreName() {
    return this.oreName;
  }

  public final MapColor getMapColor() {
    return this.mapColor;
  }

  public final EnumDyeColor getDyeColor() {
    return this.dyeColor;
  }

  public final int getIntColor() {
    return this.intColor.getAsInt();
  }

  @Override
  public final String getName() {
    return this.name().toLowerCase(Locale.ROOT);
  }

  public static ChromaticColor of(final int ordinal) {
    return VALUES[ordinal % VALUES.length];
  }

  public static ChromaticColor of(final EnumDyeColor dyeColor) {
    return BY_DYE_COLOR.get(dyeColor);
  }

  public static Optional<ChromaticColor> of(final ItemStack stack) {
    if (!stack.isEmpty()) {
      for (final int id : OreDictionary.getOreIDs(stack)) {
        final String name = OreDictionary.getOreName(id);
        final @Nullable ChromaticColor color = BY_ORE_NAME.get(name);
        if (color != null) {
          return Optional.of(color);
        }
      }
    }
    return Optional.empty();
  }
}
