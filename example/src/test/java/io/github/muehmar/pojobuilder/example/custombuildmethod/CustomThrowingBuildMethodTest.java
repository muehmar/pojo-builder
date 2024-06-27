package io.github.muehmar.pojobuilder.example.custombuildmethod;

import static io.github.muehmar.pojobuilder.example.custombuildmethod.CustomThrowingBuildMethodBuilder.fullCustomThrowingBuildMethodBuilder;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.Test;

class CustomThrowingBuildMethodTest {

  @Test
  void build_when_calledWithCorrectUrlAndUri_then_doesNotThrow() {
    final URL url =
        assertDoesNotThrow(
            () -> fullCustomThrowingBuildMethodBuilder().url("http://www.google.com").build());

    assertEquals("http://www.google.com", url.toString());
  }

  @Test
  void build_when_calledWithIncorrectUrl_then_throwsMalformedURLException() {
    assertThrows(
        MalformedURLException.class,
        () -> fullCustomThrowingBuildMethodBuilder().url("google.com").build());
  }
}
