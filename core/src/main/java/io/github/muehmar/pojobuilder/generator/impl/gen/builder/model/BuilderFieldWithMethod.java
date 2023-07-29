package io.github.muehmar.pojobuilder.generator.impl.gen.builder.model;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.FieldBuilderMethod;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import lombok.Value;

@Value
@PojoBuilder
public class BuilderFieldWithMethod {
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
