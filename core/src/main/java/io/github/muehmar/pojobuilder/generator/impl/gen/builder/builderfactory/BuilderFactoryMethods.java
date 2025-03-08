package io.github.muehmar.pojobuilder.generator.impl.gen.builder.builderfactory;

import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;
import static io.github.muehmar.codegenerator.java.JavaModifier.STATIC;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Filters.isFullBuilderEnabled;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Filters.isStandardBuilderEnabled;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.codegenerator.java.MethodGen.Argument;
import io.github.muehmar.pojobuilder.generator.impl.gen.RefsGen;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.builderstages.BuilderStagesGenerator;
import io.github.muehmar.pojobuilder.generator.model.Generic;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class BuilderFactoryMethods {

  private BuilderFactoryMethods() {}

  public static Generator<Pojo, PojoSettings> builderFactoryMethods() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .append(method(BuilderType.STANDARD, NameOption.SHORT, GenericsOption.NO_GENERICS))
        .appendSingleBlankLine()
        .append(
            method(BuilderType.STANDARD, NameOption.INCLUDE_CLASS_NAME, GenericsOption.NO_GENERICS))
        .appendSingleBlankLine()
        .append(method(BuilderType.STANDARD, NameOption.SHORT, GenericsOption.WITH_GENERICS))
        .appendSingleBlankLine()
        .append(
            method(
                BuilderType.STANDARD, NameOption.INCLUDE_CLASS_NAME, GenericsOption.WITH_GENERICS))
        .appendSingleBlankLine()
        .append(method(BuilderType.FULL, NameOption.SHORT, GenericsOption.NO_GENERICS))
        .appendSingleBlankLine()
        .append(method(BuilderType.FULL, NameOption.INCLUDE_CLASS_NAME, GenericsOption.NO_GENERICS))
        .appendSingleBlankLine()
        .append(method(BuilderType.FULL, NameOption.SHORT, GenericsOption.WITH_GENERICS))
        .appendSingleBlankLine()
        .append(
            method(BuilderType.FULL, NameOption.INCLUDE_CLASS_NAME, GenericsOption.WITH_GENERICS));
  }

  private static Generator<Pojo, PojoSettings> method(
      BuilderType builderType, NameOption nameOption, GenericsOption genericsOption) {
    final Function<Pojo, Object> returnType =
        p ->
            String.format(
                "%s.%sBuilder0%s",
                BuilderStagesGenerator.BUILDER_STAGES_CLASS_NAME,
                builderType.typeUppercase,
                p.getTypeVariablesFormatted());
    final Function<Pojo, String> content =
        p ->
            String.format(
                "return new %s.%sBuilder0%s(new Builder%s());",
                BuilderStagesGenerator.BUILDER_STAGES_CLASS_NAME,
                builderType.typeUppercase,
                p.getDiamond(),
                p.getTypeVariablesFormatted());
    return JavaGenerators.<Pojo, PojoSettings>methodGen()
        .modifiers(PUBLIC, STATIC)
        .genericTypes(
            p -> p.getGenerics().asList().map(Generic::getTypeDeclaration).map(Name::asString))
        .returnType(returnType)
        .methodName((pojo, settings) -> nameOption.getMethodName(pojo, settings, builderType))
        .arguments(genericsOption::createArguments)
        .doesNotThrow()
        .content(content)
        .build()
        .append(RefsGen.genericRefs())
        .filter(builderType.isEnabled().and(genericsOption.isEnabled()));
  }

  private enum BuilderType {
    STANDARD("", "") {
      @Override
      BiPredicate<Pojo, PojoSettings> isEnabled() {
        return isStandardBuilderEnabled();
      }
    },
    FULL("full", "Full") {
      @Override
      BiPredicate<Pojo, PojoSettings> isEnabled() {
        return isFullBuilderEnabled();
      }
    };

    private final String typeLowercase;
    private final String typeUppercase;

    BuilderType(String typeLowercase, String typeUppercase) {
      this.typeLowercase = typeLowercase;
      this.typeUppercase = typeUppercase;
    }

    abstract BiPredicate<Pojo, PojoSettings> isEnabled();
  }

  private enum NameOption {
    SHORT {
      @Override
      String getMethodName(Pojo pojo, PojoSettings settings, BuilderType builderType) {
        return String.format("create%s", builderType.typeUppercase);
      }
    },
    INCLUDE_CLASS_NAME {
      @Override
      String getMethodName(Pojo pojo, PojoSettings settings, BuilderType builderType) {
        return settings
            .builderName(pojo)
            .startUpperCase()
            .prefix(builderType.typeLowercase)
            .startLowerCase()
            .asString();
      }
    };

    abstract String getMethodName(Pojo pojo, PojoSettings settings, BuilderType builderType);
  }

  private enum GenericsOption {
    NO_GENERICS {
      @Override
      PList<Argument> createArguments(Pojo pojo, PojoSettings settings) {
        return PList.empty();
      }

      @Override
      BiPredicate<Pojo, PojoSettings> isEnabled() {
        return (pojo, settings) -> true;
      }
    },
    WITH_GENERICS {
      @Override
      PList<Argument> createArguments(Pojo pojo, PojoSettings settings) {
        return pojo.getGenerics()
            .asList()
            .map(
                generic ->
                    new Argument(
                        String.format("Class<%s>", generic.getTypeVariable()),
                        String.format("classOf%s", generic.getTypeVariable())));
      }

      @Override
      BiPredicate<Pojo, PojoSettings> isEnabled() {
        return (pojo, settings) -> pojo.getGenerics().asList().nonEmpty();
      }
    };

    abstract PList<Argument> createArguments(Pojo pojo, PojoSettings settings);

    abstract BiPredicate<Pojo, PojoSettings> isEnabled();
  }
}
