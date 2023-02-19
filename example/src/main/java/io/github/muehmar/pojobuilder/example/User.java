package io.github.muehmar.pojobuilder.example;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.util.Optional;
import lombok.Value;

@Value
@PojoBuilder
public class User {
  public static final String USER_LABEL = "label";
  String name;
  Optional<Integer> age;
}
