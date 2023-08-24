package io.github.muehmar.pojobuilder.generator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    assertEquals("<T, S>", GENERICS.getTypeVariablesFormatted());
  }

  @Test
  void getTypeVariablesFormatted_when_emptyGenerics_then_emptyOutput() {
    assertEquals("", Generics.empty().getTypeVariablesFormatted());
  }

  @Test
  void getBoundedTypeVariablesFormatted_when_generics_then_correctFormatted() {
    assertEquals(
        "<T extends Object & List<String>, S>", GENERICS.getBoundedTypeVariablesFormatted());
  }

  @Test
  void getBoundedTypeVariablesFormatted_when_emptyGenerics_then_emptyOutput() {
    assertEquals("", Generics.empty().getBoundedTypeVariablesFormatted());
  }

  @Test
  void getImports_when_generics_then_correctImports() {
    final PList<Name> imports = GENERICS.getImports();
    assertEquals(
        PList.of("java.lang.Object", "java.util.List", "java.lang.String"),
        imports.map(Name::asString));
  }
}
