package io.github.muehmar.pojobuilder;

import static io.github.muehmar.pojobuilder.Functions.mapFirst;
import static org.assertj.core.api.Assertions.assertThat;

import ch.bluecare.commons.data.PList;
import org.junit.jupiter.api.Test;

class FunctionsTest {

  @Test
  void mapFirst_when_calledForList_then_firstElementMapped() {
    final PList<String> result =
        PList.of("one", "two", "three").zipWithIndex().map(mapFirst(str -> str + "-mapped"));

    assertThat(result).isEqualTo(PList.of("one-mapped", "two", "three"));
  }
}
