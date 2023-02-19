package io.github.muehmar.pojobuilder.generator.impl.gen.safebuilder.model;

import io.github.muehmar.pojobuilder.generator.model.FieldBuilderMethod;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojoextension.annotations.PojoExtension;
import lombok.Value;

@Value
@PojoExtension
public class BuilderFieldWithMethod implements BuilderFieldWithMethodExtension {
  BuilderField builderField;
  FieldBuilderMethod fieldBuilderMethod;

  public IndexedField getIndexedField() {
    return builderField.getIndexedField();
  }

  public Pojo getPojo() {
    return builderField.getPojo();
  }

  public PojoField getField() {
    return builderField.getField();
  }

  public int getIndex() {
    return builderField.getIndex();
  }
}
