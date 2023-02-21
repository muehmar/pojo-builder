package io.github.muehmar.pojobuilder.generator.model;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import lombok.Value;

@Value
@PojoBuilder
public class FieldArgument {
  PojoField field;
  Argument argument;
  OptionalFieldRelation relation;

  /** Returns the relation from the field to the argument */
  public OptionalFieldRelation getRelation() {
    return relation;
  }
}
