package io.github.muehmar.pojoextension.generator.impl.gen.instantiation;

import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojoextension.generator.Pojos;
import io.github.muehmar.pojoextension.generator.model.Pojo;
import io.github.muehmar.pojoextension.generator.model.settings.PojoSettings;
import org.junit.jupiter.api.Test;

class ConstructorCallGensTest {

  @Test
  void callWithAllLocalVariables_when_samplePojo_then_simpleConstructorCall() {
    final Writer writer =
        ConstructorCallGens.callWithAllLocalVariables("return ")
            .generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault());

    assertEquals(PList.empty(), writer.getRefs());
    assertEquals("return new Customer(id, username, nickname);", writer.asString());
  }

  @Test
  void callWithAllLocalVariables_when_genericSamplePojo_then_simpleConstructorCall() {
    final Writer writer =
        ConstructorCallGens.callWithAllLocalVariables("return ")
            .generate(
                Pojos.genericSample(), PojoSettings.defaultSettings(), Writer.createDefault());

    assertEquals(PList.empty(), writer.getRefs());
    assertEquals("return new Customer<>(id, data, additionalData);", writer.asString());
  }

  @Test
  void
      callWithAllLocalVariables_when_samplePojoAndOptionalArgumentWrappedInOptional_then_wrapNullableFieldInOptional() {
    final Pojo sample = Pojos.sampleWithConstructorWithOptionalArgument();

    final Writer pojo =
        ConstructorCallGens.callWithAllLocalVariables("return ")
            .generate(sample, PojoSettings.defaultSettings(), Writer.createDefault());

    assertEquals(PList.single(JAVA_UTIL_OPTIONAL), pojo.getRefs());
    assertEquals(
        "return new Customer(id, username, Optional.ofNullable(nickname));", pojo.asString());
  }
}
