package io.github.muehmar.pojobuilder.generator.model;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.type.Classname;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import lombok.Value;

@PojoBuilder
@Value
public class FactoryMethod {
  Classname ownerClassname;
  PackageName pkg;
  Name methodName;
  PList<Argument> arguments;
  PList<QualifiedClassname> exceptions;
}
