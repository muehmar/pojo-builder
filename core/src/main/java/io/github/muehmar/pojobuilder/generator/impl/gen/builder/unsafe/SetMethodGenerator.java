package io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe;

import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Generators.newLine;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe.UnsafeBuilderGenerator.BUILDER_CLASSNAME;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.codegenerator.java.JavaModifier;
import io.github.muehmar.codegenerator.java.JavaModifiers;
import io.github.muehmar.pojobuilder.generator.impl.gen.RefsGen;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoAndField;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;

class SetMethodGenerator {
  private SetMethodGenerator() {}

  public static Generator<Pojo, PojoSettings> setMethodGenerator() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .appendList(standardSetMethod(), Pojo::getPojoAndFields, newLine())
        .appendSingleBlankLine()
        .appendList(optionalSetMethod().appendSingleBlankLine(), Pojo::getPojoAndFields);
  }

  private static Generator<PojoAndField, PojoSettings> standardSetMethod() {
    final Generator<PojoAndField, PojoSettings> content =
        (paf, settings, writer) ->
            writer
                .println("this.%s = %s;", paf.getField().getName(), paf.getField().getName())
                .println("return this;");

    return JavaGenerators.<PojoAndField, PojoSettings>methodGen()
        .modifiers(
            (paf, s) ->
                JavaModifiers.of(
                    paf.getField().isRequired() ? JavaModifier.PRIVATE : JavaModifier.PUBLIC))
        .noGenericTypes()
        .returnType(paf -> BUILDER_CLASSNAME + paf.getPojo().getTypeVariablesFormatted())
        .methodName((paf, s) -> paf.getField().builderSetMethodName(s).asString())
        .singleArgument(
            paf ->
                String.format(
                    "%s %s",
                    paf.getField().getType().getTypeDeclaration(), paf.getField().getName()))
        .content(content)
        .build()
        .append(RefsGen.fieldRefs(), PojoAndField::getField);
  }

  private static Generator<PojoAndField, PojoSettings> optionalSetMethod() {
    final Generator<PojoAndField, PojoSettings> content =
        (paf, settings, writer) ->
            writer
                .println(
                    "this.%s = %s.orElse(null);",
                    paf.getField().getName(), paf.getField().getName())
                .println("return this;");

    return JavaGenerators.<PojoAndField, PojoSettings>methodGen()
        .modifiers(PUBLIC)
        .noGenericTypes()
        .returnType(paf -> BUILDER_CLASSNAME + paf.getPojo().getTypeVariablesFormatted())
        .methodName((paf, s) -> paf.getField().builderSetMethodName(s).asString())
        .singleArgument(
            paf ->
                String.format(
                    "Optional<%s> %s",
                    paf.getField().getType().getTypeDeclaration(), paf.getField().getName()))
        .content(content)
        .build()
        .append(w -> w.ref(JAVA_UTIL_OPTIONAL))
        .append(RefsGen.fieldRefs(), PojoAndField::getField)
        .filter(pf -> pf.getField().isOptional());
  }
}
