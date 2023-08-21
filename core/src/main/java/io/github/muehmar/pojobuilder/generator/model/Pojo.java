package io.github.muehmar.pojobuilder.generator.model;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.Strings;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.exception.PojoBuilderException;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import java.util.Optional;
import lombok.Value;
import lombok.With;

@Value
@With
@PojoBuilder
public class Pojo {
  QualifiedClassname pojoClassname;
  Name pojoNameWithTypeVariables;
  /** Package name where the annotation was placed and the builder will get created. */
  PackageName pkg;

  PList<PojoField> fields;
  PList<Constructor> constructors;
  Optional<FactoryMethod> factoryMethod;
  /**
   * These are the generics used to create an instance of the pojo, may be different to the generics
   * of the pojo itself.
   */
  PList<Generic> generics;

  PList<FieldBuilder> fieldBuilders;
  Optional<BuildMethod> buildMethod;

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

  public String getBoundedTypeVariablesFormatted() {
    final String typeVariableDeclaration =
        generics.map(Generic::getTypeDeclaration).map(Name::asString).mkString(", ");
    return Strings.surroundIfNotEmpty("<", typeVariableDeclaration, ">");
  }

  public String getTypeVariablesFormatted() {
    return Strings.surroundIfNotEmpty(
        "<", generics.map(Generic::getTypeVariable).mkString(", "), ">");
  }

  public Name getPojoNameWithTypeVariables() {
    return pojoNameWithTypeVariables;
  }

  public Optional<MatchingConstructor> findMatchingConstructor() {
    return constructors
        .flatMapOptional(
            c -> matchArguments(c.getArguments()).map(f -> new MatchingConstructor(c, f)))
        .headOption();
  }

  public MatchingConstructor getMatchingConstructorOrThrow() {
    return findMatchingConstructor()
        .orElseThrow(() -> new PojoBuilderException(noMatchingConstructorMessage()));
  }

  /**
   * Matches a list of arguments against the fields and returns the matched {@link FieldArgument}'s
   * if they match completely.
   */
  public Optional<PList<FieldArgument>> matchArguments(PList<Argument> arguments) {
    if (fields.size() != arguments.size()) {
      return Optional.empty();
    }

    final PList<FieldArgument> fieldArguments =
        fields
            .zip(arguments)
            .flatMapOptional(p -> FieldArgument.fromFieldAndArgument(p.first(), p.second()));

    return Optional.of(fieldArguments).filter(f -> f.size() == arguments.size());
  }

  private String noMatchingConstructorMessage() {
    return String.format(
        "No matching constructor found for class/record %s."
            + " A constructor should have all the fields as arguments in the order of declaration and matching type,"
            + " where the actual type of a non-required field can be wrapped into an java.util.Optional. Furthermore"
            + "it should be accessible from within the same package, i.e. at least package-private. If a field is"
            + "instantiated in the constructor and not part of the arguments, you can annotate it with @Ignore.",
        getPojoClassname());
  }
}
