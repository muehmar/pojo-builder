package io.github.muehmar.pojoextension.generator.impl.gen.safebuilder;

import static io.github.muehmar.pojoextension.generator.data.Necessity.OPTIONAL;
import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_LANG_INTEGER;
import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_LANG_STRING;
import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_UTIL_LIST;
import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_UTIL_MAP;
import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.muehmar.pojoextension.generator.Generator;
import io.github.muehmar.pojoextension.generator.PojoFields;
import io.github.muehmar.pojoextension.generator.Pojos;
import io.github.muehmar.pojoextension.generator.data.Pojo;
import io.github.muehmar.pojoextension.generator.data.PojoField;
import io.github.muehmar.pojoextension.generator.data.PojoSettings;
import io.github.muehmar.pojoextension.generator.writer.Writer;
import org.junit.jupiter.api.Test;

class NormalBuilderGensTest {
  @Test
  void buildMethod_when_generatorUsedWithSamplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = NormalBuilderGens.buildMethod();
    final String output =
        generator
            .generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault())
            .asString();

    assertEquals(
        "public Customer build() {\n" + "  return new Customer(id, username, nickname);\n" + "}",
        output);
  }

  @Test
  void
      buildMethod_when_generatorUsedWithSamplePojoAndConstructorWithOptionalArgument_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = NormalBuilderGens.buildMethod();
    final Writer writer =
        generator.generate(
            Pojos.sampleWithConstructorWithOptionalArgument(),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    final String output = writer.asString();

    assertTrue(writer.getRefs().exists(JAVA_UTIL_OPTIONAL::equals));
    assertEquals(
        "public Customer build() {\n"
            + "  return new Customer(id, username, Optional.ofNullable(nickname));\n"
            + "}",
        output);
  }

  @Test
  void setMethod_when_generatorUsedWithRequiredField_then_correctPrivateMethodGenerated() {
    final Generator<PojoField, PojoSettings> generator = NormalBuilderGens.setMethod();

    final Writer writer =
        generator.generate(
            PojoFields.requiredId(), PojoSettings.defaultSettings(), Writer.createDefault());
    final String output = writer.asString();

    assertTrue(writer.getRefs().exists(JAVA_LANG_INTEGER::equals));
    assertEquals(
        "private Builder setId(Integer id) {\n" + "  this.id = id;\n" + "  return this;\n" + "}",
        output);
  }

  @Test
  void setMethod_when_generatorUsedWithGenericType_then_correctRefs() {
    final Generator<PojoField, PojoSettings> generator = NormalBuilderGens.setMethod();
    final PojoField field = PojoFields.requiredMap();

    final Writer writer =
        generator.generate(field, PojoSettings.defaultSettings(), Writer.createDefault());

    assertTrue(writer.getRefs().exists(JAVA_UTIL_LIST::equals));
    assertTrue(writer.getRefs().exists(JAVA_LANG_STRING::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_MAP::equals));
  }

  @Test
  void setMethod_when_generatorUsedWithOptionalField_then_correctPublicMethodGenerated() {
    final Generator<PojoField, PojoSettings> generator = NormalBuilderGens.setMethod();

    final Writer writer =
        generator.generate(
            PojoFields.requiredId().withNecessity(OPTIONAL),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    final String output = writer.asString();

    assertTrue(writer.getRefs().exists(JAVA_LANG_INTEGER::equals));
    assertEquals(
        "public Builder setId(Integer id) {\n" + "  this.id = id;\n" + "  return this;\n" + "}",
        output);
  }

  @Test
  void setMethodOptional_when_optionalField_then_correctPublicMethodGenerated() {
    final Generator<PojoField, PojoSettings> generator = NormalBuilderGens.setMethodOptional();

    final Writer writer =
        generator.generate(
            PojoFields.requiredId().withNecessity(OPTIONAL),
            PojoSettings.defaultSettings(),
            Writer.createDefault());

    final String output = writer.asString();

    assertTrue(writer.getRefs().exists(JAVA_UTIL_OPTIONAL::equals));
    assertTrue(writer.getRefs().exists(JAVA_LANG_INTEGER::equals));
    assertEquals(
        "public Builder setId(Optional<Integer> id) {\n"
            + "  this.id = id.orElse(null);\n"
            + "  return this;\n"
            + "}",
        output);
  }

  @Test
  void setMethodOptional_when_optionalFieldWithGenericType_then_correctRefs() {
    final Generator<PojoField, PojoSettings> generator = NormalBuilderGens.setMethodOptional();

    final Writer writer =
        generator.generate(
            PojoFields.requiredMap().withNecessity(OPTIONAL),
            PojoSettings.defaultSettings(),
            Writer.createDefault());

    assertTrue(writer.getRefs().exists(JAVA_UTIL_OPTIONAL::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_MAP::equals));
    assertTrue(writer.getRefs().exists(JAVA_LANG_STRING::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_LIST::equals));
  }

  @Test
  void builderClass_when_generatorUsedWithSamplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = NormalBuilderGens.builderClass();
    final String output =
        generator
            .generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault())
            .asString();

    assertEquals(
        "public static final class Builder {\n"
            + "  private Builder() {\n"
            + "  }\n"
            + "\n"
            + "  private Integer id;\n"
            + "  private String username;\n"
            + "  private String nickname;\n"
            + "\n"
            + "  private Builder setId(Integer id) {\n"
            + "    this.id = id;\n"
            + "    return this;\n"
            + "  }\n"
            + "\n"
            + "  private Builder setUsername(String username) {\n"
            + "    this.username = username;\n"
            + "    return this;\n"
            + "  }\n"
            + "\n"
            + "  public Builder setNickname(String nickname) {\n"
            + "    this.nickname = nickname;\n"
            + "    return this;\n"
            + "  }\n"
            + "\n"
            + "  public Builder setNickname(Optional<String> nickname) {\n"
            + "    this.nickname = nickname.orElse(null);\n"
            + "    return this;\n"
            + "  }\n"
            + "\n"
            + "  public Customer build() {\n"
            + "    return new Customer(id, username, nickname);\n"
            + "  }\n"
            + "}",
        output);
  }
}