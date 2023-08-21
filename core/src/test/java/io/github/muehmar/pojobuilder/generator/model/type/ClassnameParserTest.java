package io.github.muehmar.pojobuilder.generator.model.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.muehmar.pojobuilder.generator.model.PackageName;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ClassnameParserTest {

  @Test
  void parse_when_javaLangString_then_parsedCorrectly() {
    final Optional<QualifiedClassname> parse = ClassnameParser.parse("java.lang.String");
    assertTrue(parse.isPresent());
    assertEquals(Classname.fromString("String"), parse.get().getClassname());
    assertEquals(PackageName.javaLang(), parse.get().getPkg());
  }

  @Test
  void parse_when_genericClass_then_parsedCorrectlyWithoutTypeParameter() {
    final Optional<QualifiedClassname> parse =
        ClassnameParser.parse("java.util.Optional<java.lang.String>");
    assertTrue(parse.isPresent());
    assertEquals(Classname.fromString("Optional"), parse.get().getClassname());
    assertEquals(PackageName.javaUtil(), parse.get().getPkg());
  }

  @Test
  void parse_when_innerClassName_then_parsedCorrectly() {
    final Optional<QualifiedClassname> parse =
        ClassnameParser.parse("io.github.muehmar.Customer.Address");
    assertTrue(parse.isPresent());
    assertEquals(Classname.fromString("Customer.Address"), parse.get().getClassname());
    assertEquals(PackageName.fromString("io.github.muehmar"), parse.get().getPkg());
  }

  @ParameterizedTest
  @ValueSource(strings = {"1Invalid", "org.123.Customer", "Customer"})
  void parse_when_invalidClassName_then_returnEmpty(String input) {
    final Optional<QualifiedClassname> result = ClassnameParser.parse(input);
    assertFalse(result.isPresent(), "Parsed to " + result);
  }
}
