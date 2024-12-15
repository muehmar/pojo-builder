package io.github.muehmar.pojobuilder.example.factorymethod;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.Test;

class UrlBuilderTest {
  @Test
  void urlBuilder_when_used_then_correctInstance() throws MalformedURLException {
    final URL url = URLBuilder.fullURLBuilder().spec("http://localhost:8080").build();

    final URL expectedUrl = new URL("http://localhost:8080");

    assertThat(url).isEqualTo(expectedUrl);
  }
}
