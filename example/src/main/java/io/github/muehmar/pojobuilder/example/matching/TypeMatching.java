package io.github.muehmar.pojobuilder.example.matching;

import io.github.muehmar.pojobuilder.annotations.ConstructorMatching;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.util.Optional;

@PojoBuilder(constructorMatching = ConstructorMatching.TYPE)
public class TypeMatching {
  private final String name;
  private final Integer age;
  private final Optional<String> email;

  // Only types need to match, the names can be different
  public TypeMatching(String name_, Integer age_, Optional<String> email_) {
    this.name = name_;
    this.age = age_;
    this.email = email_;
  }
}
