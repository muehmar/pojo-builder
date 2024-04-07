package io.github.muehmar.pojobuilder.example.annotatedconstructor;

import io.github.muehmar.pojobuilder.annotations.Nullable;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.util.Optional;
import lombok.Value;

@Value
public class ConstructorAnnotationData<T> {
  String name;
  Optional<Integer> age;
  T data;

  @PojoBuilder
  public ConstructorAnnotationData(String name, @Nullable Integer age, T data) {
    this.name = name;
    this.age = Optional.ofNullable(age);
    this.data = data;
  }
}
