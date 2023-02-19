package io.github.muehmar.pojobuilder.generator.impl.gen.instantiation;

import io.github.muehmar.pojobuilder.generator.model.FieldArgument;
import io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation;
import java.util.Objects;

/** Represents */
class FinalConstructorArgument {
  private final String fieldString;
  private final OptionalFieldRelation relation;

  public FinalConstructorArgument(String fieldString, OptionalFieldRelation relation) {
    this.fieldString = fieldString;
    this.relation = relation;
  }

  public static FinalConstructorArgument ofFieldVariable(
      FieldVariable fieldVariable, FieldArgument fieldArgument) {
    return new FinalConstructorArgument(
        fieldVariable.getField().getName().asString(),
        fieldVariable.getRelation().andThen(fieldArgument.getRelation()));
  }

  public String getFieldString() {
    return fieldString;
  }

  public OptionalFieldRelation getRelation() {
    return relation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FinalConstructorArgument finalConstructorArgument = (FinalConstructorArgument) o;
    return Objects.equals(fieldString, finalConstructorArgument.fieldString)
        && relation == finalConstructorArgument.relation;
  }

  @Override
  public int hashCode() {
    return Objects.hash(fieldString, relation);
  }

  @Override
  public String toString() {
    return "Field{" + "fieldString='" + fieldString + '\'' + ", relation=" + relation + '}';
  }
}
