package io.github.muehmar.pojobuilder.generator;

import io.github.muehmar.pojobuilder.generator.model.Name;

public class Names {
  private Names() {}

  public static Name id() {
    return Name.fromString("id");
  }

  public static Name zip() {
    return Name.fromString("zip");
  }
}
