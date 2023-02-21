package io.github.muehmar.pojobuilder.generator.model;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import lombok.Value;

@Value
@PojoBuilder
public class MatchingConstructor {
  Constructor constructor;
  PList<FieldArgument> fieldArguments;
}
