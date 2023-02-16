package io.github.muehmar.pojobuilder.processor;

import static io.github.muehmar.pojobuilder.generator.model.Necessity.OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.REQUIRED;
import static io.github.muehmar.pojobuilder.generator.model.type.Types.integer;
import static io.github.muehmar.pojobuilder.generator.model.type.Types.string;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.Ignore;
import io.github.muehmar.pojobuilder.annotations.Nullable;
import io.github.muehmar.pojobuilder.annotations.OptionalDetection;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.Names;
import io.github.muehmar.pojobuilder.generator.PojoFields;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.Constructor;
import io.github.muehmar.pojobuilder.generator.model.Generic;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Necessity;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.type.Classname;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import io.github.muehmar.pojobuilder.generator.model.type.WildcardType;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class PojoBuilderProcessorTest extends BaseExtensionProcessorTest {

  @Test
  void run_when_simplePojo_then_correctPojoCreated() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotation(PojoBuilder.class)
            .className(className)
            .withField("String", "id")
            .constructor()
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    final PojoField m1 = new PojoField(Names.id(), string(), REQUIRED);
    final PList<PojoField> fields = PList.single(m1);
    final Pojo expected =
        io.github.muehmar.pojobuilder.generator.model.PojoBuilder.create()
            .name(className)
            .pkg(PACKAGE)
            .fields(fields)
            .constructors(
                PList.single(new Constructor(className, fields.map(PojoFields::toArgument))))
            .generics(PList.empty())
            .fieldBuilders(PList.empty())
            .build();

    assertEquals(expected, pojoAndSettings.getPojo());
  }

  @Test
  void run_when_oneOptionalField_then_pojoFieldIsOptionalAndTypeParameterUsedAsType() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .withImport(Optional.class)
            .annotation(PojoBuilder.class)
            .className(className)
            .withField("Optional<String>", "id")
            .constructor()
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    final PojoField m1 = new PojoField(Names.id(), string(), OPTIONAL);
    final PList<PojoField> fields = PList.single(m1);
    final Pojo expected =
        io.github.muehmar.pojobuilder.generator.model.PojoBuilder.create()
            .name(className)
            .pkg(PACKAGE)
            .fields(fields)
            .constructors(
                PList.single(new Constructor(className, fields.map(PojoFields::toArgument))))
            .generics(PList.empty())
            .fieldBuilders(PList.empty())
            .build();

    assertEquals(expected, pojoAndSettings.getPojo());
  }

  @Test
  void run_when_fieldAnnotatedWithNullable_then_pojoFieldIsOptional() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .withImport(Nullable.class)
            .annotation(PojoBuilder.class)
            .className(className)
            .withField("String", "id", Nullable.class)
            .constructor()
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    final PojoField m1 = new PojoField(Names.id(), string(), OPTIONAL);
    final PList<PojoField> fields = PList.single(m1);
    final Pojo expected =
        io.github.muehmar.pojobuilder.generator.model.PojoBuilder.create()
            .name(className)
            .pkg(PACKAGE)
            .fields(fields)
            .constructors(
                PList.single(new Constructor(className, fields.map(PojoFields::toArgument))))
            .generics(PList.empty())
            .fieldBuilders(PList.empty())
            .build();

    assertEquals(expected, pojoAndSettings.getPojo());
  }

  @ParameterizedTest
  @EnumSource(OptionalDetection.class)
  void
      run_when_fieldAnnotatedWithNullableAndDifferentDetection_then_pojoFieldIsOptionalOnlyIfNullableAnnotationDetection(
          OptionalDetection optionalDetection) {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .withImport(Nullable.class)
            .annotationEnumParam(
                PojoBuilder.class, "optionalDetection", OptionalDetection.class, optionalDetection)
            .className(className)
            .withField("String", "id", Nullable.class)
            .constructor()
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    final Necessity necessity =
        optionalDetection.equals(OptionalDetection.NULLABLE_ANNOTATION) ? OPTIONAL : REQUIRED;
    final PojoField m1 = new PojoField(Names.id(), string(), necessity);
    final PList<PojoField> fields = PList.single(m1);
    final Pojo expected =
        io.github.muehmar.pojobuilder.generator.model.PojoBuilder.create()
            .name(className)
            .pkg(PackageName.fromString("io.github.muehmar"))
            .fields(fields)
            .constructors(
                PList.single(new Constructor(className, fields.map(PojoFields::toArgument))))
            .generics(PList.empty())
            .fieldBuilders(PList.empty())
            .build();

    assertEquals(expected, pojoAndSettings.getPojo());
  }

  @ParameterizedTest
  @EnumSource(OptionalDetection.class)
  void
      run_when_optionalFieldAndDifferentDetection_then_pojoFieldIsOptionalOnlyIfOptionalClassDetection(
          OptionalDetection optionalDetection) {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .withImport(Optional.class)
            .annotationEnumParam(
                PojoBuilder.class, "optionalDetection", OptionalDetection.class, optionalDetection)
            .className(className)
            .withField("Optional<String>", "id")
            .constructor()
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    final Necessity required =
        optionalDetection.equals(OptionalDetection.OPTIONAL_CLASS) ? OPTIONAL : REQUIRED;
    final Type type = required.isRequired() ? Types.optional(string()) : string();

    final PojoField m1 = new PojoField(Names.id(), type, required);
    final PList<PojoField> fields = PList.single(m1);
    final Pojo expected =
        io.github.muehmar.pojobuilder.generator.model.PojoBuilder.create()
            .name(className)
            .pkg(PACKAGE)
            .fields(fields)
            .constructors(
                PList.single(
                    new Constructor(
                        className, PList.single(new Argument(Names.id(), Types.string())))))
            .generics(PList.empty())
            .fieldBuilders(PList.empty())
            .build();

    assertEquals(expected, pojoAndSettings.getPojo());
  }

  @Test
  void run_when_fieldAnnotatedWithIgnore_then_fieldIgnored() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .withImport(Ignore.class)
            .annotation(PojoBuilder.class)
            .className(className)
            .withField("String", "id", Ignore.class)
            .constructor()
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    final PList<PojoField> fields = PList.empty();
    final Pojo expected =
        io.github.muehmar.pojobuilder.generator.model.PojoBuilder.create()
            .name(className)
            .pkg(PACKAGE)
            .fields(fields)
            .constructors(PList.single(new Constructor(className, PList.empty())))
            .generics(PList.empty())
            .fieldBuilders(PList.empty())
            .build();

    assertEquals(expected, pojoAndSettings.getPojo());
  }

  @Test
  void run_when_primitiveFields_then_fieldsCorrectCreated() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotation(PojoBuilder.class)
            .className(className)
            .withField("boolean", "b")
            .withField("int", "i")
            .withField("short", "s")
            .withField("long", "l")
            .withField("float", "f")
            .withField("double", "d")
            .withField("byte", "by")
            .withField("char", "c")
            .constructor()
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    final PojoField f1 = new PojoField(Name.fromString("b"), Types.primitiveBoolean(), REQUIRED);
    final PojoField f2 = new PojoField(Name.fromString("i"), Types.primitiveInt(), REQUIRED);
    final PojoField f3 = new PojoField(Name.fromString("s"), Types.primitiveShort(), REQUIRED);
    final PojoField f4 = new PojoField(Name.fromString("l"), Types.primitiveLong(), REQUIRED);
    final PojoField f5 = new PojoField(Name.fromString("f"), Types.primitiveFloat(), REQUIRED);
    final PojoField f6 = new PojoField(Name.fromString("d"), Types.primitiveDouble(), REQUIRED);
    final PojoField f7 = new PojoField(Name.fromString("by"), Types.primitiveByte(), REQUIRED);
    final PojoField f8 = new PojoField(Name.fromString("c"), Types.primitiveChar(), REQUIRED);
    final PList<PojoField> fields = PList.of(f1, f2, f3, f4, f5, f6, f7, f8);
    final Pojo expected =
        io.github.muehmar.pojobuilder.generator.model.PojoBuilder.create()
            .name(className)
            .pkg(PACKAGE)
            .fields(fields)
            .constructors(
                PList.single(new Constructor(className, fields.map(PojoFields::toArgument))))
            .generics(PList.empty())
            .fieldBuilders(PList.empty())
            .build();

    assertEquals(expected, pojoAndSettings.getPojo());
  }

  @Test
  void run_when_arrays_then_fieldsCorrectCreated() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .withImport(Map.class)
            .annotation(PojoBuilder.class)
            .className(className)
            .withField("Map<String, Integer>[]", "data")
            .withField("byte[]", "key")
            .constructor()
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    final PojoField f1 =
        new PojoField(
            Name.fromString("data"), Types.array(Types.map(string(), integer())), REQUIRED);
    final PojoField f2 =
        new PojoField(Name.fromString("key"), Types.array(Types.primitiveByte()), REQUIRED);
    final PList<PojoField> fields = PList.of(f1, f2);
    final Pojo expected =
        io.github.muehmar.pojobuilder.generator.model.PojoBuilder.create()
            .name(className)
            .pkg(PACKAGE)
            .fields(fields)
            .constructors(
                PList.single(new Constructor(className, fields.map(PojoFields::toArgument))))
            .generics(PList.empty())
            .fieldBuilders(PList.empty())
            .build();

    assertEquals(expected, pojoAndSettings.getPojo());
  }

  @Test
  void run_when_constantDefined_then_constantIgnored() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotation(PojoBuilder.class)
            .className(className)
            .withField("String", "id")
            .withConstant("String", "CONSTANT_VAL=\"123456\"")
            .constructor()
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    final PojoField m1 = new PojoField(Names.id(), string(), REQUIRED);
    final PList<PojoField> fields = PList.single(m1);
    final Pojo expected =
        io.github.muehmar.pojobuilder.generator.model.PojoBuilder.create()
            .name(className)
            .pkg(PACKAGE)
            .fields(fields)
            .constructors(
                PList.single(new Constructor(className, fields.map(PojoFields::toArgument))))
            .generics(PList.empty())
            .fieldBuilders(PList.empty())
            .build();

    assertEquals(expected, pojoAndSettings.getPojo());
  }

  @Test
  void run_when_genericClass_then_correctGenerics() {
    final Name className = randomClassName();

    final String classString =
        "package "
            + PACKAGE
            + ";\n"
            + "import java.util.List;\n"
            + "import io.github.muehmar.pojobuilder.annotations.PojoBuilder;\n"
            + "@PojoBuilder\n"
            + "public class "
            + className
            + "<T extends List<String> & Comparable<T>> {\n"
            + "  private final String prop1;\n"
            + "  private final T data;\n"
            + "\n"
            + "  public "
            + className
            + "(String prop1, T data) {\n"
            + "    this.prop1 = prop1;\n"
            + "    this.data = data;\n"
            + "  }\n"
            + "}";

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    final PojoField f1 = new PojoField(Name.fromString("prop1"), string(), REQUIRED);
    final PojoField f2 =
        new PojoField(Name.fromString("data"), Types.typeVariable(Name.fromString("T")), REQUIRED);
    final PList<PojoField> fields = PList.of(f1, f2);

    final Generic generic =
        new Generic(
            Name.fromString("T"),
            PList.of(
                Types.list(string()), Types.comparable(Types.typeVariable(Name.fromString("T")))));

    final Pojo expected =
        io.github.muehmar.pojobuilder.generator.model.PojoBuilder.create()
            .name(className)
            .pkg(PACKAGE)
            .fields(fields)
            .constructors(
                PList.single(new Constructor(className, fields.map(PojoFields::toArgument))))
            .generics(PList.single(generic))
            .fieldBuilders(PList.empty())
            .build();

    assertEquals(expected, pojoAndSettings.getPojo());
  }

  @Test
  void run_when_wildcardTypeVariable_then_correctWildcard() {
    final Name className = randomClassName();

    final String classString =
        "package "
            + PACKAGE
            + ";\n"
            + "import io.github.muehmar.pojobuilder.annotations.PojoBuilder;\n"
            + "@PojoBuilder\n"
            + "public class "
            + className
            + " {\n"
            + "  private final Enum<?> code;\n"
            + "\n"
            + "  public "
            + className
            + "(Enum<?> code) {\n"
            + "    this.code = code;\n"
            + "  }\n"
            + "}";

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    final Type type =
        Types.declaredType(
            Classname.fromFullClassName("Enum"),
            Optional.of(PackageName.javaLang()),
            PList.single(Type.fromSpecificType(WildcardType.create())));

    final PojoField f1 = new PojoField(Name.fromString("code"), type, REQUIRED);
    final PList<PojoField> fields = PList.of(f1);

    final Pojo expected =
        io.github.muehmar.pojobuilder.generator.model.PojoBuilder.create()
            .name(className)
            .pkg(PACKAGE)
            .fields(fields)
            .constructors(
                PList.single(new Constructor(className, fields.map(PojoFields::toArgument))))
            .generics(PList.empty())
            .fieldBuilders(PList.empty())
            .build();

    assertEquals(expected, pojoAndSettings.getPojo());
  }
}
