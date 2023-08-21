package io.github.muehmar.pojobuilder.processor;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Generic;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import java.util.List;
import javax.lang.model.element.TypeParameterElement;

/** Processes type variables of classes, this means only upper bounds are considered. */
public class TypeParameterProcessor {
  private TypeParameterProcessor() {}

  static PList<Generic> processTypeParameters(List<? extends TypeParameterElement> typeParameters) {
    return PList.fromIter(typeParameters)
        .map(
            typeParameterElement -> {
              final PList<Type> upperBounds =
                  PList.fromIter(typeParameterElement.getBounds()).map(TypeMirrorMapper::map);
              return new Generic(Name.fromString(typeParameterElement.toString()), upperBounds);
            });
  }
}