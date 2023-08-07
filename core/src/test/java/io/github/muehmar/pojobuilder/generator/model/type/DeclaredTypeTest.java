package io.github.muehmar.pojobuilder.generator.model.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import org.junit.jupiter.api.Test;

class DeclaredTypeTest {

  @Test
  void getTypeDeclaration_when_javaMap_then_correctNameReturned() {
    final DeclaredType declaredType =
        DeclaredType.of(
            Classname.fromString("Map"),
            PackageName.javaUtil(),
            PList.of(Types.string(), Types.integer()));
    assertEquals("Map<String,Integer>", declaredType.getTypeDeclaration().asString());
  }

  @Test
  void getImports_when_nestedClass_then_correctImportWithOuterClass() {
    final DeclaredType declaredType =
        DeclaredType.of(
            Classname.fromString("Customer.Address"),
            PackageName.fromString("io.github.muehmar"),
            PList.empty());
    assertEquals(
        PList.single(Name.fromString("io.github.muehmar.Customer")), declaredType.getImports());
  }
}
