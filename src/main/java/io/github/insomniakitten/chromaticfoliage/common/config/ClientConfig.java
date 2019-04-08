package io.github.insomniakitten.chromaticfoliage.common.config;

public interface ClientConfig {
  BlockConfig getBlockConfig();

  ColorConfig getColorsConfig();

  InfoConfig getInfoConfig();

  interface BlockConfig {
    boolean isSnowLayerTintingEnabled();

    boolean isGrassPlantTintingEnabled();
  }

  interface ColorConfig {
    int getBlack();

    int getRed();

    int getGreen();

    int getBrown();

    int getBlue();

    int getPurple();

    int getCyan();

    int getLightGray();

    int getGray();

    int getPink();

    int getLime();

    int getYellow();

    int getLightBlue();

    int getMagenta();

    int getOrange();

    int getWhite();
  }

  interface InfoConfig {
    boolean isItemTooltipEnabled();

    boolean isWailaColorVariantTooltipEnabled();

    boolean isWailaIlluminationTooltipEnabled();
  }
}
