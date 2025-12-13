package io.github.muehmar.pojobuilder.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class SampleRecordTest {
  @Test
  void create_when_populateBuilder_then_correctInstanceCreated() {
    final SampleRecord sampleRecord =
        SampleRecordBuilder.create()
            .id(12L)
            .name("name")
            .numbers(List.of(1, 2, 3))
            .andAllOptionals()
            .data("Hello World!")
            .build();

    assertThat(sampleRecord.id()).isEqualTo(12L);
    assertThat(sampleRecord.name()).isEqualTo("name");
    assertThat(sampleRecord.data()).isEqualTo(Optional.of("Hello World!"));
    assertThat(sampleRecord.numbers()).isEqualTo(List.of(1, 2, 3));
  }
}
