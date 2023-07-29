package io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard;

import static io.github.muehmar.codegenerator.java.JavaModifier.FINAL;
import static io.github.muehmar.codegenerator.java.JavaModifier.PRIVATE;
import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;
import static io.github.muehmar.codegenerator.java.JavaModifier.STATIC;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Generators.newLine;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.BuildMethod.buildMethod;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.StandardBuilderGenerator.BUILDER_ASSIGNMENT;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.pojobuilder.generator.impl.gen.RefsGen;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.function.ToIntFunction;

class FinalRequiredBuilder {
  private FinalRequiredBuilder() {}

  public static Generator<Pojo, PojoSettings> finalRequiredBuilder() {
    final ToIntFunction<Pojo> builderNumber =
        pojo -> pojo.getFields().filter(PojoField::isRequired).size();

    final Generator<Pojo, PojoSettings> constructor =
        JavaGenerators.<Pojo, PojoSettings>constructorGen()
            .modifiers(PRIVATE)
            .className(p -> String.format("Builder%d", builderNumber.applyAsInt(p)))
            .singleArgument(p -> String.format("Builder%s builder", p.getTypeVariablesSection()))
            .content(BUILDER_ASSIGNMENT)
            .build();

    final Generator<Pojo, PojoSettings> content =
        BuilderFieldDeclaration.<PojoSettings>builderFieldDeclaration()
            .append(newLine())
            .append(constructor)
            .append(newLine())
            .append(andAllOptionalsMethod())
            .append(newLine())
            .append(andOptionalsMethod())
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
                    "Builder%d%s",
                    builderNumber.applyAsInt(p), p.getGenericTypeDeclarationSection()))
        .noSuperClass()
        .noInterfaces()
        .content(content)
        .build()
        .append(RefsGen.genericRefs());
  }

  public static <A> Generator<Pojo, A> andAllOptionalsMethod() {
    return JavaGenerators.<Pojo, A>methodGen()
        .modifiers(PUBLIC)
        .noGenericTypes()
        .returnType(p -> "OptBuilder0" + p.getTypeVariablesSection())
        .methodName("andAllOptionals")
        .noArguments()
        .content(p -> String.format("return new OptBuilder0%s(builder);", p.getDiamond()))
        .build();
  }

  public static <A> Generator<Pojo, A> andOptionalsMethod() {
    return JavaGenerators.<Pojo, A>methodGen()
        .modifiers(PUBLIC)
        .noGenericTypes()
        .returnType(p -> "Builder" + p.getTypeVariablesSection())
        .methodName("andOptionals")
        .noArguments()
        .content("return builder;")
        .build();
  }
}
