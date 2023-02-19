package io.github.muehmar.pojobuilder.generator.impl.gen.safebuilder.model;

import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojoextension.annotations.PojoExtension;
import lombok.Value;

@Value
@PojoExtension
public class IndexedField implements IndexedFieldExtension {
  Pojo pojo;
  PojoField field;
  int index;
}
