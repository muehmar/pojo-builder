package io.github.muehmar.pojobuilder.generator.model;

import io.github.muehmar.pojobuilder.generator.model.type.Type;
import lombok.Value;

@Value
public class BuildMethod {
  Name name;
  Type returnType;
}
