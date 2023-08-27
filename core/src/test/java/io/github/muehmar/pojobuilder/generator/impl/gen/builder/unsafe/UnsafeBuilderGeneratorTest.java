package io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import org.junit.jupiter.api.Test;

class UnsafeBuilderGeneratorTest {

  @Test
  void builderClass_when_generatorUsedWithSamplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = UnsafeBuilderGenerator.unsafeBuilderGenerator();
    final String output =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), javaWriter()).asString();

    assertEquals(
        "public static final class Builder {\n"
            + "  private Builder() {\n"
            + "  }\n"
            + "\n"
            + "  private Integer id;\n"
            + "  private String username;\n"
            + "  private String nickname;\n"
            + "\n"
            + "  private Builder id(Integer id) {\n"
            + "    this.id = id;\n"
            + "    return this;\n"
            + "  }\n"
            + "\n"
            + "  private Builder username(String username) {\n"
            + "    this.username = username;\n"
            + "    return this;\n"
            + "  }\n"
            + "\n"
            + "  public Builder nickname(String nickname) {\n"
            + "    this.nickname = nickname;\n"
            + "    return this;\n"
            + "  }\n"
            + "\n"
            + "  public Builder nickname(Optional<String> nickname) {\n"
            + "    this.nickname = nickname.orElse(null);\n"
            + "    return this;\n"
            + "  }\n"
            + "\n"
            + "  public Customer build() {\n"
            + "    final Customer instance =\n"
            + "        new Customer(id, username, nickname);\n"
            + "    return instance;\n"
            + "  }\n"
            + "}",
        output);
  }

  @Test
  void builderClass_when_generatorUsedWithGenericSamplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = UnsafeBuilderGenerator.unsafeBuilderGenerator();
    final String output =
        generator
            .generate(Pojos.genericSample(), PojoSettings.defaultSettings(), javaWriter())
            .asString();

    assertEquals(
        "public static final class Builder<T extends List<String>, S> {\n"
            + "  private Builder() {\n"
            + "  }\n"
            + "\n"
            + "  private String id;\n"
            + "  private T data;\n"
            + "  private S additionalData;\n"
            + "\n"
            + "  private Builder<T, S> id(String id) {\n"
            + "    this.id = id;\n"
            + "    return this;\n"
            + "  }\n"
            + "\n"
            + "  private Builder<T, S> data(T data) {\n"
            + "    this.data = data;\n"
            + "    return this;\n"
            + "  }\n"
            + "\n"
            + "  public Builder<T, S> additionalData(S additionalData) {\n"
            + "    this.additionalData = additionalData;\n"
            + "    return this;\n"
            + "  }\n"
            + "\n"
            + "  public Builder<T, S> additionalData(Optional<S> additionalData) {\n"
            + "    this.additionalData = additionalData.orElse(null);\n"
            + "    return this;\n"
            + "  }\n"
            + "\n"
            + "  public Customer<T, S> build() {\n"
            + "    final Customer<T, S> instance =\n"
            + "        new Customer<T, S>(id, data, additionalData);\n"
            + "    return instance;\n"
            + "  }\n"
            + "}",
        output);
  }
}
