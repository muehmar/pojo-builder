package io.github.muehmar.pojobuilder.processor;

import static io.github.muehmar.pojobuilder.Booleans.not;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Generic;
import io.github.muehmar.pojobuilder.generator.model.Generics;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import java.util.List;
import javax.lang.model.element.TypeParameterElement;

/** Processes type variables of classes, this means only upper bounds are considered. */
public class TypeParameterProcessor {
  private TypeParameterProcessor() {}

  static Generics processTypeParameters(List<? extends TypeParameterElement> typeParameters) {
    final PList<Generic> generics =
        PList.fromIter(typeParameters)
            .map(
                typeParameterElement -> {
                  final PList<Type> upperBounds =
                      PList.fromIter(typeParameterElement.getBounds())
                          .map(TypeMirrorMapper::map)
                          .filter(type -> not(type.equals(Types.object())));
                  return new Generic(Name.fromString(typeParameterElement.toString()), upperBounds);
                });
    return Generics.of(generics);
  }
}
