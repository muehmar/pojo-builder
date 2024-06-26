package io.github.muehmar.pojobuilder.generator.model;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import lombok.Value;

@Value
public class BuildMethod {
  Name name;
  Type returnType;
  PList<QualifiedClassname> exceptions;
}
