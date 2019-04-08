package io.github.insomniakitten.chromaticfoliage.common.base;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkState;

public final class ObjectHolders {
  private ObjectHolders() {
    throw new UnsupportedOperationException();
  }

  @Nonnull
  public static <T> T checkPresent(@Nullable final T reference, final String namespace, final String name) {
    checkState(isPresent(reference), "ObjectHolder '%s:%s' is missing reference", namespace, name);
    return reference;
  }

  @Nonnull
  public static <T> T checkPresent(@Nullable final T reference, final String id) {
    return checkPresent(reference, ChromaticFoliage.MOD_ID, id);
  }

  public static boolean isPresent(@Nullable final Object reference) {
    return reference != null;
  }
}

