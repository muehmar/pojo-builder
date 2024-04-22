package io.github.muehmar.pojobuilder.example.matching;

import io.github.muehmar.pojobuilder.annotations.ConstructorMatching;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.util.Optional;

@PojoBuilder(constructorMatching = ConstructorMatching.TYPE_AND_NAME)
public class TypeAndNameMatching {
  private final String name;
  private final Integer age;
  private final Optional<String> email;

  // Names need to match, otherwise the processor throws an exception
  public TypeAndNameMatching(String name, Integer age, Optional<String> email) {
    this.name = name;
    this.age = age;
    this.email = email;
  }
}
