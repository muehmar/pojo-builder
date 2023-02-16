package io.github.muehmar.pojobuilder.example.custombuildmethod;

import io.github.muehmar.pojobuilder.annotations.BuildMethod;
import io.github.muehmar.pojobuilder.annotations.SafeBuilder;
import java.util.Optional;
import lombok.Value;

@Value
@SafeBuilder
public class CustomBuildMethodClass<T> {
  String prop1;
  String prop2;
  Optional<T> data;

  @BuildMethod
  static <T> String customBuildMethod(CustomBuildMethodClass<T> instance) {
    return instance.toString();
  }
}
