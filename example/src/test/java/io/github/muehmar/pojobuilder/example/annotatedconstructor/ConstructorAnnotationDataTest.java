package io.github.muehmar.pojobuilder.example.annotatedconstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    assertEquals("name", constructorAnnotationData.getName());
    assertEquals(Optional.of(42), constructorAnnotationData.getAge());
    assertEquals("data", constructorAnnotationData.getData());
  }
}
