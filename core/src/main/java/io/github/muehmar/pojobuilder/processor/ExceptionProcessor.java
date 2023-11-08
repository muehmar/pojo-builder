package io.github.muehmar.pojobuilder.processor;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.type.ClassnameParser;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import javax.lang.model.element.ExecutableElement;

public class ExceptionProcessor {
  private ExceptionProcessor() {}

  public static PList<QualifiedClassname> process(ExecutableElement executableElement) {
    return PList.fromIter(executableElement.getThrownTypes())
        .map(typeMirror -> ClassnameParser.parseThrowing(typeMirror.toString()));
  }
}
