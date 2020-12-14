package dev.sapphic.chromaticfoliage.recipe;

import com.google.gson.JsonObject;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public final class IlluminationRecipeCondition implements IConditionFactory {
  @Override
  public BooleanSupplier parse(final JsonContext context, final JsonObject json) {
    return () -> ChromaticConfig.General.illuminationRecipes;
  }
}
