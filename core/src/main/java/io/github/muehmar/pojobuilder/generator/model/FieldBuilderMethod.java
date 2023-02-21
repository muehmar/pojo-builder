package io.github.muehmar.pojobuilder.generator.model;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import java.util.Optional;
import lombok.Value;
import lombok.With;

@Value
@With
@PojoBuilder
public class FieldBuilderMethod {
  Name fieldName;
  Optional<Name> innerClassName;
  Name methodName;
  Type returnType;
  PList<Argument> arguments;

  public PList<Name> getArgumentNames() {
    return arguments.map(Argument::getName);
  }
}
