package io.github.muehmar.pojobuilder.example.annotatedconstructor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class ConstructorAnnotationDataTest {

  @Test
  void builder_when_used_then_allAttributesSet() {
    final ConstructorAnnotationData<String> constructorAnnotationData =
        ConstructorAnnotationDataBuilder.<String>constructorAnnotationDataBuilder()
            .name("name")
            .data("data")
            .andAllOptionals()
            .age(Optional.of(42))
            .build();

    assertThat(constructorAnnotationData.getName()).isEqualTo("name");
    assertThat(constructorAnnotationData.getAge()).isEqualTo(Optional.of(42));
    assertThat(constructorAnnotationData.getData()).isEqualTo("data");
  }
}
