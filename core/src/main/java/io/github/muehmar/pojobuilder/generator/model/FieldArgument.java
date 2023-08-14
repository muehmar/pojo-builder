package io.github.muehmar.pojobuilder.generator.model;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.util.Optional;
import lombok.Value;

/** Contains a field of the pojo and the matching argument. */
@Value
@PojoBuilder
public class FieldArgument {
  PojoField field;
  Argument argument;
  OptionalFieldRelation relation;

  public static Optional<FieldArgument> fromFieldAndArgument(PojoField field, Argument argument) {
    final Optional<OptionalFieldRelation> optionalFieldRelation =
        argument.getRelationFromField(field);
    return optionalFieldRelation.map(relation -> new FieldArgument(field, argument, relation));
  }

  /** Returns the relation from the nullable field to the argument */
  public OptionalFieldRelation getRelation() {
    return relation;
  }
}
