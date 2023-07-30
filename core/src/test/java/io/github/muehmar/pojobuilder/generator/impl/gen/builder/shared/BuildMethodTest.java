package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.BuildMethod.buildMethod;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class BuildMethodTest {

  @Test
  void buildMethod_when_calledWithGenericSample_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethod();
    final Writer writer =
        generator.generate(
            Pojos.genericSample(), PojoSettings.defaultSettings(), Writer.createDefault());
    assertEquals(
        "public Customer<T, S> build() {\n" + "  return builder.build();\n" + "}",
        writer.asString());
  }

  @Test
  void buildMethod_when_calledWithGenericSampleWithCustomBuildMethod_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethod();
    final io.github.muehmar.pojobuilder.generator.model.BuildMethod buildMethod =
        new io.github.muehmar.pojobuilder.generator.model.BuildMethod(
            Name.fromString("customBuildMethod"), Types.string());
    final Writer writer =
        generator.generate(
            Pojos.genericSample().withBuildMethod(Optional.of(buildMethod)),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    assertEquals(
        "public String build() {\n" + "  return builder.build();\n" + "}", writer.asString());
  }
}
