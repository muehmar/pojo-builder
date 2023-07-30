package io.github.muehmar.pojobuilder.example.disabledbuilder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.muehmar.pojobuilder.example.MethodHelper;
import org.junit.jupiter.api.Test;

class NoStandardBuilderObjectTest {

  @Test
  void noStandardBuilderObjectBuilder_hasNoStandardBuilderFactoryMethods() {
    final Class<NoStandardBuilderObjectBuilder> builderClass = NoStandardBuilderObjectBuilder.class;

    assertTrue(MethodHelper.hasMethod(builderClass, "createFull"));
    assertTrue(MethodHelper.hasMethod(builderClass, "fullNoStandardBuilderObjectBuilder"));

    assertFalse(MethodHelper.hasMethod(builderClass, "create"));
    assertFalse(MethodHelper.hasMethod(builderClass, "noStandardBuilderObjectBuilder"));
  }
}
