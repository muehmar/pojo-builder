package io.github.muehmar.pojobuilder.generator.model;

import io.github.muehmar.pojobuilder.generator.model.type.Classname;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
public class PojoName {
  Classname classname;

  public static PojoName fromString(String name) {
    return new PojoName(Classname.fromString(name));
  }

  public static PojoName fromClassname(Classname classname) {
    return new PojoName(classname);
  }

  public Name getName() {
    return classname.asName();
  }

  public Name getSimpleName() {
    return classname.getSimpleName();
  }

  @Override
  public String toString() {
    return getName().asString();
  }
}
