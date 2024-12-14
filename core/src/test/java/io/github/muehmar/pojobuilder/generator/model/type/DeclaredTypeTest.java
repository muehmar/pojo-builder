package io.github.muehmar.pojobuilder.generator.model.type;

import static org.assertj.core.api.Assertions.assertThat;

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
    assertThat(declaredType.getTypeDeclaration().asString()).isEqualTo("Map<String,Integer>");
  }

  @Test
  void getImports_when_nestedClass_then_correctImportWithOuterClass() {
    final DeclaredType declaredType =
        DeclaredType.of(
            Classname.fromString("Customer.Address"),
            PackageName.fromString("io.github.muehmar"),
            PList.empty());
    assertThat(declaredType.getImports())
        .isEqualTo(PList.single(Name.fromString("io.github.muehmar.Customer")));
  }
}
