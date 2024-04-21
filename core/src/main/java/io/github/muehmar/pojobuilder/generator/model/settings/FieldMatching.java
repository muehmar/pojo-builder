package io.github.muehmar.pojobuilder.generator.model.settings;

import io.github.muehmar.pojobuilder.annotations.ConstructorMatching;

/**
 * Defines how fields of the pojo should be matched against arguments, i.e. the detection which
 * fields corresponds to which argument.
 */
public enum FieldMatching {
  TYPE,
  TYPE_AND_NAME;

  public static FieldMatching fromConstructorMatching(ConstructorMatching constructorMatching) {
    switch (constructorMatching) {
      case TYPE:
        return FieldMatching.TYPE;
      case TYPE_AND_NAME:
        return FieldMatching.TYPE_AND_NAME;
    }
    throw new IllegalArgumentException(
        "Unknown/unhandled constructor matching: " + constructorMatching);
  }
}
