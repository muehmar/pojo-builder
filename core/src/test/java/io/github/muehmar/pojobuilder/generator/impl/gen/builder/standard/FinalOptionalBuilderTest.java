package io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard;

import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_STRING;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_LIST;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.FinalOptionalBuilder.finalOptionalBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import org.junit.jupiter.api.Test;

class FinalOptionalBuilderTest {

  @Test
  void finalOptionalBuilder_when_samplePojo_then_correctClassOutput() {
    final Generator<Pojo, PojoSettings> generator = finalOptionalBuilder();
    final String output =
        generator
            .generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault())
            .asString();

    assertEquals(
        "public static final class OptBuilder1 {\n"
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
        output);
  }

  @Test
  void finalOptionalBuilder_when_genericPojo_then_correctClassOutput() {
    final Generator<Pojo, PojoSettings> generator = finalOptionalBuilder();
    final Writer writer =
        generator.generate(
            Pojos.genericSample(), PojoSettings.defaultSettings(), Writer.createDefault());

    assertEquals(
        "public static final class OptBuilder1<T extends List<String>, S> {\n"
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

    assertTrue(writer.getRefs().exists(JAVA_LANG_STRING::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_LIST::equals));
  }
}
