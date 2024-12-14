package io.github.muehmar.pojobuilder.generator.model.type;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.muehmar.pojobuilder.generator.model.PackageName;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ClassnameParserTest {

  @Test
  void parse_when_javaLangString_then_parsedCorrectly() {
    final Optional<QualifiedClassname> parse = ClassnameParser.parse("java.lang.String");

    assertThat(parse)
        .isPresent()
        .hasValueSatisfying(
            className -> {
              assertThat(className.getClassname()).isEqualTo(Classname.fromString("String"));
              assertThat(className.getPkg()).isEqualTo(PackageName.javaLang());
            });
  }

  @Test
  void parse_when_genericClass_then_parsedCorrectlyWithoutTypeParameter() {
    final Optional<QualifiedClassname> parse =
        ClassnameParser.parse("java.util.Optional<java.lang.String>");

    assertThat(parse)
        .isPresent()
        .hasValueSatisfying(
            className -> {
              assertThat(className.getClassname()).isEqualTo(Classname.fromString("Optional"));
              assertThat(className.getPkg()).isEqualTo(PackageName.javaUtil());
            });
  }

  @Test
  void parse_when_innerClassName_then_parsedCorrectly() {
    final Optional<QualifiedClassname> parse =
        ClassnameParser.parse("io.github.muehmar.Customer.Address");

    assertThat(parse)
        .isPresent()
        .hasValueSatisfying(
            className -> {
              assertThat(className.getClassname())
                  .isEqualTo(Classname.fromString("Customer.Address"));
              assertThat(className.getPkg()).isEqualTo(PackageName.fromString("io.github.muehmar"));
            });
  }

  @ParameterizedTest
  @ValueSource(strings = {"1Invalid", "org.123.Customer", "Customer"})
  void parse_when_invalidClassName_then_returnEmpty(String input) {
    final Optional<QualifiedClassname> result = ClassnameParser.parse(input);
    assertThat(result).isEmpty();
  }
}
