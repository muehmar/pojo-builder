package io.github.muehmar.pojoextension.example;

import io.github.muehmar.pojoextension.annotations.SafeBuilder;
import java.util.Optional;
import lombok.Value;

@Value
@SafeBuilder
public class User {
  public static final String USER_LABEL = "label";
  String name;
  Optional<Integer> age;
}
