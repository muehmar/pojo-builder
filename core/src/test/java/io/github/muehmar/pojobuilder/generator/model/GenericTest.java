package io.github.muehmar.pojobuilder.generator.model;

import static org.assertj.core.api.Assertions.assertThat;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import org.junit.jupiter.api.Test;

class GenericTest {

  @Test
  void getTypeDeclaration_when_called_then_correctFormatted() {
    final Generic generic =
        GenericBuilder.create()
            .typeVariable(Name.fromString("T"))
            .upperBounds(
                PList.of(
                    Types.list(Types.string()),
                    Types.comparable(Types.typeVariable(Name.fromString("T")))))
            .build();

    assertThat(generic.getTypeDeclaration().asString())
        .isEqualTo("T extends List<String> & Comparable<T>");
  }
}
