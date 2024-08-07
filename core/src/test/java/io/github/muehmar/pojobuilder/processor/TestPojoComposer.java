package io.github.muehmar.pojobuilder.processor;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.util.Strings;
import io.github.muehmar.pojobuilder.annotations.Ignore;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PackageName;

public class TestPojoComposer {
  private TestPojoComposer() {}

  public static Imports ofPackage(String pkg) {
    final StringBuilder builder = new StringBuilder();
    builder.append(String.format("package %s;\n", pkg));
    return new Imports(builder);
  }

  public static Imports ofPackage(PackageName pkg) {
    return ofPackage(pkg.asString());
  }

  public static class Imports {
    private final StringBuilder builder;

    private Imports(StringBuilder builder) {
      this.builder = builder;
    }

    public Imports withImport(Class<?> clazz) {
      return withImport(clazz.getName());
    }

    public Imports withImport(String clazz) {
      builder.append(String.format("import %s;\n", clazz));
      return this;
    }

    public <T extends Enum<T>> ClassAnnotations annotationEnumParam(
        Class<?> annotation, String metaName, Class<T> metaType, T metaValue) {
      return new ClassAnnotations(builder)
          .annotationEnumParam(annotation, metaName, metaType, metaValue);
    }

    public ClassAnnotations annotationBooleanParam(
        Class<?> annotation, String parameterName, boolean value) {
      return new ClassAnnotations(builder).annotationBooleanParam(annotation, parameterName, value);
    }

    public ClassAnnotations annotationStringParam(
        Class<?> annotation, String parameterName, String value) {
      return new ClassAnnotations(builder).annotationStringParam(annotation, parameterName, value);
    }

    public ClassAnnotations annotation(Class<?> annotation) {
      return new ClassAnnotations(builder).annotation(annotation);
    }

    public PojoFields className(String name) {
      return new ClassName(builder).className(name);
    }

    public PojoFields className(Name name) {
      return new ClassName(builder).className(name);
    }
  }

  public static class ClassAnnotations {
    private final StringBuilder builder;

    public ClassAnnotations(StringBuilder builder) {
      this.builder = builder;
    }

    public ClassAnnotations annotation(Class<?> annotation) {
      builder.append(String.format("@%s\n", annotation.getSimpleName()));
      return this;
    }

    public <T extends Enum<T>> ClassAnnotations annotationEnumParam(
        Class<?> annotation, String metaName, Class<T> metaType, T metaValue) {
      builder.append(
          String.format(
              "@%s(%s = %s.%s)\n",
              annotation.getSimpleName(), metaName, metaType.getName(), metaValue.name()));
      return this;
    }

    public ClassAnnotations annotationBooleanParam(
        Class<?> annotation, String parameterName, boolean value) {
      builder.append(
          String.format("@%s(%s = %s)\n", annotation.getSimpleName(), parameterName, value));
      return this;
    }

    public ClassAnnotations annotationStringParam(
        Class<?> annotation, String parameterName, String value) {
      builder.append(
          String.format("@%s(%s = \"%s\")\n", annotation.getSimpleName(), parameterName, value));
      return this;
    }

    public PojoFields className(String name) {
      return new ClassName(builder).className(name);
    }

    public PojoFields className(Name name) {
      return new ClassName(builder).className(name);
    }
  }

  public static class ClassName {
    private final StringBuilder builder;

    public ClassName(StringBuilder builder) {
      this.builder = builder;
    }

    public PojoFields className(String name) {
      builder.append(String.format("public class %s {\n", name));
      return new PojoFields(builder, name);
    }

    public PojoFields className(Name name) {
      return className(name.asString());
    }
  }

  public static class PojoFields {
    private final StringBuilder builder;
    private final String className;
    private final PList<TypeAndName> fields;

    private PojoFields(StringBuilder builder, String className, PList<TypeAndName> fields) {
      this.builder = builder;
      this.className = className;
      this.fields = fields;
    }

    private PojoFields(StringBuilder builder, String className) {
      this.builder = builder;
      this.className = className;
      this.fields = PList.empty();
    }

    public PojoFields withConstant(String type, String name) {
      builder.append(String.format("  private static final %s %s;\n", type, name));
      return new PojoFields(builder, className, fields);
    }

    public PojoFields withField(String type, String name) {
      builder.append(String.format("  private final %s %s;\n", type, name));
      return new PojoFields(builder, className, fields.add(new TypeAndName(type, name)));
    }

    public PojoFields withField(String type, String name, Class<?> annotation) {
      return withField(type, name, annotation.getName());
    }

    public PojoFields withField(String type, String name, String annotation) {
      final String finalModifier = annotation.equals(Ignore.class.getName()) ? "" : "final";
      final String className = annotation.substring(annotation.lastIndexOf(".") + 1);
      builder.append(String.format("  @%s\n", className));
      builder.append(String.format("  private %s %s %s;\n", finalModifier, type, name));
      final PList<TypeAndName> fields =
          annotation.equals(Ignore.class.getName())
              ? this.fields
              : this.fields.add(new TypeAndName(type, name));
      return new PojoFields(builder, this.className, fields);
    }

    public String create() {
      return new PojoMethod(builder).create();
    }

    public PojoConstructor constructor(Class<?>... exceptions) {
      final String args =
          fields
              .map(TypeAndName::unwrapOptionalClass)
              .map(tan -> String.format("%s %s", tan.type, tan.name))
              .mkString(",");
      final String exceptionsFormatted =
          Strings.surroundIfNotEmpty(
              "throws ", PList.of(exceptions).map(Class::getCanonicalName).mkString(", "), "");
      builder.append(String.format("  public %s(%s) %s {\n", className, args, exceptionsFormatted));
      fields.forEach(
          tan -> {
            if (tan.isOptionalClass()) {
              builder.append(
                  String.format("    this.%s = Optional.ofNullable(%s);\n", tan.name, tan.name));
            } else {
              builder.append(String.format("    this.%s = %s;\n", tan.name, tan.name));
            }
          });
      builder.append("  }\n");
      return new PojoConstructor(builder);
    }

    private static class TypeAndName {
      private final String type;
      private final String name;

      public TypeAndName(String type, String name) {
        this.type = type;
        this.name = name;
      }

      public boolean isOptionalClass() {
        return type.startsWith("Optional<") && type.endsWith(">");
      }

      public TypeAndName unwrapOptionalClass() {
        if (isOptionalClass()) {
          return new TypeAndName(type.substring("Optional<".length(), type.length() - 1), name);
        } else {
          return this;
        }
      }
    }
  }

  public static class PojoConstructor {
    private final StringBuilder builder;

    private PojoConstructor(StringBuilder builder) {
      this.builder = builder;
    }

    public PojoMethod getter(String returnType, String fieldName) {
      return new PojoMethod(builder).getter(returnType, fieldName);
    }

    public PojoMethod getterWithAnnotation(String returnType, String fieldName, String annotation) {
      return new PojoMethod(builder).getterWithAnnotation(returnType, fieldName, annotation);
    }

    public String create() {
      return new PojoMethod(builder).create();
    }
  }

  public static class PojoMethod {
    private final StringBuilder builder;

    private PojoMethod(StringBuilder builder) {
      this.builder = builder;
    }

    public PojoMethod getter(String returnType, String fieldName) {
      return getterWithAnnotation(returnType, fieldName, "");
    }

    public PojoMethod getterWithAnnotation(String returnType, String fieldName, String annotation) {
      return methodWithAnnotation(
          returnType,
          Name.fromString(fieldName).toPascalCase().prefix("get").asString(),
          "return null",
          annotation);
    }

    public PojoMethod method(String returnType, String methodName, String content) {
      return methodWithAnnotation(returnType, methodName, content, "");
    }

    public PojoMethod methodWithAnnotation(
        String returnType, String methodName, String content, String annotation) {
      return methodWithAnnotation(returnType, methodName, content, annotation, "");
    }

    public PojoMethod methodWithAnnotation(
        String returnType, String methodName, String content, String annotation, String arguments) {
      builder.append(String.format("  %s\n", annotation));
      builder.append(String.format("  public %s %s(%s) {\n", returnType, methodName, arguments));
      builder.append(String.format("    %s;\n", content));
      builder.append("  }\n");
      return new PojoMethod(builder);
    }

    public PojoMethod methodWithAnnotation(
        String returnType,
        String methodName,
        String content,
        String annotation,
        String arguments,
        String exceptions) {
      builder.append(String.format("  %s\n", annotation));
      final String formattedExceptions = Strings.surroundIfNotEmpty("throws ", exceptions, "");
      builder.append(
          String.format(
              "  public %s %s(%s) %s {\n", returnType, methodName, arguments, formattedExceptions));
      builder.append(String.format("    %s;\n", content));
      builder.append("  }\n");
      return new PojoMethod(builder);
    }

    public String create() {
      builder.append("}\n");
      return builder.toString();
    }
  }
}
