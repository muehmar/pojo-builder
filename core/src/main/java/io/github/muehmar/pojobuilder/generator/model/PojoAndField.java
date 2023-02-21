package io.github.muehmar.pojobuilder.generator.model;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import lombok.Value;

@Value
@PojoBuilder
public class PojoAndField {
  Pojo pojo;
  PojoField field;

  public static PojoAndField of(Pojo pojo, PojoField field) {
    return new PojoAndField(pojo, field);
  }
}
