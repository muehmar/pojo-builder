package io.github.muehmar.pojobuilder.example;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.util.List;
import java.util.Optional;
import lombok.Value;

@Value
@PojoBuilder
public class GenericClass<T extends List<String> & Comparable<T>, S> {
  String prop1;
  T data;
  Optional<S> additionalData;
  Enum<?> code;
}
