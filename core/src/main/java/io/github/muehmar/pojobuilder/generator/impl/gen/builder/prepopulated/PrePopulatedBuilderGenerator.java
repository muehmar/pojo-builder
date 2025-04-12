package io.github.muehmar.pojobuilder.generator.impl.gen.builder.prepopulated;

import static io.github.muehmar.codegenerator.java.ConstructorGen.Argument.argument;
import static io.github.muehmar.codegenerator.java.JavaModifier.PRIVATE;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.BuildMethod.buildMethod;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.MappingBuildMethodContentGenerator.mappingBuildMethodContentGenerator;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.ConstructorGen;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.pojobuilder.generator.impl.gen.FieldDeclarationGen;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.SetMethodGenerator;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.SetMethodGenerator.ModifierOption;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.function.BiFunction;

/**
 * This creates the builder (content) which is used as prepopulated builder. It is generated
 * directly in the main class with a private constructor.
 */
public class PrePopulatedBuilderGenerator {
  private PrePopulatedBuilderGenerator() {}

  public static Generator<Pojo, PojoSettings> prePopulatedBuilderGenerator() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .appendList(FieldDeclarationGen.ofModifiers(PRIVATE), Pojo::getFields)
        .appendSingleBlankLine()
        .append(constructor())
        .appendSingleBlankLine()
        .append(
            SetMethodGenerator.setMethodGenerator(
                (p, s) -> s.builderName(p).asString(), ModifierOption.PUBLIC))
        .appendSingleBlankLine()
        .append(buildMethod(mappingBuildMethodContentGenerator()));
  }

  private static Generator<Pojo, PojoSettings> constructor() {
    return JavaGenerators.<Pojo, PojoSettings>constructorGen()
        .modifiers(PRIVATE)
        .className((p, s) -> s.builderName(p))
        .arguments(constructorArguments())
        .memberAssignmentContent(constructorArguments())
        .build();
  }

  private static BiFunction<Pojo, PojoSettings, PList<ConstructorGen.Argument>>
      constructorArguments() {
    return (pojo, settings) -> {
      return pojo.getFields()
          .map(field -> argument(field.getType().getTypeDeclaration(), field.getName()));
    };
  }
}
