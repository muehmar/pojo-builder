package io.github.muehmar.pojobuilder.generator.impl.gen.builder;

import static io.github.muehmar.codegenerator.java.JavaModifier.FINAL;
import static io.github.muehmar.codegenerator.java.JavaModifier.PRIVATE;
import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;
import static io.github.muehmar.codegenerator.java.JavaModifier.STATIC;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Filters.isFullBuilderEnabled;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Filters.isStandardBuilderEnabled;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.full.FullBuilderGenerator.fullBuilderGenerator;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.StandardBuilderGenerator.standardBuilderGenerator;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe.UnsafeBuilderGenerator.unsafeBuilderGenerator;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.codegenerator.java.JavaModifier;
import io.github.muehmar.pojobuilder.generator.impl.gen.PackageGen;
import io.github.muehmar.pojobuilder.generator.impl.gen.RefsGen;
import io.github.muehmar.pojobuilder.generator.model.Generic;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PojoBuilderGenerator {
  private PojoBuilderGenerator() {}

  public static Generator<Pojo, PojoSettings> pojoBuilderGenerator() {
    return JavaGenerators.<Pojo, PojoSettings>classGen()
        .clazz()
        .topLevel()
        .packageGen(new PackageGen())
        .noJavaDoc()
        .noAnnotations()
        .modifierList((pojo, settings) -> createClassModifiers(settings))
        .className((p, s) -> s.builderName(p).asString())
        .noSuperClass()
        .noInterfaces()
        .content(pojoBuilderContent())
        .build();
  }

  private static PList<JavaModifier> createClassModifiers(PojoSettings settings) {
    return PList.fromOptional(settings.getBuilderAccessLevel().asJavaModifier()).cons(FINAL);
  }

  private static Generator<Pojo, PojoSettings> pojoBuilderContent() {
    final Generator<Pojo, PojoSettings> constructor =
        JavaGenerators.<Pojo, PojoSettings>constructorGen()
            .modifiers(PRIVATE)
            .className((p, s) -> s.builderName(p).asString())
            .noArguments()
            .noContent()
            .build();
    return Generator.<Pojo, PojoSettings>emptyGen()
        .appendSingleBlankLine()
        .append(constructor)
        .appendSingleBlankLine()
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
                    String.format("full%s", settings.builderName(pojo).startUpperCase())))
        .appendSingleBlankLine()
        .append(unsafeBuilderGenerator())
        .appendSingleBlankLine()
        .append(standardBuilderGenerator())
        .appendSingleBlankLine()
        .append(fullBuilderGenerator());
  }

  private static Generator<Pojo, PojoSettings> standardBuilderFactoryMethod(
      BiFunction<Pojo, PojoSettings, String> methodName) {
    final Function<Pojo, String> returnType = p -> "Builder0" + p.getTypeVariablesSection();
    final Function<Pojo, String> content =
        p ->
            String.format(
                "return new Builder0%s(new Builder%s());",
                p.getDiamond(), p.getTypeVariablesSection());
    return JavaGenerators.<Pojo, PojoSettings>methodGen()
        .modifiers(PUBLIC, STATIC)
        .genericTypes(p -> p.getGenerics().map(Generic::getTypeDeclaration).map(Name::asString))
        .returnType(returnType)
        .methodName(methodName)
        .noArguments()
        .content(content)
        .build()
        .append(RefsGen.genericRefs())
        .filter(isStandardBuilderEnabled());
  }

  private static Generator<Pojo, PojoSettings> fullBuilderFactoryMethod(
      BiFunction<Pojo, PojoSettings, String> methodName) {
    final Function<Pojo, String> returnType = p -> "FullBuilder0" + p.getTypeVariablesSection();
    final Function<Pojo, String> content =
        p ->
            String.format(
                "return new FullBuilder0%s(new Builder%s());",
                p.getDiamond(), p.getTypeVariablesSection());
    return JavaGenerators.<Pojo, PojoSettings>methodGen()
        .modifiers(PUBLIC, STATIC)
        .genericTypes(p -> p.getGenerics().map(Generic::getTypeDeclaration).map(Name::asString))
        .returnType(returnType)
        .methodName(methodName)
        .noArguments()
        .content(content)
        .build()
        .append(RefsGen.genericRefs())
        .filter(isFullBuilderEnabled());
  }
}
