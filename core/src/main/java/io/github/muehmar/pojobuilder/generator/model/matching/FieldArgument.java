package io.github.muehmar.pojobuilder.generator.model.matching;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import lombok.Value;

/** Contains a field of the pojo and the matching argument. */
@Value
@PojoBuilder
public class FieldArgument {
  PojoField field;
  Argument argument;
  OptionalFieldRelation relation;

  /** Returns the relation from the nullable field to the argument */
  public OptionalFieldRelation getRelation() {
    return relation;
  }
}
