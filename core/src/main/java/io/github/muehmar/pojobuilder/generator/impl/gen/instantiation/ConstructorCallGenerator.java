package io.github.muehmar.pojobuilder.generator.impl.gen.instantiation;

import static io.github.muehmar.pojobuilder.Booleans.not;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.matching.FieldArgument;
import io.github.muehmar.pojobuilder.generator.model.matching.MatchingConstructor;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.Optional;
import lombok.Value;

public class ConstructorCallGenerator {
  private ConstructorCallGenerator() {}

  /**
   * This generator creates a call to the constructor (return the created instance) of the pojo
   * while wrapping optional fields into an {@link Optional} depending on the argument of the
   * constructor.
   */
  public static Generator<Pojo, PojoSettings> constructorCallGenerator() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .append(
            constructorCallForFields(),
            (pojo, settings) -> {
              final MatchingConstructor matchingConstructor =
                  pojo.findMatchingConstructor(settings.getFieldMatching())
                      .getFirstMatchingConstructorOrThrow(pojo, settings.getFieldMatching());
              final PList<FieldArgument> fieldArguments = matchingConstructor.getFieldArguments();
              return new ConstructorCall(pojo, fieldArguments, matchingConstructor);
            })
        .filter(pojo -> not(pojo.getFactoryMethod().isPresent()));
  }

  private static Generator<ConstructorCall, PojoSettings> constructorCallForFields() {
    return Generator.<ConstructorCall, PojoSettings>emptyGen()
        .append((cc, s, w) -> w.print("new %s(", cc.getPojo().getPojoNameWithTypeVariables()))
        .appendList(
            singleFieldSameType().append(singleFieldWrapIntoOptional()),
            ConstructorCall::getFields,
            (a, s, w) -> w.print(", "))
        .append((cc, s, w) -> w.println(");"));
  }

  private static Generator<FieldArgument, PojoSettings> singleFieldSameType() {
    return Generator.<FieldArgument, PojoSettings>emptyGen()
        .append((f, s, w) -> w.print("%s", f.getField().getName()))
        .filter(fieldArgument -> fieldArgument.getRelation().isSameType());
  }

  private static Generator<FieldArgument, PojoSettings> singleFieldWrapIntoOptional() {
    return Generator.<FieldArgument, PojoSettings>emptyGen()
        .append((f, s, w) -> w.print("Optional.ofNullable(%s)", f.getField().getName()))
        .append(w -> w.ref(JAVA_UTIL_OPTIONAL))
        .filter(fieldArgument -> fieldArgument.getRelation().isWrapIntoOptional());
  }

  @Value
  private static class ConstructorCall {
    Pojo pojo;
    PList<FieldArgument> fields;
    MatchingConstructor constructor;
  }
}
