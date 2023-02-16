package io.github.muehmar.pojobuilder.example;

import io.github.muehmar.pojobuilder.annotations.SafeBuilder;
import java.util.Optional;
import lombok.Value;

@Value
@SafeBuilder
public class User {
  public static final String USER_LABEL = "label";
  String name;
  Optional<Integer> age;
}
