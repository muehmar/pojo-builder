package io.github.muehmar.pojobuilder.generator.model;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojoextension.annotations.PojoExtension;
import lombok.Value;

@Value
@PojoExtension
public class MatchingConstructor implements MatchingConstructorExtension {
  Constructor constructor;
  PList<FieldArgument> fieldArguments;
}
