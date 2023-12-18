package io.github.muehmar.pojobuilder.example;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import jakarta.annotation.Nullable;
import lombok.Value;

@PojoBuilder
@Value
public class Patient {
  @Nullable String firstName;
  String lastName;
}
