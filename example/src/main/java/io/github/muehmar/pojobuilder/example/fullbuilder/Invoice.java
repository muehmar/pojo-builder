package io.github.muehmar.pojobuilder.example.fullbuilder;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.util.Optional;
import lombok.Value;

@PojoBuilder
@Value
public class Invoice {
  String prop1;
  Optional<String> prop2;
  int prop3;
}
