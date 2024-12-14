package io.github.muehmar.pojobuilder.generator.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NameTest {
  @Test
  void toPascalCase_when_valueStartWithLowercase_then_outputStartsWithUppercase() {
    final Name name = Name.fromString("name");
    assertThat(name.toPascalCase().asString()).isEqualTo("Name");
  }

  @Test
  void javaBeansName_when_called_then_correctJavaBeansName() {
    assertThat(Name.fromString("name").javaBeansName().asString()).isEqualTo("Name");
    assertThat(Name.fromString("xIndex").javaBeansName().asString()).isEqualTo("xIndex");
    assertThat(Name.fromString("xxIndex").javaBeansName().asString()).isEqualTo("XxIndex");
  }

  @Test
  void replace_when_called_then_oldNameReplaceWithNew() {
    final Name name = Name.fromString("HelloWorld!");
    assertThat(name.replace(Name.fromString("World"), Name.fromString("Replace")).asString())
        .isEqualTo("HelloReplace!");
  }
}
