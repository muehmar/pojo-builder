package io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard;

import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_STRING;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_LIST;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.FinalRequiredBuilder.finalRequiredBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import org.junit.jupiter.api.Test;

class FinalRequiredBuilderTest {

  @Test
  void finalRequiredBuilder_when_samplePojo_then_correctClassOutput() {
    final Generator<Pojo, PojoSettings> generator = finalRequiredBuilder();
    final String output =
        generator
            .generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault())
            .asString();

    assertEquals(
        "public static final class Builder2 {\n"
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
            + "}",
        output);
  }

  @Test
  void finalRequiredBuilder_when_genericPojo_then_correctClassOutput() {
    final Generator<Pojo, PojoSettings> generator = finalRequiredBuilder();
    final Writer writer =
        generator.generate(
            Pojos.genericSample(), PojoSettings.defaultSettings(), Writer.createDefault());

    assertEquals(
        "public static final class Builder2<T extends List<String>, S> {\n"
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
            + "}",
        writer.asString());

    assertTrue(writer.getRefs().exists(JAVA_LANG_STRING::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_LIST::equals));
  }
}
