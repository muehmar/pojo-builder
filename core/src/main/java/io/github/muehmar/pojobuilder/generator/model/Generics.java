package io.github.muehmar.pojobuilder.generator.model;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.Strings;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Generics {
  PList<Generic> list;

  private Generics(PList<Generic> list) {
    this.list = list;
  }

  public static Generics of(PList<Generic> generics) {
    return new Generics(generics);
  }

  public static Generics of(Generic... generics) {
    return new Generics(PList.fromArray(generics));
  }

  public static Generics empty() {
    return new Generics(PList.empty());
  }

  public String getTypeVariablesFormatted() {
    final String typeVariables = list.map(Generic::getTypeVariable).mkString(", ");
    return Strings.surroundIfNotEmpty("<", typeVariables, ">");
  }

  public String getBoundedTypeVariablesFormatted() {
    final String typeVariables = list.map(Generic::getTypeDeclaration).mkString(", ");
    return Strings.surroundIfNotEmpty("<", typeVariables, ">");
  }

  public PList<Name> getImports() {
    return list.flatMap(Generic::getUpperBounds).flatMap(Type::getImports);
  }

  public PList<Generic> asList() {
    return list;
  }
}
