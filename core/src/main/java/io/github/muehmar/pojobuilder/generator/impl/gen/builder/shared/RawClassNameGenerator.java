package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import io.github.muehmar.pojobuilder.generator.impl.gen.builder.model.IndexedField;

public interface RawClassNameGenerator {

  String forFieldIndex(int index);

  default String forField(IndexedField indexedField) {
    return forFieldIndex(indexedField.getIndex());
  }
}
