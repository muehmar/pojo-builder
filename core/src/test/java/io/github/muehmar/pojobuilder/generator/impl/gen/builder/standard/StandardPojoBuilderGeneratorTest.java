package io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.impl.gen.Refs;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import org.junit.jupiter.api.Test;

class StandardPojoBuilderGeneratorTest {
  @Test
  void standardBuilderGenerator_when_generatorUsedWithSamplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator =
        StandardBuilderGenerator.standardBuilderGenerator();
    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), javaWriter());

    assertEquals(
        "public static final class Builder0 {\n"
            + "  private final Builder builder;\n"
            + "\n"
            + "  private Builder0(Builder builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public Builder1 id(Integer id) {\n"
            + "    return new Builder1(builder.id(id));\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "public static final class Builder1 {\n"
            + "  private final Builder builder;\n"
            + "\n"
            + "  private Builder1(Builder builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public Builder2 username(String username) {\n"
            + "    return new Builder2(builder.username(username));\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "public static final class Builder2 {\n"
            + "  private final Builder builder;\n"
            + "\n"
            + "  private Builder2(Builder builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public OptBuilder0 andAllOptionals() {\n"
            + "    return new OptBuilder0(builder);\n"
            + "  }\n"
            + "\n"
            + "  public Builder andOptionals() {\n"
            + "    return builder;\n"
            + "  }\n"
            + "\n"
            + "  public Customer build() {\n"
            + "    return builder.build();\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "public static final class OptBuilder0 {\n"
            + "  private final Builder builder;\n"
            + "\n"
            + "  private OptBuilder0(Builder builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public OptBuilder1 nickname(String nickname) {\n"
            + "    return new OptBuilder1(builder.nickname(nickname));\n"
            + "  }\n"
            + "\n"
            + "  public OptBuilder1 nickname(Optional<String> nickname) {\n"
            + "    return new OptBuilder1(builder.nickname(nickname));\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "public static final class OptBuilder1 {\n"
            + "  private final Builder builder;\n"
            + "\n"
            + "  private OptBuilder1(Builder builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public Customer build() {\n"
            + "    return builder.build();\n"
            + "  }\n"
            + "}",
        writer.asString());

    assertTrue(writer.getRefs().exists(Refs.JAVA_LANG_INTEGER::equals));
    assertTrue(writer.getRefs().exists(Refs.JAVA_LANG_STRING::equals));
    assertTrue(writer.getRefs().exists(Refs.JAVA_UTIL_OPTIONAL::equals));
  }

  @Test
  void standardBuilderGenerator_when_samplePojoAndBuilderSetMethodPrefix_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator =
        StandardBuilderGenerator.standardBuilderGenerator();
    final Writer writer =
        generator.generate(
            Pojos.sample(),
            PojoSettings.defaultSettings().withBuilderSetMethodPrefixOpt(Name.fromString("set")),
            javaWriter());

    assertEquals(
        "public static final class Builder0 {\n"
            + "  private final Builder builder;\n"
            + "\n"
            + "  private Builder0(Builder builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public Builder1 setId(Integer id) {\n"
            + "    return new Builder1(builder.setId(id));\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "public static final class Builder1 {\n"
            + "  private final Builder builder;\n"
            + "\n"
            + "  private Builder1(Builder builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public Builder2 setUsername(String username) {\n"
            + "    return new Builder2(builder.setUsername(username));\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "public static final class Builder2 {\n"
            + "  private final Builder builder;\n"
            + "\n"
            + "  private Builder2(Builder builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public OptBuilder0 andAllOptionals() {\n"
            + "    return new OptBuilder0(builder);\n"
            + "  }\n"
            + "\n"
            + "  public Builder andOptionals() {\n"
            + "    return builder;\n"
            + "  }\n"
            + "\n"
            + "  public Customer build() {\n"
            + "    return builder.build();\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "public static final class OptBuilder0 {\n"
            + "  private final Builder builder;\n"
            + "\n"
            + "  private OptBuilder0(Builder builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public OptBuilder1 setNickname(String nickname) {\n"
            + "    return new OptBuilder1(builder.setNickname(nickname));\n"
            + "  }\n"
            + "\n"
            + "  public OptBuilder1 setNickname(Optional<String> nickname) {\n"
            + "    return new OptBuilder1(builder.setNickname(nickname));\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "public static final class OptBuilder1 {\n"
            + "  private final Builder builder;\n"
            + "\n"
            + "  private OptBuilder1(Builder builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public Customer build() {\n"
            + "    return builder.build();\n"
            + "  }\n"
            + "}",
        writer.asString());

    assertTrue(writer.getRefs().exists(Refs.JAVA_LANG_INTEGER::equals));
    assertTrue(writer.getRefs().exists(Refs.JAVA_LANG_STRING::equals));
    assertTrue(writer.getRefs().exists(Refs.JAVA_UTIL_OPTIONAL::equals));
  }

  @Test
  void standardBuilderGenerator_when_genericPojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator =
        StandardBuilderGenerator.standardBuilderGenerator();
    final Writer writer =
        generator.generate(Pojos.genericSample(), PojoSettings.defaultSettings(), javaWriter());

    assertEquals(
        "public static final class Builder0<T extends List<String>, S> {\n"
            + "  private final Builder<T, S> builder;\n"
            + "\n"
            + "  private Builder0(Builder<T, S> builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public Builder1<T, S> id(String id) {\n"
            + "    return new Builder1<>(builder.id(id));\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "public static final class Builder1<T extends List<String>, S> {\n"
            + "  private final Builder<T, S> builder;\n"
            + "\n"
            + "  private Builder1(Builder<T, S> builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public Builder2<T, S> data(T data) {\n"
            + "    return new Builder2<>(builder.data(data));\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "public static final class Builder2<T extends List<String>, S> {\n"
            + "  private final Builder<T, S> builder;\n"
            + "\n"
            + "  private Builder2(Builder<T, S> builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public OptBuilder0<T, S> andAllOptionals() {\n"
            + "    return new OptBuilder0<>(builder);\n"
            + "  }\n"
            + "\n"
            + "  public Builder<T, S> andOptionals() {\n"
            + "    return builder;\n"
            + "  }\n"
            + "\n"
            + "  public Customer<T, S> build() {\n"
            + "    return builder.build();\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "public static final class OptBuilder0<T extends List<String>, S> {\n"
            + "  private final Builder<T, S> builder;\n"
            + "\n"
            + "  private OptBuilder0(Builder<T, S> builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public OptBuilder1<T, S> additionalData(S additionalData) {\n"
            + "    return new OptBuilder1<>(builder.additionalData(additionalData));\n"
            + "  }\n"
            + "\n"
            + "  public OptBuilder1<T, S> additionalData(Optional<S> additionalData) {\n"
            + "    return new OptBuilder1<>(builder.additionalData(additionalData));\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "public static final class OptBuilder1<T extends List<String>, S> {\n"
            + "  private final Builder<T, S> builder;\n"
            + "\n"
            + "  private OptBuilder1(Builder<T, S> builder) {\n"
            + "    this.builder = builder;\n"
            + "  }\n"
            + "\n"
            + "  public Customer<T, S> build() {\n"
            + "    return builder.build();\n"
            + "  }\n"
            + "}",
        writer.asString());

    assertTrue(writer.getRefs().exists(Refs.JAVA_UTIL_LIST::equals));
    assertTrue(writer.getRefs().exists(Refs.JAVA_LANG_STRING::equals));
    assertTrue(writer.getRefs().exists(Refs.JAVA_UTIL_OPTIONAL::equals));
  }

  @Test
  void standardBuilderGenerator_when_disabledStandardBuilder_then_noOutput() {
    final Generator<Pojo, PojoSettings> generator =
        StandardBuilderGenerator.standardBuilderGenerator();
    final Writer writer =
        generator.generate(
            Pojos.sample(),
            PojoSettings.defaultSettings().withStandardBuilderEnabled(false),
            javaWriter());

    assertEquals("", writer.asString());
  }
}
