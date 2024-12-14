package io.github.muehmar.pojobuilder.generator.model.type;

import static org.assertj.core.api.Assertions.assertThat;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Name;
import org.junit.jupiter.api.Test;

class TypeVariableTypeTest {

  @Test
  void getTypeDeclaration_when_typeVariable_then_returnsTypeVariableNameOnly() {
    final TypeVariableType typeVariableType =
        TypeVariableType.ofNameAndUpperBounds(
            Name.fromString("T"),
            PList.single(Types.comparable(Types.typeVariable(Name.fromString("T")))));

    assertThat(typeVariableType.getTypeDeclaration().asString()).isEqualTo("T");
  }

  @Test
  void getTypeVariableDeclaration_when_typeVariable_then_returnsDeclarationWithUpperBounds() {
    final TypeVariableType typeVariableType =
        TypeVariableType.ofNameAndUpperBounds(
            Name.fromString("T"),
            PList.of(Types.comparable(Types.typeVariable(Name.fromString("T"))), Types.string()));

    assertThat(typeVariableType.getTypeVariableDeclaration().asString())
        .isEqualTo("T extends Comparable<T> & String");
  }
}
