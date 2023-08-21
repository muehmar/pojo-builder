package io.github.muehmar.pojobuilder.generator.model;

public enum OptionalFieldRelation {
  SAME_TYPE,
  WRAP_INTO_OPTIONAL;

  public boolean isSameType() {
    return this == SAME_TYPE;
  }

  public boolean isWrapIntoOptional() {
    return this == WRAP_INTO_OPTIONAL;
  }
}
