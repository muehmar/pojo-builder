package io.github.muehmar.pojobuilder.generator.model.type;

import static org.assertj.core.api.Assertions.assertThat;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Name;
import org.junit.jupiter.api.Test;

class WildcardTypeTest {

  @Test
  void getTypeDeclaration_when_unbounded_then_returnsQuestionMark() {
    final WildcardType wildcardType = WildcardType.create();
    assertThat(wildcardType.getTypeDeclaration().asString()).isEqualTo("?");
  }

  @Test
  void getTypeDeclaration_when_upperBound_then_returnsExtendsDeclaration() {
    final WildcardType wildcardType = WildcardType.upperBound(Types.integer());
    assertThat(wildcardType.getTypeDeclaration().asString()).isEqualTo("? extends Integer");
  }

  @Test
  void getTypeDeclaration_when_lowerBound_then_returnsSuperDeclaration() {
    final WildcardType wildcardType = WildcardType.lowerBound(Types.string());
    assertThat(wildcardType.getTypeDeclaration().asString()).isEqualTo("? super String");
  }

  @Test
  void getTypeDeclaration_when_upperBoundWithGenericType_then_returnsCorrectDeclaration() {
    final Type listOfString = Types.list(Types.string());
    final WildcardType wildcardType = WildcardType.upperBound(listOfString);
    assertThat(wildcardType.getTypeDeclaration().asString()).isEqualTo("? extends List<String>");
  }

  @Test
  void getImports_when_unbounded_then_returnsEmpty() {
    final WildcardType wildcardType = WildcardType.create();
    assertThat(wildcardType.getImports()).isEqualTo(PList.empty());
  }

  @Test
  void getImports_when_upperBoundWithImport_then_returnsBoundImports() {
    final Type listOfString = Types.list(Types.string());
    final WildcardType wildcardType = WildcardType.upperBound(listOfString);
    assertThat(wildcardType.getImports())
        .containsExactly(Name.fromString("java.util.List"), Name.fromString("java.lang.String"));
  }

  @Test
  void getImports_when_lowerBoundWithImport_then_returnsBoundImports() {
    final Type listOfString = Types.list(Types.string());
    final WildcardType wildcardType = WildcardType.lowerBound(listOfString);
    assertThat(wildcardType.getImports())
        .containsExactly(Name.fromString("java.util.List"), Name.fromString("java.lang.String"));
  }

  @Test
  void getName_when_anyWildcard_then_returnsQuestionMark() {
    assertThat(WildcardType.create().getName().asString()).isEqualTo("?");
    assertThat(WildcardType.upperBound(Types.string()).getName().asString()).isEqualTo("?");
    assertThat(WildcardType.lowerBound(Types.string()).getName().asString()).isEqualTo("?");
  }

  @Test
  void getKind_when_called_then_returnsWildcard() {
    assertThat(WildcardType.create().getKind()).isEqualTo(TypeKind.WILDCARD);
  }
}
