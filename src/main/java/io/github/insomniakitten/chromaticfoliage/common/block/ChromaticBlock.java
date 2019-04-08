package io.github.insomniakitten.chromaticfoliage.common.block;

import io.github.insomniakitten.chromaticfoliage.common.base.ChromaticColor;
import net.minecraft.block.properties.PropertyEnum;

public interface ChromaticBlock {
  PropertyEnum<ChromaticColor> COLOR = PropertyEnum.create("color", ChromaticColor.class, ChromaticColor.colors());
}
