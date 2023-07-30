package io.github.muehmar.pojobuilder.example.standardbuilder;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import lombok.Value;

@Value
@PojoBuilder(builderName = "SafeBuilder")
public class BuilderClass {
  String prop1;
  String prop2;
  OuterClass.InnerClass prop3;

  @Value
  static class StringProp {
    String value;
  }
}
