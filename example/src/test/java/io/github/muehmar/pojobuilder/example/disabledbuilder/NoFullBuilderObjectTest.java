package io.github.muehmar.pojobuilder.example.disabledbuilder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.muehmar.pojobuilder.example.MethodHelper;
import org.junit.jupiter.api.Test;

class NoFullBuilderObjectTest {
  @Test
  void noFullBuilderObjectBuilder_hasNoFullBuilderFactoryMethods() {
    final Class<NoFullBuilderObjectBuilder> builderClass = NoFullBuilderObjectBuilder.class;

    assertFalse(MethodHelper.hasMethod(builderClass, "createFull"));
    assertFalse(MethodHelper.hasMethod(builderClass, "fullNoFullBuilderObjectBuilder"));
    assertTrue(MethodHelper.hasMethod(builderClass, "create"));
    assertTrue(MethodHelper.hasMethod(builderClass, "noFullBuilderObjectBuilder"));
  }
}
