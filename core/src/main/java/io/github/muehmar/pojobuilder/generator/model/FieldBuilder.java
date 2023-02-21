package io.github.muehmar.pojobuilder.generator.model;

import ch.bluecare.commons.data.NonEmptyList;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import lombok.Value;

@Value
@PojoBuilder
public class FieldBuilder {
  boolean disableDefaultMethods;
  NonEmptyList<FieldBuilderMethod> methods;

  public Name getFieldName() {
    return methods.head().getFieldName();
  }
}
