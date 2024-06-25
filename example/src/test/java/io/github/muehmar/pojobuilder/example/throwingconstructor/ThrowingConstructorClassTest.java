package io.github.muehmar.pojobuilder.example.throwingconstructor;

import static io.github.muehmar.pojobuilder.example.throwingconstructor.ThrowingConstructorClassBuilder.fullThrowingConstructorClassBuilder;
import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
import org.junit.jupiter.api.Test;

class ThrowingConstructorClassTest {

  @Test
  void build_when_invalidUrl_then_throws() {
    assertThrows(
        MalformedURLException.class,
        () -> fullThrowingConstructorClassBuilder().prop1("prop1").build());
  }

  @Test
  void build_when_validUrl_then_doesNotThrow() {
    assertDoesNotThrow(
        () -> fullThrowingConstructorClassBuilder().prop1("http://example.com").build());
  }
}
