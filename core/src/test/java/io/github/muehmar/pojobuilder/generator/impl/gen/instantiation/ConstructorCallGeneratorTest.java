package io.github.muehmar.pojobuilder.generator.impl.gen.instantiation;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.impl.gen.instantiation.ConstructorCallGenerator.constructorCallGenerator;
import static org.assertj.core.api.Assertions.assertThat;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import org.junit.jupiter.api.Test;

class ConstructorCallGeneratorTest {

  @Test
  void callWithAllLocalVariables_when_samplePojo_then_simpleConstructorCall() {
    final Writer writer =
        constructorCallGenerator()
            .generate(Pojos.sample(), PojoSettings.defaultSettings(), javaWriter());

    assertThat(writer.getRefs()).isEqualTo(PList.empty());
    assertThat(writer.asString()).isEqualTo("new Customer(id, username, nickname);");
  }

  @Test
  void callWithAllLocalVariables_when_genericSamplePojo_then_simpleConstructorCall() {
    final Writer writer =
        constructorCallGenerator()
            .generate(Pojos.genericSample(), PojoSettings.defaultSettings(), javaWriter());

    assertThat(writer.getRefs()).isEqualTo(PList.empty());
    assertThat(writer.asString()).isEqualTo("new Customer<T, S>(id, data, additionalData);");
  }

  @Test
  void
      callWithAllLocalVariables_when_samplePojoAndOptionalArgumentWrappedInOptional_then_wrapNullableFieldInOptional() {
    final Pojo sample = Pojos.sampleWithConstructorWithOptionalArgument();

    final Writer pojo =
        constructorCallGenerator().generate(sample, PojoSettings.defaultSettings(), javaWriter());

    assertThat(pojo.getRefs()).isEqualTo(PList.single(JAVA_UTIL_OPTIONAL));
    assertThat(pojo.asString())
        .isEqualTo("new Customer(id, username, Optional.ofNullable(nickname));");
  }
}
