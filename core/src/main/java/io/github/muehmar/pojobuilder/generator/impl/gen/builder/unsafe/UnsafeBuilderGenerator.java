package io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe;

import static io.github.muehmar.codegenerator.java.JavaModifier.FINAL;
import static io.github.muehmar.codegenerator.java.JavaModifier.PRIVATE;
import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;
import static io.github.muehmar.codegenerator.java.JavaModifier.STATIC;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Generators.newLine;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.BuildMethod.buildMethod;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.MappingBuildMethodContentGenerator.mappingBuildMethodContentGenerator;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.SetMethodGenerator.setMethodGenerator;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.pojobuilder.generator.impl.gen.FieldDeclarationGen;
import io.github.muehmar.pojobuilder.generator.impl.gen.PackageGen;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.SetMethodGenerator.ModifierOption;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;

/**
 * Factory which creates more or less the well-known standard builder pattern used for the
 * SafeBuilder.
 */
public class UnsafeBuilderGenerator {

  public static final String BUILDER_CLASSNAME = "Builder";

  private UnsafeBuilderGenerator() {}

  public static Generator<Pojo, PojoSettings> unsafeBuilderGenerator() {
    final Generator<Pojo, PojoSettings> constructor =
        JavaGenerators.<Pojo, PojoSettings>constructorGen()
            .modifiers(PRIVATE)
            .className(BUILDER_CLASSNAME)
            .noArguments()
            .noContent()
            .build();
    final Generator<Pojo, PojoSettings> content =
        constructor
            .append(newLine())
            .appendList(FieldDeclarationGen.ofModifiers(PRIVATE), Pojo::getFields)
            .appendSingleBlankLine()
            .append(
                setMethodGenerator((p, s) -> BUILDER_CLASSNAME, ModifierOption.REQUIRED_PRIVATE))
            .appendSingleBlankLine()
            .append(buildMethod(mappingBuildMethodContentGenerator()));

    return JavaGenerators.<Pojo, PojoSettings>classGen()
        .clazz()
        .nested()
        .packageGen(new PackageGen())
        .noJavaDoc()
        .noAnnotations()
        .modifiers(PUBLIC, STATIC, FINAL)
        .className(p -> BUILDER_CLASSNAME + p.getBoundedTypeVariablesFormatted())
        .noSuperClass()
        .noInterfaces()
        .content(content)
        .build();
  }
}
