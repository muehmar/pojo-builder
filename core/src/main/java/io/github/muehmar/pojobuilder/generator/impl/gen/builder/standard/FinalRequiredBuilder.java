package io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard;

import static io.github.muehmar.codegenerator.java.JavaModifier.FINAL;
import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;
import static io.github.muehmar.codegenerator.java.JavaModifier.STATIC;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Generators.newLine;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.BuildMethod.standardBuilderBuildMethod;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.BuilderMethodConstructor.builderMethodConstructor;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.StandardBuilderGenerator.CLASS_NAME_FOR_REQUIRED;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.codegenerator.java.MethodGen;
import io.github.muehmar.pojobuilder.generator.impl.gen.RefsGen;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.BuilderFieldDeclaration;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe.ToPrepopulatedBuilderMethodGenerator;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.function.ToIntFunction;

class FinalRequiredBuilder {
  private FinalRequiredBuilder() {}

  public static Generator<Pojo, PojoSettings> finalRequiredBuilder() {
    final ToIntFunction<Pojo> builderNumber =
        pojo -> pojo.getFields().filter(PojoField::isRequired).size();

    final Generator<Pojo, PojoSettings> content =
        BuilderFieldDeclaration.<PojoSettings>builderFieldDeclaration()
            .append(newLine())
            .append(builderMethodConstructor(CLASS_NAME_FOR_REQUIRED, builderNumber))
            .append(newLine())
            .append(andAllOptionalsMethod())
            .append(newLine())
            .append(andOptionalsMethod())
            .append(newLine())
            .append(standardBuilderBuildMethod())
            .appendSingleBlankLine()
            .append(toBuilderMethod());

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
                    "Builder%d%s",
                    builderNumber.applyAsInt(p), p.getBoundedTypeVariablesFormatted()))
        .noSuperClass()
        .noInterfaces()
        .content(content)
        .build()
        .append(RefsGen.genericRefs());
  }

  private static Generator<Pojo, PojoSettings> andAllOptionalsMethod() {
    return JavaGenerators.<Pojo, PojoSettings>methodGen()
        .modifiers(PUBLIC)
        .noGenericTypes()
        .returnType(p -> "OptBuilder0" + p.getTypeVariablesFormatted())
        .methodName("andAllOptionals")
        .noArguments()
        .doesNotThrow()
        .content(p -> String.format("return new OptBuilder0%s(builder);", p.getDiamond()))
        .build();
  }

  private static Generator<Pojo, PojoSettings> andOptionalsMethod() {
    return JavaGenerators.<Pojo, PojoSettings>methodGen()
        .modifiers(PUBLIC)
        .noGenericTypes()
        .returnType(p -> "Builder" + p.getTypeVariablesFormatted())
        .methodName("andOptionals")
        .noArguments()
        .doesNotThrow()
        .content("return builder;")
        .build();
  }

  private static Generator<Pojo, PojoSettings> toBuilderMethod() {
    MethodGen<Pojo, PojoSettings> method =
        JavaGenerators.<Pojo, PojoSettings>methodGen()
            .modifiers(PUBLIC)
            .noGenericTypes()
            .returnType(
                (p, s, w) -> w.println("%s%s", s.builderName(p), p.getTypeVariablesFormatted()))
            .methodName("toBuilder")
            .noArguments()
            .doesNotThrow()
            .content("return builder.toBuilder();")
            .build();
    return ToPrepopulatedBuilderMethodGenerator.javaDoc().append(method);
  }
}
