package io.github.muehmar.pojobuilder.generator.impl.gen.builder;

import static io.github.muehmar.codegenerator.java.JavaModifier.FINAL;
import static io.github.muehmar.codegenerator.java.JavaModifier.PRIVATE;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.builderfactory.BuilderFactoryMethods.builderFactoryMethods;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.builderstages.BuilderStagesGenerator.builderStagesGenerator;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.full.FullBuilderGenerator.fullBuilderGenerator;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.StandardBuilderGenerator.standardBuilderGenerator;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe.UnsafeBuilderGenerator.unsafeBuilderGenerator;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.codegenerator.java.JavaModifier;
import io.github.muehmar.pojobuilder.generator.impl.gen.PackageGen;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;

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
        .append(builderFactoryMethods())
        .appendSingleBlankLine()
        .append(unsafeBuilderGenerator())
        .appendSingleBlankLine()
        .append(builderStages());
  }

  private static Generator<Pojo, PojoSettings> builderStages() {
    final Generator<Pojo, PojoSettings> content =
        Generator.<Pojo, PojoSettings>emptyGen()
            .append(standardBuilderGenerator())
            .appendSingleBlankLine()
            .append(fullBuilderGenerator());

    return builderStagesGenerator(content);
  }
}
