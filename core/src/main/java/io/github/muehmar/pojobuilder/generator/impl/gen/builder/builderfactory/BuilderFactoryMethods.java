package io.github.muehmar.pojobuilder.generator.impl.gen.builder.builderfactory;

import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;
import static io.github.muehmar.codegenerator.java.JavaModifier.STATIC;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Filters.isFullBuilderEnabled;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Filters.isStandardBuilderEnabled;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.pojobuilder.generator.impl.gen.RefsGen;
import io.github.muehmar.pojobuilder.generator.model.Generic;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BuilderFactoryMethods {

  private BuilderFactoryMethods() {}

  public static Generator<Pojo, PojoSettings> builderFactoryMethods() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .append(standardBuilderFactoryMethod((pojo, settings) -> "create"))
        .appendSingleBlankLine()
        .append(
            standardBuilderFactoryMethod(
                (pojo, settings) ->
                    String.format("%s", settings.builderName(pojo).startLowerCase())))
        .appendSingleBlankLine()
        .append(fullBuilderFactoryMethod((pojo, settings) -> "createFull"))
        .appendSingleBlankLine()
        .append(
            fullBuilderFactoryMethod(
                (pojo, settings) ->
                    String.format("full%s", settings.builderName(pojo).startUpperCase())));
  }

  private static Generator<Pojo, PojoSettings> standardBuilderFactoryMethod(
      BiFunction<Pojo, PojoSettings, Object> methodName) {
    final Function<Pojo, Object> returnType = p -> "Builder0" + p.getTypeVariablesFormatted();
    final Function<Pojo, String> content =
        p ->
            String.format(
                "return new Builder0%s(new Builder%s());",
                p.getDiamond(), p.getTypeVariablesFormatted());
    return JavaGenerators.<Pojo, PojoSettings>methodGen()
        .modifiers(PUBLIC, STATIC)
        .genericTypes(
            p -> p.getGenerics().asList().map(Generic::getTypeDeclaration).map(Name::asString))
        .returnType(returnType)
        .methodName(methodName)
        .noArguments()
        .doesNotThrow()
        .content(content)
        .build()
        .append(RefsGen.genericRefs())
        .filter(isStandardBuilderEnabled());
  }

  private static Generator<Pojo, PojoSettings> fullBuilderFactoryMethod(
      BiFunction<Pojo, PojoSettings, Object> methodName) {
    final Function<Pojo, Object> returnType = p -> "FullBuilder0" + p.getTypeVariablesFormatted();
    final Function<Pojo, String> content =
        p ->
            String.format(
                "return new FullBuilder0%s(new Builder%s());",
                p.getDiamond(), p.getTypeVariablesFormatted());
    return JavaGenerators.<Pojo, PojoSettings>methodGen()
        .modifiers(PUBLIC, STATIC)
        .genericTypes(
            p -> p.getGenerics().asList().map(Generic::getTypeDeclaration).map(Name::asString))
        .returnType(returnType)
        .methodName(methodName)
        .noArguments()
        .doesNotThrow()
        .content(content)
        .build()
        .append(RefsGen.genericRefs())
        .filter(isFullBuilderEnabled());
  }
}
