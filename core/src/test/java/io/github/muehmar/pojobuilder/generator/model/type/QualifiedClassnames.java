package io.github.muehmar.pojobuilder.generator.model.type;

import io.github.muehmar.pojobuilder.generator.model.PackageName;

public class QualifiedClassnames {
  private QualifiedClassnames() {}

  public static QualifiedClassname ioException() {
    return new QualifiedClassname(
        Classname.fromString("IOException"), PackageName.fromString("java.io"));
  }

  public static QualifiedClassname malformedUrlException() {
    return new QualifiedClassname(
        Classname.fromString("MalformedURLException"), PackageName.fromString("java.net"));
  }
}
