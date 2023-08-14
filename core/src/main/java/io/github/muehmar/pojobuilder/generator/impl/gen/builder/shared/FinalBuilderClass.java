package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.codegenerator.java.JavaModifier.FINAL;
import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;
import static io.github.muehmar.codegenerator.java.JavaModifier.STATIC;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Generators.newLine;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.BuildMethod.buildMethod;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.BuilderMethodConstructor.builderMethodConstructor;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.pojobuilder.generator.impl.gen.RefsGen;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.function.ToIntFunction;

public class FinalBuilderClass {
  private FinalBuilderClass() {}

  public static Generator<Pojo, PojoSettings> finalBuilderClass(
      RawClassNameGenerator rawClassNameGenerator, ToIntFunction<Pojo> builderNumber) {

    final Generator<Pojo, PojoSettings> content =
        BuilderFieldDeclaration.<PojoSettings>builderFieldDeclaration()
            .append(newLine())
            .append(builderMethodConstructor(rawClassNameGenerator, builderNumber))
            .append(newLine())
            .append(buildMethod());

    return JavaGenerators.<Pojo, PojoSettings>classGen()
        .clazz()
        .nested()
        .packageGen(Generator.emptyGen())
        .noJavaDoc()
        .noAnnotations()
        .modifiers(PUBLIC, STATIC, FINAL)
        .className(
            (p, s) ->
                String.format(
                    "%s%s",
                    rawClassNameGenerator.forFieldIndex(builderNumber.applyAsInt(p)),
                    p.getBoundedTypeVariablesFormatted()))
        .noSuperClass()
        .noInterfaces()
        .content(content)
        .build()
        .append(RefsGen.genericRefs());
  }
}
