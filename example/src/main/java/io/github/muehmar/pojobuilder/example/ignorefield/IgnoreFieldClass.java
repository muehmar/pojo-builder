package io.github.muehmar.pojobuilder.example.ignorefield;

import io.github.muehmar.pojobuilder.annotations.Ignore;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@PojoBuilder
@EqualsAndHashCode
@Getter
public class IgnoreFieldClass {
  private final String id;
  private final String name;
  @Ignore private final String deviated;

  public IgnoreFieldClass(String id, String name) {
    this.id = id;
    this.name = name;
    this.deviated = String.format("%s-%s", id, name);
  }
}
