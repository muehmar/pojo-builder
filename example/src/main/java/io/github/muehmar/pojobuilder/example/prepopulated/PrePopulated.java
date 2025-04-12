package io.github.muehmar.pojobuilder.example.prepopulated;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.util.Optional;
import lombok.Value;

@PojoBuilder
@Value
public class PrePopulated {
  String prop1;
  Integer prop2;
  Optional<String> prop3;
  Optional<Integer> prop4;
}
