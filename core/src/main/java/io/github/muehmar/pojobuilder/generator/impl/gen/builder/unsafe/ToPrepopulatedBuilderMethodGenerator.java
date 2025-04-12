package io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe;

import static io.github.muehmar.codegenerator.Generator.constant;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaDocGenerator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.codegenerator.java.JavaModifier;
import io.github.muehmar.codegenerator.java.MethodGen;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;

public class ToPrepopulatedBuilderMethodGenerator {
  public static final String TO_PREPOPULATED_BUILDER_NAME = "toBuilder";

  private ToPrepopulatedBuilderMethodGenerator() {}

  public static Generator<Pojo, PojoSettings> toPrepopulatedBuilderMethodGenerator() {
    return javaDoc().append(method());
  }

  private static MethodGen<Pojo, PojoSettings> method() {
    return JavaGenerators.<Pojo, PojoSettings>methodGen()
        .modifiers(JavaModifier.PUBLIC)
        .noGenericTypes()
        .returnType((p, s, w) -> w.println(s.builderName(p) + p.getTypeVariablesFormatted()))
        .methodName(TO_PREPOPULATED_BUILDER_NAME)
        .noArguments()
        .doesNotThrow()
        .content(content())
        .build();
  }

  public static Generator<Pojo, PojoSettings> javaDoc() {
    return JavaDocGenerator.javaDoc(
        (p, s) ->
            String.format(
                "Creates a new instance of {@link %s} where it is guaranteed that all required fields are already set.",
                s.builderName(p)));
  }

  private static Generator<Pojo, PojoSettings> content() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .append((p, s, w) -> w.println("return new %s%s(", s.builderName(p), p.getDiamond()))
        .appendList(
            (f, s, w) -> w.tab(2).print("%s", f.getName()),
            Pojo::getFields,
            (f, s, w) -> w.println(","))
        .appendSingleBlankLine()
        .append(constant(");"));
  }
}
