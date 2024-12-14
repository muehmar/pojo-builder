package io.github.muehmar.pojobuilder.generator.model.type;

import static org.assertj.core.api.Assertions.assertThat;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.impl.gen.Refs;
import io.github.muehmar.pojobuilder.generator.model.Name;
import org.junit.jupiter.api.Test;

class ArrayTypeTest {

  @Test
  void getTypeDeclaration_when_stringArray_then_correctDeclaration() {
    final ArrayType arrayType = ArrayType.fromItemType(Types.string());
    assertThat(arrayType.getTypeDeclaration().asString()).isEqualTo("String[]");
  }

  @Test
  void getTypeDeclaration_when_stringVarargs_then_correctDeclaration() {
    final ArrayType arrayType = ArrayType.varargs(Types.string());
    assertThat(arrayType.getTypeDeclaration().asString()).isEqualTo("String...");
  }

  @Test
  void getName_when_stringArray_then_correctName() {
    final ArrayType arrayType = ArrayType.fromItemType(Types.string());
    assertThat(arrayType.getName().asString()).isEqualTo("String[]");
  }

  @Test
  void getName_when_stringVarargs_then_correctName() {
    final ArrayType arrayType = ArrayType.varargs(Types.string());
    assertThat(arrayType.getName().asString()).isEqualTo("String...");
  }

  @Test
  void getImports_when_stringArray_then_allImports() {
    final ArrayType arrayType = ArrayType.fromItemType(Types.string());
    assertThat(arrayType.getImports().map(Name::asString))
        .isEqualTo(PList.single(Refs.JAVA_LANG_STRING));
  }
}
