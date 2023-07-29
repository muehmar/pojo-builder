package io.github.muehmar.pojobuilder.annotations;

/** Defines the order of the fields for the full-builder. */
public enum FullBuilderFieldOrder {
  /** The order of the fields in the full-builder is the same as the declaration in the pojo. */
  DECLARATION_ORDER,

  /**
   * The required fields will be used before the optional fields. This corresponds to the order in
   * the standard builder.
   */
  REQUIRED_FIELDS_FIRST
}
