package io.github.muehmar.pojobuilder.generator.model;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.matching.ArgumentsMatchingResult;
import io.github.muehmar.pojobuilder.generator.model.matching.ConstructorMatchingResults;
import io.github.muehmar.pojobuilder.generator.model.matching.MatchingConstructor;
import io.github.muehmar.pojobuilder.generator.model.matching.MismatchReason;
import io.github.muehmar.pojobuilder.generator.model.matching.SingleArgumentMatchingResult;
import io.github.muehmar.pojobuilder.generator.model.matching.SingleConstructorMatchingResult;
import io.github.muehmar.pojobuilder.generator.model.settings.FieldMatching;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
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
  Generics generics;

  PList<FieldBuilder> fieldBuilders;
  Optional<BuildMethod> buildMethod;

  public PackageName getPackage() {
    return pkg;
  }

  public PList<PojoAndField> getPojoAndFields() {
    return fields.map(f -> new PojoAndField(this, f));
  }

  public PList<Name> getGenericImports() {
    return generics.getImports();
  }

  public String getDiamond() {
    return generics.asList().nonEmpty() ? "<>" : "";
  }

  public String getBoundedTypeVariablesFormatted() {
    return generics.getBoundedTypeVariablesFormatted();
  }

  public String getTypeVariablesFormatted() {
    return generics.getTypeVariablesFormatted();
  }

  public Name getPojoNameWithTypeVariables() {
    return pojoNameWithTypeVariables;
  }

  public ConstructorMatchingResults findMatchingConstructor(FieldMatching fieldMatching) {
    final PList<SingleConstructorMatchingResult> constructorMatchingResults =
        constructors.map(
            constructor ->
                matchArguments(constructor.getArguments(), fieldMatching)
                    .fold(
                        fieldArguments ->
                            SingleConstructorMatchingResult.ofMatchingConstructor(
                                new MatchingConstructor(constructor, fieldArguments)),
                        mismatchReasons ->
                            SingleConstructorMatchingResult.ofMismatchReasons(
                                constructor, mismatchReasons)));
    return new ConstructorMatchingResults(constructorMatchingResults);
  }

  public ArgumentsMatchingResult matchArguments(
      PList<Argument> arguments, FieldMatching fieldMatching) {
    if (fields.size() != arguments.size()) {
      return ArgumentsMatchingResult.fromSingleArgumentMatchingResult(
          SingleArgumentMatchingResult.fromMismatchReason(
              MismatchReason.nonMatchingArgumentCount(this, arguments)));
    }

    final PList<SingleArgumentMatchingResult> matchingResults =
        fields
            .zip(arguments)
            .map(
                p -> {
                  final PojoField pojoField = p.first();
                  final Argument argument = p.second();
                  return pojoField.match(argument, fieldMatching);
                });

    return new ArgumentsMatchingResult(matchingResults);
  }
}
