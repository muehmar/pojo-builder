package io.github.muehmar.pojobuilder.generator.model.type;

import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import lombok.Value;

@Value
public class QualifiedClassname {
  Classname classname;
  PackageName pkg;

  public Name getName() {
    return classname.asName();
  }

  public Name getSimpleName() {
    return classname.getSimpleName();
  }

  public String asString() {
    return String.format("%s.%s", pkg.asString(), classname.asString());
  }

  @Override
  public String toString() {
    return asString();
  }
}
