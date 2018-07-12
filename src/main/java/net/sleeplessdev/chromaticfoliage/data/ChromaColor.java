package net.sleeplessdev.chromaticfoliage.data;

import com.google.common.collect.Maps;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.DyeUtils;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

import static net.sleeplessdev.chromaticfoliage.config.ChromaClientConfig.COLORS;

public enum ChromaColor implements IStringSerializable {
    BLACK      ( "dyeBlack",     MapColor.BLACK,      EnumDyeColor.BLACK,      () -> COLORS.black     ),
    RED        ( "dyeRed",       MapColor.RED,        EnumDyeColor.RED,        () -> COLORS.red       ),
    GREEN      ( "dyeGreen",     MapColor.GREEN,      EnumDyeColor.GREEN,      () -> COLORS.green     ),
    BROWN      ( "dyeBrown",     MapColor.BROWN,      EnumDyeColor.BROWN,      () -> COLORS.brown     ),
    BLUE       ( "dyeBlue",      MapColor.BLUE,       EnumDyeColor.BLUE,       () -> COLORS.blue      ),
    PURPLE     ( "dyePurple",    MapColor.PURPLE,     EnumDyeColor.PURPLE,     () -> COLORS.purple    ),
    CYAN       ( "dyeCyan",      MapColor.CYAN,       EnumDyeColor.CYAN,       () -> COLORS.cyan      ),
    LIGHT_GRAY ( "dyeLightGray", MapColor.SILVER,     EnumDyeColor.SILVER,     () -> COLORS.lightGray ),
    GRAY       ( "dyeGray",      MapColor.GRAY,       EnumDyeColor.GRAY,       () -> COLORS.gray      ),
    PINK       ( "dyePink",      MapColor.PINK,       EnumDyeColor.PINK,       () -> COLORS.pink      ),
    LIME       ( "dyeLime",      MapColor.LIME,       EnumDyeColor.LIME,       () -> COLORS.lime      ),
    YELLOW     ( "dyeYellow",    MapColor.YELLOW,     EnumDyeColor.YELLOW,     () -> COLORS.yellow    ),
    LIGHT_BLUE ( "dyeLightBlue", MapColor.LIGHT_BLUE, EnumDyeColor.LIGHT_BLUE, () -> COLORS.lightBlue ),
    MAGENTA    ( "dyeMagenta",   MapColor.MAGENTA,    EnumDyeColor.MAGENTA,    () -> COLORS.magenta   ),
    ORANGE     ( "dyeOrange",    MapColor.GOLD,       EnumDyeColor.ORANGE,     () -> COLORS.orange    ),
    WHITE      ( "dyeWhite",     MapColor.SNOW,       EnumDyeColor.WHITE,      () -> COLORS.white     );

    public static final ChromaColor[] VALUES = ChromaColor.values();

    public static final IProperty<ChromaColor> PROPERTY = PropertyEnum.create("color", ChromaColor.class);

    private static final Map<EnumDyeColor, ChromaColor> DYE_TO_CHROMA_MAP = Stream.of(ChromaColor.VALUES)
        .collect(Maps.toImmutableEnumMap(ChromaColor::getDyeColor, Function.identity()));

    private final String oreName;
    private final MapColor mapColor;
    private final EnumDyeColor dyeColor;
    private final IntSupplier colorValue;

    ChromaColor(String oreName, MapColor mapColor, EnumDyeColor dyeColor, IntSupplier colorValue) {
        this.oreName = oreName;
        this.mapColor = mapColor;
        this.dyeColor = dyeColor;
        this.colorValue = colorValue;
    }

    /**
     * Determines if the stack has a paired EnumDyeColor color, and therefore a ChromaColor
     * Ensures stack is non-empty before parsing it internally, otherwise an exception is thrown
     * @param stack The {@link ItemStack} to query a paired EnumDyeColor and ChromaColor for
     * @return {@link Optional#empty()} if the stack has no paired EnumDyeColor and ChromaColor
     * @see net.minecraftforge.oredict.OreDictionary#getOreIDs for reference on the exception
     */
    public static Optional<ChromaColor> from(ItemStack stack) {
        if (!stack.isEmpty()) {
            return DyeUtils.colorFromStack(stack).map(DYE_TO_CHROMA_MAP::get);
        } else return Optional.empty();
    }

    public String getOreName() {
        return this.oreName;
    }

    public MapColor getMapColor() {
        return this.mapColor;
    }

    public EnumDyeColor getDyeColor() {
        return this.dyeColor;
    }

    public int getColorValue() {
        return this.colorValue.getAsInt();
    }

    public String getTooltip(@Nullable TextFormatting color) {
        final String key = "color.chromaticfoliage." + this.getName() + ".name";
        final ITextComponent component = new TextComponentTranslation(key);
        if (color != null) component.getStyle().setColor(color);
        return component.getFormattedText();
    }

    public String getTooltip() {
        return this.getTooltip(null);
    }

    @Override
    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
