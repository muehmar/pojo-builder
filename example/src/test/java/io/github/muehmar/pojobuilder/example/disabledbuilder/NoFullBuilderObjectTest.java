package io.github.muehmar.pojobuilder.example.disabledbuilder;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.muehmar.pojobuilder.example.MethodHelper;
import org.junit.jupiter.api.Test;

class NoFullBuilderObjectTest {
  @Test
  void noFullBuilderObjectBuilder_hasNoFullBuilderFactoryMethods() {
    final Class<NoFullBuilderObjectBuilder> builderClass = NoFullBuilderObjectBuilder.class;

    assertThat(MethodHelper.hasMethod(builderClass, "createFull")).isFalse();
    assertThat(MethodHelper.hasMethod(builderClass, "fullNoFullBuilderObjectBuilder")).isFalse();
    assertThat(MethodHelper.hasMethod(builderClass, "create")).isTrue();
    assertThat(MethodHelper.hasMethod(builderClass, "noFullBuilderObjectBuilder")).isTrue();
  }
}
