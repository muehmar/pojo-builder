package io.github.muehmar.pojobuilder.generator.impl.gen.builder.model;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import lombok.Value;
import lombok.With;

@Value
@With
@PojoBuilder
public class IndexedField {
  Pojo pojo;
  PojoField field;
  int index;
}
