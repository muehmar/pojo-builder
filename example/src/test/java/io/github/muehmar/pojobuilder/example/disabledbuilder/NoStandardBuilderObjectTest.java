package io.github.muehmar.pojobuilder.example.disabledbuilder;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.muehmar.pojobuilder.example.MethodHelper;
import org.junit.jupiter.api.Test;

class NoStandardBuilderObjectTest {

  @Test
  void noStandardBuilderObjectBuilder_hasNoStandardBuilderFactoryMethods() {
    final Class<NoStandardBuilderObjectBuilder> builderClass = NoStandardBuilderObjectBuilder.class;

    assertThat(MethodHelper.hasMethod(builderClass, "createFull")).isTrue();
    assertThat(MethodHelper.hasMethod(builderClass, "fullNoStandardBuilderObjectBuilder")).isTrue();

    assertThat(MethodHelper.hasMethod(builderClass, "create")).isFalse();
    assertThat(MethodHelper.hasMethod(builderClass, "noStandardBuilderObjectBuilder")).isFalse();
  }
}
