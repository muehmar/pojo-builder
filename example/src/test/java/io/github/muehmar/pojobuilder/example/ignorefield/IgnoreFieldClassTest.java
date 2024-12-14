package io.github.muehmar.pojobuilder.example.ignorefield;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class IgnoreFieldClassTest {

  @Test
  void safeBuilderUsed_when_constructed_then_correctInstanceCreated() {
    final IgnoreFieldClass ignoreFieldClass =
        IgnoreFieldClassBuilder.create().id("1234").name("Martin").andAllOptionals().build();

    assertThat(ignoreFieldClass.getId()).isEqualTo("1234");
    assertThat(ignoreFieldClass.getName()).isEqualTo("Martin");
    assertThat(ignoreFieldClass.getDeviated()).isEqualTo("1234-Martin");
  }
}
