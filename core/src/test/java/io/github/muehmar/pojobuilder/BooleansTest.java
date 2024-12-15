package io.github.muehmar.pojobuilder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BooleansTest {
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void not_when_booleanAsInput_then_negated(boolean in) {
    assertThat(Booleans.not(in)).isNotEqualTo(in);
  }
}
