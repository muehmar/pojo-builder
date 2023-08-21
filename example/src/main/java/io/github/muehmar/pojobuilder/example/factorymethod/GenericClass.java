package io.github.muehmar.pojobuilder.example.factorymethod;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Value;

@Value
public class GenericClass<T extends List<String> & Comparable<T>, S> {
  String prop1;
  T data;
  Optional<S> additionalData;
  Enum<?> code;

  @PojoBuilder
  static <T extends List<String> & Comparable<T>, S, U> GenericClass<T, S> create(
      Supplier<U> creator,
      Function<U, String> prop1Mapper,
      T data,
      Optional<S> additionalData,
      Enum<?> code) {
    return new GenericClass<>(prop1Mapper.apply(creator.get()), data, additionalData, code);
  }
}
