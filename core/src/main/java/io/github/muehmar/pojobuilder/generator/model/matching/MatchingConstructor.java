package io.github.muehmar.pojobuilder.generator.model.matching;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.Constructor;
import lombok.Value;

@Value
@PojoBuilder
public class MatchingConstructor {
  Constructor constructor;
  PList<FieldArgument> fieldArguments;
}
