package io.github.muehmar.pojobuilder.example.disabledbuilder;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import lombok.Value;

@Value
@PojoBuilder(enableFullBuilder = false)
public class NoFullBuilderObject {
  String prop1;
}
