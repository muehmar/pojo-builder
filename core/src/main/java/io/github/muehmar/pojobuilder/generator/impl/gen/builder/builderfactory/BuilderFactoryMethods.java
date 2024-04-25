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
import java.util.function.BiPredicate;
import java.util.function.Function;

public class BuilderFactoryMethods {

  private BuilderFactoryMethods() {}

  public static Generator<Pojo, PojoSettings> builderFactoryMethods() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .append(standardBuilderFactoryMethod(BuilderType.STANDARD, NameOption.SHORT))
        .appendSingleBlankLine()
        .append(standardBuilderFactoryMethod(BuilderType.STANDARD, NameOption.INCLUDE_CLASS_NAME))
        .appendSingleBlankLine()
        .append(standardBuilderFactoryMethod(BuilderType.FULL, NameOption.SHORT))
        .appendSingleBlankLine()
        .append(standardBuilderFactoryMethod(BuilderType.FULL, NameOption.INCLUDE_CLASS_NAME));
  }

  private static Generator<Pojo, PojoSettings> standardBuilderFactoryMethod(
      BuilderType builderType, NameOption nameOption) {
    final Function<Pojo, Object> returnType =
        p -> builderType.typeUppercase + "Builder0" + p.getTypeVariablesFormatted();
    final Function<Pojo, String> content =
        p ->
            String.format(
                "return new %sBuilder0%s(new Builder%s());",
                builderType.typeUppercase, p.getDiamond(), p.getTypeVariablesFormatted());
    return JavaGenerators.<Pojo, PojoSettings>methodGen()
        .modifiers(PUBLIC, STATIC)
        .genericTypes(
            p -> p.getGenerics().asList().map(Generic::getTypeDeclaration).map(Name::asString))
        .returnType(returnType)
        .methodName((pojo, settings) -> nameOption.getMethodName(pojo, settings, builderType))
        .noArguments()
        .doesNotThrow()
        .content(content)
        .build()
        .append(RefsGen.genericRefs())
        .filter(builderType.isEnabled());
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
}
