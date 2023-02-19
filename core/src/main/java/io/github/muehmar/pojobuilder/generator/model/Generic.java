package io.github.muehmar.pojobuilder.generator.model;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.Strings;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import io.github.muehmar.pojoextension.annotations.PojoExtension;
import lombok.Value;

@Value
@PojoExtension
public class Generic {
  Name typeVariable;
  PList<Type> upperBounds;

  public Name getTypeDeclaration() {
    final String upperBoundsDeclaration =
        upperBounds.map(Type::getTypeDeclaration).map(Name::asString).mkString(" & ");
    return typeVariable.append(Strings.surroundIfNotEmpty(" extends ", upperBoundsDeclaration, ""));
  }
}
