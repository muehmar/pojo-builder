package io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe;

import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe.BuildMethod.buildMethod;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.BuildMethod;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class BuildMethodTest {
  @Test
  void buildMethod_when_calledWithSample_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethod();
    final String output =
        generator
            .generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault())
            .asString();

    assertEquals(
        "public Customer build() {\n"
            + "  final Customer instance =\n"
            + "      new Customer(id, username, nickname);\n"
            + "  return instance;\n"
            + "}",
        output);
  }

  @Test
  void buildMethod_when_calledWithGenericSample_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethod();
    final String output =
        generator
            .generate(Pojos.genericSample(), PojoSettings.defaultSettings(), Writer.createDefault())
            .asString();

    assertEquals(
        "public Customer<T, S> build() {\n"
            + "  final Customer<T, S> instance =\n"
            + "      new Customer<>(id, data, additionalData);\n"
            + "  return instance;\n"
            + "}",
        output);
  }

  @Test
  void buildMethod_when_calledWithGenericSampleAndBuildMethod_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethod();
    final io.github.muehmar.pojobuilder.generator.model.BuildMethod buildMethod =
        new BuildMethod(Name.fromString("customBuildMethod"), Types.string());
    final Pojo pojo = Pojos.genericSample().withBuildMethod(Optional.of(buildMethod));
    final String output =
        generator.generate(pojo, PojoSettings.defaultSettings(), Writer.createDefault()).asString();

    assertEquals(
        "public String build() {\n"
            + "  final Customer<T, S> instance =\n"
            + "      new Customer<>(id, data, additionalData);\n"
            + "  return Customer.customBuildMethod(instance);\n"
            + "}",
        output);
  }

  @Test
  void
      buildMethod_when_generatorUsedWithSamplePojoAndConstructorWithOptionalArgument_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethod();
    final Writer writer =
        generator.generate(
            Pojos.sampleWithConstructorWithOptionalArgument(),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    final String output = writer.asString();

    assertTrue(writer.getRefs().exists(JAVA_UTIL_OPTIONAL::equals));
    assertEquals(
        "public Customer build() {\n"
            + "  final Customer instance =\n"
            + "      new Customer(id, username, Optional.ofNullable(nickname));\n"
            + "  return instance;\n"
            + "}",
        output);
  }
}
