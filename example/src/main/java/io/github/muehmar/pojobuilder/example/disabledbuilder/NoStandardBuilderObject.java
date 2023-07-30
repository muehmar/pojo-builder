package io.github.muehmar.pojobuilder.example.disabledbuilder;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import lombok.Value;

@Value
@PojoBuilder(enableStandardBuilder = false)
public class NoStandardBuilderObject {
  String prop1;
}
