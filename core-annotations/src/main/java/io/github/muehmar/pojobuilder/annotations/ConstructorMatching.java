package io.github.muehmar.pojobuilder.annotations;

import java.util.Optional;
import java.util.stream.Stream;

/** Defines how a constructor is selected and used by the builder to instantiate a pojo. */
public enum ConstructorMatching {
  /**
   * A constructor matches in case it has the same number of arguments as declared fields in the
   * pojo and the types of each field matches the corresponding type of the argument. The fields and
   * arguments are matched in order of declaration.
   */
  TYPE,
  /**
   * A constructor matches in case it fulfills the condition for {@link ConstructorMatching#TYPE}
   * but additionally the name of all fields and corresponding arguments need to match too.
   */
  TYPE_AND_NAME;

  public static Optional<ConstructorMatching> fromString(String name) {
    return Stream.of(values())
        .filter(constructorMatching -> constructorMatching.name().equals(name))
        .findFirst();
  }
}
