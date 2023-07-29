package io.github.muehmar.pojobuilder.generator.impl.gen;

import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.function.BiPredicate;

public class Filters {
  private Filters() {}

  public static <T> BiPredicate<T, PojoSettings> isFullBuilderEnabled() {
    return (data, settings) -> settings.isFullBuilderEnabled();
  }

  public static <T> BiPredicate<T, PojoSettings> isStandardBuilderEnabled() {
    return (data, settings) -> settings.isStandardBuilderEnabled();
  }
}
