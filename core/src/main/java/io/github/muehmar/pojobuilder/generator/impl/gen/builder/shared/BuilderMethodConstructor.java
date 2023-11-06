package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.codegenerator.java.ConstructorGen.Argument.argument;
import static io.github.muehmar.codegenerator.java.JavaModifier.PRIVATE;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.model.IndexedField;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.function.ToIntFunction;
import lombok.Value;

public class BuilderMethodConstructor {
  private BuilderMethodConstructor() {}

  public static Generator<Pojo, PojoSettings> builderMethodConstructor(
      RawClassNameGenerator rawClassNameGenerator, ToIntFunction<Pojo> builderNumber) {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .append(
            builderMethodConstructorInternal(rawClassNameGenerator),
            pojo -> PojoAndBuilderNumber.fromPojoAndBuilderNumber(pojo, builderNumber));
  }

  public static Generator<IndexedField, PojoSettings> builderMethodConstructor(
      RawClassNameGenerator rawClassNameGenerator) {
    return Generator.<IndexedField, PojoSettings>emptyGen()
        .append(
            builderMethodConstructorInternal(rawClassNameGenerator),
            PojoAndBuilderNumber::fromIndexField);
  }

  private static Generator<PojoAndBuilderNumber, PojoSettings> builderMethodConstructorInternal(
      RawClassNameGenerator rawClassNameGenerator) {
    return JavaGenerators.<PojoAndBuilderNumber, PojoSettings>constructorGen()
        .modifiers(PRIVATE)
        .className(
            p -> String.format("%s", rawClassNameGenerator.forFieldIndex(p.getBuilderNumber())))
        .singleArgument(
            p ->
                argument(
                    String.format("Builder%s", p.getPojo().getTypeVariablesFormatted()), "builder"))
        .content("this.builder = builder;")
        .build();
  }

  @Value
  private static class PojoAndBuilderNumber {
    Pojo pojo;
    int builderNumber;

    static PojoAndBuilderNumber fromIndexField(IndexedField indexedField) {
      return new PojoAndBuilderNumber(indexedField.getPojo(), indexedField.getIndex());
    }

    static PojoAndBuilderNumber fromPojoAndBuilderNumber(
        Pojo pojo, ToIntFunction<Pojo> builderNumber) {
      return new PojoAndBuilderNumber(pojo, builderNumber.applyAsInt(pojo));
    }
  }
}
