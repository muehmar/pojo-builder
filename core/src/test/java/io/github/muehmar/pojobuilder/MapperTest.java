package io.github.muehmar.pojobuilder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MapperTest {
  @Test
  void map_when_called_then_mappingFunctionApplied() {
    final int result = Mapper.initial(1).map(i -> i + 4).apply();
    assertThat(result).isEqualTo(5);
  }

  @Test
  void mapConditionally_when_conditionFalse_then_mappingFunctionNotApplied() {
    final int result = Mapper.initial(1).mapConditionally(false, i -> i + 4).apply();
    assertThat(result).isEqualTo(1);
  }

  @Test
  void mapConditionally_when_conditionTrue_then_mappingFunctionApplied() {
    final int result = Mapper.initial(1).mapConditionally(true, i -> i + 4).apply();
    assertThat(result).isEqualTo(5);
  }
}
