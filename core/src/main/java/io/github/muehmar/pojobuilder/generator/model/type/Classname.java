package io.github.muehmar.pojobuilder.generator.model.type;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Name;
import lombok.EqualsAndHashCode;

/**
 * Name of a class. Could also be an inner class (or static nested class) where it is referenced via
 * its enclosing class(es) like OuterClass.InnerClass.
 */
@EqualsAndHashCode
public class Classname {
  private final PList<Name> outerClassNames;
  private final Name simpleName;

  private Classname(PList<Name> outerClassNames, Name simpleName) {
    this.outerClassNames = outerClassNames;
    this.simpleName = simpleName;
  }

  public static Classname fromString(String fullClassName) {
    final PList<Name> classesReversed =
        PList.fromArray(fullClassName.split("\\.")).map(Name::fromString).reverse();
    if (classesReversed.isEmpty()) {
      throw new IllegalArgumentException("Invalid class name " + fullClassName);
    }

    final Name className = classesReversed.head();
    return new Classname(classesReversed.drop(1).reverse(), className);
  }

  /**
   * Returns the top level class for this class name. This is either the class itself if it's a top
   * level class or the outermost class for an inner class.
   */
  public Name getTopLevelClass() {
    return outerClassNames.headOption().orElse(simpleName);
  }

  public Name getSimpleName() {
    return simpleName;
  }

  public Name asName() {
    return Name.fromString(asString());
  }

  public String asString() {
    return outerClassNames.add(simpleName).mkString(".");
  }

  @Override
  public String toString() {
    return asString();
  }
}
