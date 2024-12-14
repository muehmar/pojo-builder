package io.github.muehmar.pojobuilder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StringsTest {

  @Test
  void surroundIfNotEmpty_when_calledWithEmptyContent_then_returnsEmpty() {
    final String result = Strings.surroundIfNotEmpty("prefix", "", "suffix");
    assertThat(result).isEqualTo("");
  }

  @Test
  void surroundIfNotEmpty_when_calledNonEmptyContent_then_contentPrefixedAndSuffixed() {
    final String result = Strings.surroundIfNotEmpty("prefix", "_content_", "suffix");
    assertThat(result).isEqualTo("prefix_content_suffix");
  }
}
