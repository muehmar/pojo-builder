package io.github.muehmar.pojobuilder.example.safebuilder;

import io.github.muehmar.pojobuilder.annotations.SafeBuilder;
import lombok.Value;

@Value
@SafeBuilder(builderName = "SafeBuilder")
public class BuilderClass {
  String prop1;
  String prop2;
  OuterClass.InnerClass prop3;

  @Value
  static class StringProp {
    String value;
  }
}
