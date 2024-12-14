package io.github.muehmar.pojobuilder.example.standardbuilder;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

class PackagePrivateBuilderClassTest {

  @Test
  void getClassModifiers_when_calledForBuilderClass_then_isFinalAndPackagePrivate() {
    final int modifiers = PackagePrivateBuilderClassBuilder.class.getModifiers();

    assertThat(Modifier.isPublic(modifiers)).isFalse();
    assertThat(Modifier.isFinal(modifiers)).isTrue();
  }
}
