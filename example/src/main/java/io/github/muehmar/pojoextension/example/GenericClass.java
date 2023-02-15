package io.github.muehmar.pojoextension.example;

import io.github.muehmar.pojoextension.annotations.SafeBuilder;
import java.util.List;
import java.util.Optional;
import lombok.Value;

@Value
@SafeBuilder
public class GenericClass<T extends List<String> & Comparable<T>, S> {
  String prop1;
  T data;
  Optional<S> additionalData;
  Enum<?> code;
}
