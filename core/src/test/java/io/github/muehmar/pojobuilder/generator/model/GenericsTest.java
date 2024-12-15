package io.github.muehmar.pojobuilder.generator.model;

import static org.assertj.core.api.Assertions.assertThat;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import org.junit.jupiter.api.Test;

class GenericsTest {

  private static final Generics GENERICS =
      Generics.of(
          new Generic(Name.fromString("T"), PList.of(Types.object(), Types.list(Types.string()))),
          new Generic(Name.fromString("S"), PList.empty()));

  @Test
  void getTypeVariablesFormatted_when_generics_then_correctFormatted() {
    assertThat(GENERICS.getTypeVariablesFormatted()).isEqualTo("<T, S>");
  }

  @Test
  void getTypeVariablesFormatted_when_emptyGenerics_then_emptyOutput() {
    assertThat(Generics.empty().getTypeVariablesFormatted()).isEqualTo("");
  }

  @Test
  void getBoundedTypeVariablesFormatted_when_generics_then_correctFormatted() {
    assertThat(GENERICS.getBoundedTypeVariablesFormatted())
        .isEqualTo("<T extends Object & List<String>, S>");
  }

  @Test
  void getBoundedTypeVariablesFormatted_when_emptyGenerics_then_emptyOutput() {
    assertThat(Generics.empty().getBoundedTypeVariablesFormatted()).isEqualTo("");
  }

  @Test
  void getImports_when_generics_then_correctImports() {
    final PList<Name> imports = GENERICS.getImports();
    assertThat(imports.map(Name::asString))
        .isEqualTo(PList.of("java.lang.Object", "java.util.List", "java.lang.String"));
  }
}
