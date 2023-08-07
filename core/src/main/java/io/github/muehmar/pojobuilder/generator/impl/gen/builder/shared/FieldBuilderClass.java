package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.codegenerator.java.JavaModifier.FINAL;
import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;
import static io.github.muehmar.codegenerator.java.JavaModifier.STATIC;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Generators.newLine;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.BuilderMethodConstructor.builderMethodConstructor;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.impl.gen.RefsGen;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.model.BuilderField;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.model.BuilderFieldWithMethod;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.model.IndexedField;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.function.Function;

public class FieldBuilderClass {
  private FieldBuilderClass() {}

  public static Generator<BuilderField, PojoSettings> fieldBuilderClass(
      RawClassNameGenerator rawClassNameGenerator) {
    return JavaGenerators.<BuilderField, PojoSettings>classGen()
        .clazz()
        .nested()
        .packageGen(Generator.emptyGen())
        .noJavaDoc()
        .noAnnotations()
        .modifiers(PUBLIC, STATIC, FINAL)
        .className(f -> classDeclaration(rawClassNameGenerator, f.getIndexedField()))
        .noSuperClass()
        .noInterfaces()
        .content(builderClassContent(rawClassNameGenerator))
        .build()
        .append(RefsGen.genericRefs(), BuilderField::getPojo);
  }

  private static String classDeclaration(
      RawClassNameGenerator rawClassNameGenerator, IndexedField field) {
    return rawClassNameGenerator.forField(field)
        + field.getPojo().getGenericTypeDeclarationSection();
  }

  private static String nextClassTypeVariables(
      RawClassNameGenerator rawClassNameGenerator, IndexedField field) {
    return nextRawClassName(rawClassNameGenerator, field)
        + field.getPojo().getTypeVariablesSection();
  }

  private static String nextClassDiamond(
      RawClassNameGenerator rawClassNameGenerator, IndexedField field) {
    return nextRawClassName(rawClassNameGenerator, field) + field.getPojo().getDiamond();
  }

  private static String nextRawClassName(
      RawClassNameGenerator rawClassNameGenerator, IndexedField field) {
    return rawClassNameGenerator.forField(field.withIndex(field.getIndex() + 1));
  }

  public static Generator<BuilderField, PojoSettings> builderClassContent(
      RawClassNameGenerator rawClassNameGenerator) {
    return BuilderFieldDeclaration.<PojoSettings>builderFieldDeclaration()
        .contraMap(BuilderField::getPojo)
        .append(newLine())
        .append(builderMethodConstructor(rawClassNameGenerator), BuilderField::getIndexedField)
        .append(newLine())
        .append(setMethod(rawClassNameGenerator))
        .appendConditionally(
            BuilderField::hasFieldBuilder,
            fieldBuilderMethods(rawClassNameGenerator).prependNewLine())
        .appendConditionally(
            BuilderField::isFieldOptional,
            setMethodOptional(rawClassNameGenerator).prependNewLine());
  }

  public static Generator<BuilderField, PojoSettings> setMethod(
      RawClassNameGenerator rawClassNameGenerator) {
    final Generator<BuilderField, PojoSettings> content =
        (f, s, w) ->
            w.println(
                "return new %s(builder.%s(%s));",
                nextClassDiamond(rawClassNameGenerator, f.getIndexedField()),
                f.getField().builderSetMethodName(s),
                f.getField().getName());

    return JavaGenerators.<BuilderField, PojoSettings>methodGen()
        .modifiers(PUBLIC)
        .noGenericTypes()
        .returnType(f -> nextClassTypeVariables(rawClassNameGenerator, f.getIndexedField()))
        .methodName((f, s) -> f.getField().builderSetMethodName(s).asString())
        .singleArgument(
            f ->
                String.format(
                    "%s %s", f.getField().getType().getTypeDeclaration(), f.getField().getName()))
        .content(content)
        .build()
        .append(RefsGen.fieldRefs(), BuilderField::getField)
        .filter(BuilderField::isEnableDefaultMethods);
  }

  public static Generator<BuilderField, PojoSettings> setMethodOptional(
      RawClassNameGenerator rawClassNameGenerator) {
    final Generator<BuilderField, PojoSettings> content =
        (f, s, w) ->
            w.println(
                "return new %s(builder.%s(%s));",
                nextClassDiamond(rawClassNameGenerator, f.getIndexedField()),
                f.getField().builderSetMethodName(s),
                f.getField().getName());

    return JavaGenerators.<BuilderField, PojoSettings>methodGen()
        .modifiers(PUBLIC)
        .noGenericTypes()
        .returnType(f -> nextClassTypeVariables(rawClassNameGenerator, f.getIndexedField()))
        .methodName((f, s) -> f.getField().builderSetMethodName(s).asString())
        .singleArgument(
            f ->
                String.format(
                    "Optional<%s> %s",
                    f.getField().getType().getTypeDeclaration(), f.getField().getName()))
        .content(content)
        .build()
        .append(w -> w.ref(JAVA_UTIL_OPTIONAL))
        .append(RefsGen.fieldRefs(), BuilderField::getField)
        .filter(BuilderField::isEnableDefaultMethods);
  }

  public static Generator<BuilderField, PojoSettings> fieldBuilderMethods(
      RawClassNameGenerator rawClassNameGenerator) {
    final Generator<BuilderFieldWithMethod, PojoSettings> content =
        (f, s, w) ->
            w.println(
                "return new %s(builder.%s(%s%s.%s(%s)));",
                nextClassDiamond(rawClassNameGenerator, f.getIndexedField()),
                f.getField().builderSetMethodName(s),
                f.getPojo().getPojoName(),
                f.getFieldBuilderMethod()
                    .getInnerClassName()
                    .map(name -> name.prefix(".").asString())
                    .orElse(""),
                f.getFieldBuilderMethod().getMethodName(),
                f.getFieldBuilderMethod().getArgumentNames().mkString(", "));

    final Function<BuilderFieldWithMethod, String> nextClassTypeVariables =
        f -> nextClassTypeVariables(rawClassNameGenerator, f.getIndexedField());

    final Generator<BuilderFieldWithMethod, PojoSettings> singleMethod =
        JavaGenerators.<BuilderFieldWithMethod, PojoSettings>methodGen()
            .modifiers(PUBLIC)
            .noGenericTypes()
            .returnType(nextClassTypeVariables)
            .methodName(f -> f.getFieldBuilderMethod().getMethodName().asString())
            .arguments(f -> f.getFieldBuilderMethod().getArguments().map(Argument::formatted))
            .content(content)
            .build()
            .append(
                RefsGen.fieldBuilderMethodRefs(), BuilderFieldWithMethod::getFieldBuilderMethod);

    return Generator.<BuilderField, PojoSettings>emptyGen()
        .appendList(
            singleMethod,
            BuilderField::getBuilderFieldsWithMethod,
            Generator.ofWriterFunction(Writer::println));
  }
}
