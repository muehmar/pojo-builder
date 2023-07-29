package io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard;

import static io.github.muehmar.codegenerator.java.JavaModifier.FINAL;
import static io.github.muehmar.codegenerator.java.JavaModifier.PRIVATE;
import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;
import static io.github.muehmar.codegenerator.java.JavaModifier.STATIC;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Generators.newLine;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.StandardBuilderGenerator.BUILDER_ASSIGNMENT;

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

class FieldBuilderClass {
  private FieldBuilderClass() {}

  public static Generator<BuilderField, PojoSettings> fieldBuilderClass() {
    return JavaGenerators.<BuilderField, PojoSettings>classGen()
        .clazz()
        .nested()
        .packageGen(Generator.emptyGen())
        .noJavaDoc()
        .noAnnotations()
        .modifiers(PUBLIC, STATIC, FINAL)
        .className(f -> classDeclaration(f.getIndexedField()))
        .noSuperClass()
        .noInterfaces()
        .content(builderClassContent())
        .build()
        .append(RefsGen.genericRefs(), BuilderField::getPojo);
  }

  private static String rawClassName(IndexedField field) {
    final String prefix = field.getField().isRequired() ? "" : "Opt";
    return String.format("%sBuilder%d", prefix, field.getIndex());
  }

  private static String classDeclaration(IndexedField field) {
    return rawClassName(field) + field.getPojo().getGenericTypeDeclarationSection();
  }

  private static String nextClassTypeVariables(IndexedField field) {
    return nextRawClassName(field) + field.getPojo().getTypeVariablesSection();
  }

  private static String nextClassDiamond(IndexedField field) {
    return nextRawClassName(field) + field.getPojo().getDiamond();
  }

  private static String nextRawClassName(IndexedField field) {
    return rawClassName(field.withIndex(field.getIndex() + 1));
  }

  public static Generator<BuilderField, PojoSettings> builderClassContent() {
    return BuilderFieldDeclaration.<PojoSettings>builderFieldDeclaration()
        .contraMap(BuilderField::getPojo)
        .append(newLine())
        .append(constructor())
        .append(newLine())
        .append(setMethod())
        .appendConditionally(BuilderField::hasFieldBuilder, fieldBuilderMethods().prependNewLine())
        .appendConditionally(BuilderField::isFieldOptional, setMethodOptional().prependNewLine());
  }

  public static Generator<BuilderField, PojoSettings> constructor() {
    return JavaGenerators.<BuilderField, PojoSettings>constructorGen()
        .modifiers(PRIVATE)
        .className(f -> rawClassName(f.getIndexedField()))
        .singleArgument(
            f -> String.format("Builder%s builder", f.getPojo().getTypeVariablesSection()))
        .content(BUILDER_ASSIGNMENT)
        .build();
  }

  public static Generator<BuilderField, PojoSettings> setMethod() {
    final Generator<BuilderField, PojoSettings> content =
        (f, s, w) ->
            w.println(
                "return new %s(builder.%s(%s));",
                nextClassDiamond(f.getIndexedField()),
                f.getField().builderSetMethodName(s),
                f.getField().getName());

    return JavaGenerators.<BuilderField, PojoSettings>methodGen()
        .modifiers(PUBLIC)
        .noGenericTypes()
        .returnType(f -> nextClassTypeVariables(f.getIndexedField()))
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

  public static Generator<BuilderField, PojoSettings> setMethodOptional() {
    final Generator<BuilderField, PojoSettings> content =
        (f, s, w) ->
            w.println(
                "return new %s(builder.%s(%s));",
                nextClassDiamond(f.getIndexedField()),
                f.getField().builderSetMethodName(s),
                f.getField().getName());

    return JavaGenerators.<BuilderField, PojoSettings>methodGen()
        .modifiers(PUBLIC)
        .noGenericTypes()
        .returnType(f -> nextClassTypeVariables(f.getIndexedField()))
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

  public static Generator<BuilderField, PojoSettings> fieldBuilderMethods() {
    final Generator<BuilderFieldWithMethod, PojoSettings> content =
        (f, s, w) ->
            w.println(
                "return new %s(builder.%s(%s%s.%s(%s)));",
                nextClassDiamond(f.getIndexedField()),
                f.getField().builderSetMethodName(s),
                f.getPojo().getName(),
                f.getFieldBuilderMethod()
                    .getInnerClassName()
                    .map(name -> name.prefix(".").asString())
                    .orElse(""),
                f.getFieldBuilderMethod().getMethodName(),
                f.getFieldBuilderMethod().getArgumentNames().mkString(", "));

    final Function<BuilderFieldWithMethod, String> nextClassTypeVariables =
        f -> nextClassTypeVariables(f.getIndexedField());

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
