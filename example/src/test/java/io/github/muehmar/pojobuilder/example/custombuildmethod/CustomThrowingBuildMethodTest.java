package io.github.muehmar.pojobuilder.example.custombuildmethod;

import static io.github.muehmar.pojobuilder.example.custombuildmethod.CustomThrowingBuildMethodBuilder.fullCustomThrowingBuildMethodBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.Test;

class CustomThrowingBuildMethodTest {

  @Test
  void build_when_calledWithCorrectUrlAndUri_then_doesNotThrow() {
    assertThatNoException()
        .isThrownBy(
            () -> {
              final URL url =
                  fullCustomThrowingBuildMethodBuilder().url("http://www.google.com").build();
              assertThat(url.toString()).isEqualTo("http://www.google.com");
            });
  }

  @Test
  void build_when_calledWithIncorrectUrl_then_throwsMalformedURLException() {
    assertThatExceptionOfType(MalformedURLException.class)
        .isThrownBy(() -> fullCustomThrowingBuildMethodBuilder().url("google.com").build());
  }
}
