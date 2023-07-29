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

class FinalOptionalBuilder {
  private FinalOptionalBuilder() {}

  public static Generator<Pojo, PojoSettings> finalOptionalBuilder() {
    final ToIntFunction<Pojo> builderNumber =
        pojo -> pojo.getFields().filter(PojoField::isOptional).size();

    final Generator<Pojo, PojoSettings> constructor =
        JavaGenerators.<Pojo, PojoSettings>constructorGen()
            .modifiers(PRIVATE)
            .className(p -> String.format("OptBuilder%d", builderNumber.applyAsInt(p)))
            .singleArgument(p -> String.format("Builder%s builder", p.getTypeVariablesSection()))
            .content(BUILDER_ASSIGNMENT)
            .build();

    final Generator<Pojo, PojoSettings> content =
        BuilderFieldDeclaration.<PojoSettings>builderFieldDeclaration()
            .append(newLine())
            .append(constructor)
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
                    "OptBuilder%d%s",
                    builderNumber.applyAsInt(p), p.getGenericTypeDeclarationSection()))
        .noSuperClass()
        .noInterfaces()
        .content(content)
        .build()
        .append(RefsGen.genericRefs());
  }
}
