package io.github.muehmar.pojobuilder.generator.model.settings;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import io.github.muehmar.pojobuilder.annotations.ConstructorMatching;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class FieldMatchingTest {

  @ParameterizedTest
  @EnumSource(ConstructorMatching.class)
  void fromConstructorMatching_when_allConstructorMatchings_then_doesNotThrow(
      ConstructorMatching constructorMatching) {
    assertDoesNotThrow(() -> FieldMatching.fromConstructorMatching(constructorMatching));
  }
}
