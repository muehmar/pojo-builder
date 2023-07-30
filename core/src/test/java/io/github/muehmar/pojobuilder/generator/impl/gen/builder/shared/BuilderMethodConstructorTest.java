package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.BuilderMethodConstructor.builderMethodConstructor;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.StandardBuilderGenerator.CLASS_NAME_FOR_OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.StandardBuilderGenerator.CLASS_NAME_FOR_REQUIRED;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.OPTIONAL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.PojoFields;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.model.IndexedField;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import org.junit.jupiter.api.Test;

class BuilderMethodConstructorTest {

  @Test
  void builderMethodConstructor_when_generatorUsedWithRequiredField_then_correctClassname() {
    final Generator<IndexedField, PojoSettings> generator =
        builderMethodConstructor(CLASS_NAME_FOR_REQUIRED);
    final IndexedField field = new IndexedField(Pojos.sample(), PojoFields.requiredId(), 2);
    final String output =
        generator
            .generate(field, PojoSettings.defaultSettings(), Writer.createDefault())
            .asString();

    assertEquals(
        "private Builder2(Builder builder) {\n" + "  this.builder = builder;\n" + "}", output);
  }

  @Test
  void builderMethodConstructor_when_generatorUsedWithOptionalField_then_correctClassname() {
    final Generator<IndexedField, PojoSettings> generator =
        builderMethodConstructor(CLASS_NAME_FOR_OPTIONAL);
    final IndexedField field =
        new IndexedField(Pojos.sample(), PojoFields.requiredId().withNecessity(OPTIONAL), 2);
    final String output =
        generator
            .generate(field, PojoSettings.defaultSettings(), Writer.createDefault())
            .asString();

    assertEquals(
        "private OptBuilder2(Builder builder) {\n" + "  this.builder = builder;\n" + "}", output);
  }

  @Test
  void builderMethodConstructor_when_genericSampleWithRequiredField_then_correctClassname() {
    final Generator<IndexedField, PojoSettings> generator =
        builderMethodConstructor(CLASS_NAME_FOR_REQUIRED);
    final IndexedField field = new IndexedField(Pojos.genericSample(), PojoFields.requiredId(), 2);
    final String output =
        generator
            .generate(field, PojoSettings.defaultSettings(), Writer.createDefault())
            .asString();

    assertEquals(
        "private Builder2(Builder<T, S> builder) {\n" + "  this.builder = builder;\n" + "}",
        output);
  }

  @Test
  void builderMethodConstructor_when_genericSampleWithOptionalField_then_correctClassname() {
    final Generator<IndexedField, PojoSettings> generator =
        builderMethodConstructor(CLASS_NAME_FOR_OPTIONAL);
    final IndexedField field =
        new IndexedField(Pojos.genericSample(), PojoFields.requiredId().withNecessity(OPTIONAL), 2);
    final String output =
        generator
            .generate(field, PojoSettings.defaultSettings(), Writer.createDefault())
            .asString();

    assertEquals(
        "private OptBuilder2(Builder<T, S> builder) {\n" + "  this.builder = builder;\n" + "}",
        output);
  }
}
