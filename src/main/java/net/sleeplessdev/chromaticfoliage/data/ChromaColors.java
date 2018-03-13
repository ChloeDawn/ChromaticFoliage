package net.sleeplessdev.chromaticfoliage.data;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.oredict.DyeUtils;
import net.sleeplessdev.chromaticfoliage.config.ChromaClientConfig;

import java.util.Locale;
import java.util.Optional;
import java.util.function.IntSupplier;

public enum ChromaColors implements IStringSerializable {

    BLACK("dyeBlack", MapColor.BLACK, EnumDyeColor.BLACK,() -> ChromaClientConfig.COLORS.black),
    RED("dyeRed", MapColor.RED, EnumDyeColor.RED, () -> ChromaClientConfig.COLORS.red),
    GREEN("dyeGreen", MapColor.GREEN, EnumDyeColor.GREEN, () -> ChromaClientConfig.COLORS.green),
    BROWN("dyeBrown", MapColor.BROWN, EnumDyeColor.BROWN, () -> ChromaClientConfig.COLORS.brown),
    BLUE("dyeBlue", MapColor.BLUE, EnumDyeColor.BLUE, () -> ChromaClientConfig.COLORS.blue),
    PURPLE("dyePurple", MapColor.PURPLE, EnumDyeColor.PURPLE, () -> ChromaClientConfig.COLORS.purple),
    CYAN("dyeCyan", MapColor.CYAN, EnumDyeColor.CYAN, () -> ChromaClientConfig.COLORS.cyan),
    LIGHT_GRAY("dyeLightGray", MapColor.SILVER, EnumDyeColor.SILVER, () -> ChromaClientConfig.COLORS.lightGray),
    GRAY("dyeGray", MapColor.GRAY, EnumDyeColor.GRAY, () -> ChromaClientConfig.COLORS.gray),
    PINK("dyePink", MapColor.PINK, EnumDyeColor.PINK, () -> ChromaClientConfig.COLORS.pink),
    LIME("dyeLime", MapColor.LIME, EnumDyeColor.LIME, () -> ChromaClientConfig.COLORS.lime),
    YELLOW("dyeYellow", MapColor.YELLOW, EnumDyeColor.YELLOW, () -> ChromaClientConfig.COLORS.yellow),
    LIGHT_BLUE("dyeLightBlue", MapColor.LIGHT_BLUE, EnumDyeColor.LIGHT_BLUE, () -> ChromaClientConfig.COLORS.lightBlue),
    MAGENTA("dyeMagenta", MapColor.MAGENTA, EnumDyeColor.MAGENTA, () -> ChromaClientConfig.COLORS.magenta),
    ORANGE("dyeOrange", MapColor.GOLD, EnumDyeColor.ORANGE, () -> ChromaClientConfig.COLORS.orange),
    WHITE("dyeWhite", MapColor.SNOW, EnumDyeColor.WHITE, () -> ChromaClientConfig.COLORS.white);

    public static final ChromaColors[] VALUES = ChromaColors.values();
    public static final PropertyEnum<ChromaColors> PROPERTY = PropertyEnum.create("color", ChromaColors.class);

    private final String oreName;
    private final MapColor mapColor;
    private final EnumDyeColor dyeColor;
    private final IntSupplier colorValue;

    ChromaColors(String oreName, MapColor mapColor, EnumDyeColor dyeColor, IntSupplier colorValue) {
        this.oreName = oreName;
        this.mapColor = mapColor;
        this.dyeColor = dyeColor;
        this.colorValue = colorValue;
    }

    public static Optional<ChromaColors> getColorFor(ItemStack stack) {
        if (stack.isEmpty()) return Optional.empty();

        Optional<EnumDyeColor> optional = DyeUtils.colorFromStack(stack);

        if (optional.isPresent()) {
            EnumDyeColor dyeColor = optional.get();
            for (ChromaColors color : VALUES) {
                if (dyeColor == color.dyeColor) {
                    return Optional.of(color);
                }
            }
        }

        return Optional.empty();
    }

    public String getOreName() {
        return oreName;
    }

    public MapColor getMapColor() {
        return mapColor;
    }

    public EnumDyeColor getDyeColor() {
        return dyeColor;
    }

    public int getColorValue() {
        return colorValue.getAsInt();
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

}
