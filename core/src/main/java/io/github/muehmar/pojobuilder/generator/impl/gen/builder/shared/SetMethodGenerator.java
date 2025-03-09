package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;
import static io.github.muehmar.codegenerator.java.MethodGen.Argument.argument;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Generators.newLine;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.codegenerator.java.JavaModifier;
import io.github.muehmar.codegenerator.java.JavaModifiers;
import io.github.muehmar.pojobuilder.generator.impl.gen.RefsGen;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoAndField;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.function.BiFunction;

public class SetMethodGenerator {
  private SetMethodGenerator() {}

  public static Generator<Pojo, PojoSettings> setMethodGenerator(
      BiFunction<Pojo, PojoSettings, String> builderClassName, ModifierOption modifierOption) {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .appendList(
            standardSetMethod(builderClassName, modifierOption), Pojo::getPojoAndFields, newLine())
        .appendSingleBlankLine()
        .appendList(
            optionalSetMethod(builderClassName).appendSingleBlankLine(), Pojo::getPojoAndFields);
  }

  private static Generator<PojoAndField, PojoSettings> standardSetMethod(
      BiFunction<Pojo, PojoSettings, String> builderClassName, ModifierOption modifierOption) {
    final Generator<PojoAndField, PojoSettings> content =
        (paf, settings, writer) ->
            writer
                .println("this.%s = %s;", paf.getField().getName(), paf.getField().getName())
                .println("return this;");

    return JavaGenerators.<PojoAndField, PojoSettings>methodGen()
        .modifiers(
            (paf, s) ->
                JavaModifiers.of(
                    paf.getField().isRequired()
                            && modifierOption.equals(ModifierOption.REQUIRED_PRIVATE)
                        ? JavaModifier.PRIVATE
                        : JavaModifier.PUBLIC))
        .noGenericTypes()
        .returnType(
            (paf, s, w) ->
                w.println(
                    builderClassName.apply(paf.getPojo(), s)
                        + paf.getPojo().getTypeVariablesFormatted()))
        .methodName((paf, s) -> paf.getField().builderSetMethodName(s).asString())
        .singleArgument(
            paf ->
                argument(paf.getField().getType().getTypeDeclaration(), paf.getField().getName()))
        .doesNotThrow()
        .content(content)
        .build()
        .append(RefsGen.fieldRefs(), PojoAndField::getField);
  }

  private static Generator<PojoAndField, PojoSettings> optionalSetMethod(
      BiFunction<Pojo, PojoSettings, String> builderClassName) {
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
        .returnType(
            (paf, s, w) ->
                w.print(
                    builderClassName.apply(paf.getPojo(), s)
                        + paf.getPojo().getTypeVariablesFormatted()))
        .methodName((paf, s) -> paf.getField().builderSetMethodName(s).asString())
        .singleArgument(
            paf ->
                argument(
                    String.format("Optional<%s>", paf.getField().getType().getTypeDeclaration()),
                    paf.getField().getName()))
        .doesNotThrow()
        .content(content)
        .build()
        .append(w -> w.ref(JAVA_UTIL_OPTIONAL))
        .append(RefsGen.fieldRefs(), PojoAndField::getField)
        .filter(pf -> pf.getField().isOptional());
  }

  public enum ModifierOption {
    PUBLIC,
    REQUIRED_PRIVATE
  }
}
