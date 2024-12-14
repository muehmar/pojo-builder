package io.github.muehmar.pojobuilder.generator.impl.gen;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_INTEGER;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_STRING;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_MAP;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.REQUIRED;
import static io.github.muehmar.pojobuilder.generator.model.type.Types.integer;
import static io.github.muehmar.pojobuilder.generator.model.type.Types.string;
import static org.assertj.core.api.Assertions.assertThat;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaModifier;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FieldDeclarationGenTest {

  @ParameterizedTest
  @MethodSource("privateAndFinalModifierUnordered")
  void generate_when_privateAndFinalModifierUnordered_then_correctOutputWithOrderedModifiers(
      PList<JavaModifier> modifiers) {
    final Generator<PojoField, PojoSettings> generator =
        FieldDeclarationGen.ofModifiers(modifiers.toArray(JavaModifier.class));
    final Writer writer =
        generator.generate(
            new PojoField(Name.fromString("someMap"), Types.map(string(), integer()), REQUIRED),
            PojoSettings.defaultSettings(),
            javaWriter());

    assertThat(writer.getRefs().exists(JAVA_LANG_STRING::equals)).isTrue();
    assertThat(writer.getRefs().exists(JAVA_LANG_INTEGER::equals)).isTrue();
    assertThat(writer.getRefs().exists(JAVA_UTIL_MAP::equals)).isTrue();

    assertThat(writer.asString()).isEqualTo("private final Map<String,Integer> someMap;");
  }

  private static Stream<Arguments> privateAndFinalModifierUnordered() {
    return Stream.of(
        Arguments.of(PList.of(JavaModifier.FINAL, JavaModifier.PRIVATE)),
        Arguments.of(PList.of(JavaModifier.PRIVATE, JavaModifier.FINAL)));
  }
}
