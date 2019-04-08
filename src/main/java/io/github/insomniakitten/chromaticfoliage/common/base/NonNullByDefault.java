package io.github.insomniakitten.chromaticfoliage.common.base;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Nonnull
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifierDefault(
  {
    ElementType.FIELD,
    ElementType.LOCAL_VARIABLE,
    ElementType.METHOD,
    ElementType.PARAMETER,
    ElementType.TYPE_USE
  }
)
public @interface NonNullByDefault {}
