package io.github.muehmar.pojobuilder.generator.model;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import lombok.Value;

@Value
@PojoBuilder
public class Constructor {
  Name name;
  PList<Argument> arguments;
  PList<QualifiedClassname> exceptions;
}
