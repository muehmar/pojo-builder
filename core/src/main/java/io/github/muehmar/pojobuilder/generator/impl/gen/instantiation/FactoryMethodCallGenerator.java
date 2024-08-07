package io.github.muehmar.pojobuilder.generator.impl.gen.instantiation;

import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.generator.model.FactoryMethod;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.matching.FieldArgument;
import io.github.muehmar.pojobuilder.generator.model.settings.FieldMatching;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.Optional;
import lombok.Value;

public class FactoryMethodCallGenerator {
  private FactoryMethodCallGenerator() {}

  public static Generator<Pojo, PojoSettings> factoryMethodCallGenerator() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .appendOptional(
            factoryCallForFields(), FactoryMethodCallGenerator::createFactoryMethodCall);
  }

  private static Optional<FactoryMethodCall> createFactoryMethodCall(Pojo pojo) {
    return pojo.getFactoryMethod()
        .flatMap(
            factoryMethod ->
                pojo.matchArguments(factoryMethod.getArguments(), FieldMatching.TYPE)
                    .getFieldArguments()
                    .map(
                        fieldArguments ->
                            new FactoryMethodCall(pojo, factoryMethod, fieldArguments)));
  }

  private static Generator<FactoryMethodCall, PojoSettings> factoryCallForFields() {
    return Generator.<FactoryMethodCall, PojoSettings>emptyGen()
        .append((fmc, s, w) -> w.print("%s(", fmc.scopedMethodName()))
        .appendList(
            singleFieldSameType().append(singleFieldWrapIntoOptional()),
            FactoryMethodCall::getFields,
            (a, s, w) -> w.print(", "))
        .append(Generator.constant(");"));
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
  private static class FactoryMethodCall {
    Pojo pojo;
    FactoryMethod factoryMethod;
    PList<FieldArgument> fields;

    public String scopedMethodName() {
      return String.format(
          "%s.%s", factoryMethod.getOwnerClassname().asName(), factoryMethod.getMethodName());
    }
  }
}
