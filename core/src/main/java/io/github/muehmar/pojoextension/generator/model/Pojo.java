package io.github.muehmar.pojoextension.generator.model;

import static io.github.muehmar.pojoextension.Booleans.not;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojoextension.Strings;
import io.github.muehmar.pojoextension.annotations.PojoExtension;
import io.github.muehmar.pojoextension.exception.PojoBuilderException;
import io.github.muehmar.pojoextension.generator.model.type.Type;
import java.util.Optional;
import lombok.Value;

@Value
@PojoExtension
public class Pojo implements io.github.muehmar.pojoextension.generator.model.PojoExtension {
  private static final PList<Name> LETTERS_AZ =
      PList.range(65, 91).map(n -> Character.toString((char) n.intValue())).map(Name::fromString);

  Name name;
  PackageName pkg;
  PList<PojoField> fields;
  PList<Constructor> constructors;
  PList<Generic> generics;
  PList<FieldBuilder> fieldBuilders;
  Optional<BuildMethod> buildMethod;

  @io.github.muehmar.pojoextension.annotations.Getter("pkg")
  public PackageName getPackage() {
    return pkg;
  }

  public PList<PojoAndField> getPojoAndFields() {
    return fields.map(f -> new PojoAndField(this, f));
  }

  public PList<Name> getGenericImports() {
    return generics.flatMap(Generic::getUpperBounds).flatMap(Type::getImports);
  }

  public String getDiamond() {
    return generics.nonEmpty() ? "<>" : "";
  }

  public PList<String> getGenericTypeDeclarations() {
    return generics.map(Generic::getTypeDeclaration).map(Name::asString);
  }

  public String getGenericTypeDeclarationSection() {
    return Strings.surroundIfNotEmpty("<", getGenericTypeDeclarations().mkString(", "), ">");
  }

  public String getTypeVariablesSection() {
    return Strings.surroundIfNotEmpty(
        "<", generics.map(Generic::getTypeVariable).mkString(", "), ">");
  }

  public Name getNameWithTypeVariables() {
    return name.append(getTypeVariablesSection());
  }

  public String getTypeVariablesWildcardSection() {
    return Strings.surroundIfNotEmpty("<", generics.map(ignore -> "?").mkString(", "), ">");
  }

  public Optional<MatchingConstructor> findMatchingConstructor() {
    return constructors
        .flatMapOptional(c -> c.matchFields(fields).map(f -> new MatchingConstructor(c, f)))
        .headOption();
  }

  public MatchingConstructor getMatchingConstructorOrThrow() {
    return findMatchingConstructor()
        .orElseThrow(() -> new PojoBuilderException(noMatchingConstructorMessage()));
  }

  private String noMatchingConstructorMessage() {
    return String.format(
        "No matching constructor found for class/record %s."
            + " A constructor should have all the fields as arguments in the order of declaration and matching type,"
            + " where the actual type of a non-required field can be wrapped into an java.util.Optional. Furthermore"
            + "it should be accessible from within the same package, i.e. at least package-private. If a field is"
            + "instantiated in the constructor and not part of the arguments, you can annotate it with @Ignore.",
        getName());
  }

  public Name findUnusedTypeVariableName() {
    final PList<Name> typeVariableNames = generics.map(Generic::getTypeVariable);
    return LETTERS_AZ
        .filter(n -> not(typeVariableNames.exists(n::equals)))
        .headOption()
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "All single-letter type variables already used for generic class "
                        + getName()
                        + "! If this is really a use case and should be supported, please contact the maintainer."));
  }

  public Name findUnusedTypeVariableName(Name preferred) {
    final PList<Name> typeVariableNames = generics.map(Generic::getTypeVariable);
    return LETTERS_AZ
        .cons(preferred)
        .filter(n -> not(typeVariableNames.exists(n::equals)))
        .headOption()
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "All single-letter type variables already used for generic class "
                        + getName()
                        + "! If this is really a use case and should be supported, please contact the maintainer."));
  }
}
