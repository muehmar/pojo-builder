package io.github.muehmar.pojobuilder.generator;

import static io.github.muehmar.pojobuilder.generator.model.Necessity.OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.REQUIRED;
import static io.github.muehmar.pojobuilder.generator.model.type.Types.string;

import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.type.Types;

public class PojoFields {
  private PojoFields() {}

  public static PojoField requiredId() {
    return new PojoField(Names.id(), Types.integer(), REQUIRED);
  }

  public static PojoField optionalName() {
    return new PojoField(Name.fromString("name"), Types.string(), OPTIONAL);
  }

  public static PojoField requiredMap() {
    return new PojoField(
        Name.fromString("someMap"), Types.map(string(), Types.list(string())), REQUIRED);
  }

  public static Argument toArgument(PojoField f) {
    return new Argument(f.getName(), f.getType());
  }
}
