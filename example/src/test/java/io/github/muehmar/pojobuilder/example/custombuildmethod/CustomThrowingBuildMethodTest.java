package io.github.muehmar.pojobuilder.example.custombuildmethod;

import static io.github.muehmar.pojobuilder.example.custombuildmethod.CustomThrowingBuildMethodBuilder.fullCustomThrowingBuildMethodBuilder;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;
import org.junit.jupiter.api.Test;

class CustomThrowingBuildMethodTest {

  @Test
  void build_when_calledWithCorrectUrlAndUri_then_doesNotThrow() {
    assertDoesNotThrow(
        () -> fullCustomThrowingBuildMethodBuilder().url("http://www.google.com").build());
  }

  @Test
  void build_when_calledWithIncorrectUrl_then_throwsMalformedURLException() {
    assertThrows(
        MalformedURLException.class,
        () -> fullCustomThrowingBuildMethodBuilder().url("google.com").build());
  }
}
