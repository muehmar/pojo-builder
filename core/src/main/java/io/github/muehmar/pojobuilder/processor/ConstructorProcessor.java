package io.github.muehmar.pojobuilder.processor;

import static io.github.muehmar.pojobuilder.Booleans.not;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.exception.PojoBuilderException;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.Constructor;
import io.github.muehmar.pojobuilder.generator.model.Name;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

public class ConstructorProcessor {
  private ConstructorProcessor() {}

  public static PList<Constructor> process(Element pojo) {

    return PList.fromIter(pojo.getEnclosedElements())
        .filter(e -> e.getKind().equals(ElementKind.CONSTRUCTOR) && e instanceof ExecutableElement)
        .map(ExecutableElement.class::cast)
        .filter(e -> not(e.getModifiers().contains(Modifier.PRIVATE)))
        .map(e -> processConstructor(Name.fromString(pojo.getSimpleName().toString()), e));
  }

  public static Constructor processConstructor(
      Name constructorName, ExecutableElement constructor) {
    if (not(constructor.getKind().equals(ElementKind.CONSTRUCTOR))) {
      final String message =
          String.format(
              "Element %s is not a constructor and cannot be processed. This is likely a programming error, please report an issue with some sample code.",
              constructor.getSimpleName());
      throw new PojoBuilderException(message);
    }
    final PList<Argument> arguments =
        PList.fromIter(constructor.getParameters()).map(ArgumentMapper::toArgument);

    return new Constructor(constructorName, arguments);
  }
}
