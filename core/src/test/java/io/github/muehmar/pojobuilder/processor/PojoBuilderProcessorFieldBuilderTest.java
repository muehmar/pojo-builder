package io.github.muehmar.pojobuilder.processor;

import static org.assertj.core.api.Assertions.assertThat;

import ch.bluecare.commons.data.NonEmptyList;
import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.FieldBuilder;
import io.github.muehmar.pojobuilder.generator.model.FieldBuilderMethod;
import io.github.muehmar.pojobuilder.generator.model.FieldBuilderMethodBuilder;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PojoBuilderProcessorFieldBuilderTest extends BaseExtensionProcessorTest {

  @Test
  void run_when_fieldBuilderAnnotationOnMethod_then_extractFieldBuilderMethod() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .withImport(io.github.muehmar.pojobuilder.annotations.FieldBuilder.class)
            .annotation(PojoBuilder.class)
            .className(pojoClassname.getName())
            .withField("Integer", "key")
            .constructor()
            .getter("Integer", "key")
            .methodWithAnnotation(
                "static Integer",
                "sumKey",
                "return Integer.parseInt(a) + b",
                "@FieldBuilder(fieldName=\"key\")",
                "String a, int b, String ... more")
            .create();

    final BaseExtensionProcessorTest.PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(pojoClassname, classString);

    final NonEmptyList<FieldBuilderMethod> methods =
        NonEmptyList.of(
            FieldBuilderMethodBuilder.create()
                .fieldName(Name.fromString("key"))
                .methodName(Name.fromString("sumKey"))
                .returnType(Types.integer())
                .arguments(
                    PList.of(
                        new Argument(Name.fromString("a"), Types.string()),
                        new Argument(Name.fromString("b"), Types.primitiveInt()),
                        new Argument(Name.fromString("more"), Types.varargs(Types.string()))))
                .andAllOptionals()
                .innerClassName(Optional.empty())
                .build());

    final FieldBuilder fieldBuilder = new FieldBuilder(false, methods);

    assertThat(PList.single(fieldBuilder)).isEqualTo(pojoAndSettings.getPojo().getFieldBuilders());
  }

  @Test
  void run_when_fieldBuilderAnnotationOnClass_then_extractFieldBuilderMethod() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        "package "
            + PACKAGE
            + ";\n"
            + "import io.github.muehmar.pojobuilder.annotations.PojoBuilder;\n"
            + "import io.github.muehmar.pojobuilder.annotations.FieldBuilder;\n"
            + "@PojoBuilder\n"
            + "public class "
            + pojoClassname.getSimpleName()
            + " {\n"
            + "  private final String id;\n"
            + "  public "
            + pojoClassname.getSimpleName()
            + "(String id) {\n"
            + "    this.id = id;\n"
            + "  }\n"
            + "\n"
            + "  @FieldBuilder(fieldName = \"id\")\n"
            + "  static class CustomIdBuilder {\n"
            + "    static String randomId(int seed) {\n"
            + "      return \"random\" + seed;\n"
            + "    } \n"
            + "    static String constant() {\n"
            + "      return \"constant\";\n"
            + "    } \n"
            + "    static String varargs(String... args) {\n"
            + "      return String.join(\"\", args);\n"
            + "    } \n"
            + "  }\n"
            + "}";

    final BaseExtensionProcessorTest.PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(pojoClassname, classString);

    final PList<FieldBuilderMethod> methods =
        PList.of(
            FieldBuilderMethodBuilder.create()
                .fieldName(Name.fromString("id"))
                .methodName(Name.fromString("randomId"))
                .returnType(Types.string())
                .arguments(PList.of(new Argument(Name.fromString("seed"), Types.primitiveInt())))
                .andAllOptionals()
                .innerClassName(Optional.of(Name.fromString("CustomIdBuilder")))
                .build(),
            FieldBuilderMethodBuilder.create()
                .fieldName(Name.fromString("id"))
                .methodName(Name.fromString("constant"))
                .returnType(Types.string())
                .arguments(PList.empty())
                .andAllOptionals()
                .innerClassName(Optional.of(Name.fromString("CustomIdBuilder")))
                .build(),
            FieldBuilderMethodBuilder.create()
                .fieldName(Name.fromString("id"))
                .methodName(Name.fromString("varargs"))
                .returnType(Types.string())
                .arguments(
                    PList.single(
                        new Argument(Name.fromString("args"), Types.varargs(Types.string()))))
                .andAllOptionals()
                .innerClassName(Optional.of(Name.fromString("CustomIdBuilder")))
                .build());

    assertThat(1).isEqualTo(pojoAndSettings.getPojo().getFieldBuilders().size());
    assertThat(methods.toHashSet())
        .isEqualTo(
            pojoAndSettings
                .getPojo()
                .getFieldBuilders()
                .apply(0)
                .getMethods()
                .toPList()
                .toHashSet());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void run_when_fieldBuilderAnnotationOnMethod_then_extractTheDisableMethodsFlagCorrectly(
      boolean disableDefaultMethods) {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .withImport(io.github.muehmar.pojobuilder.annotations.FieldBuilder.class)
            .annotation(PojoBuilder.class)
            .className(pojoClassname.getName())
            .withField("Integer", "key")
            .constructor()
            .getter("Integer", "key")
            .methodWithAnnotation(
                "static Integer",
                "sumKey",
                "return Integer.parseInt(a) + b",
                "@FieldBuilder(fieldName=\"key\", disableDefaultMethods="
                    + disableDefaultMethods
                    + ")",
                "String a, int b")
            .create();

    final BaseExtensionProcessorTest.PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(pojoClassname, classString);

    final PList<FieldBuilder> fieldBuilders = pojoAndSettings.getPojo().getFieldBuilders();

    assertThat(fieldBuilders.size()).isEqualTo(1);
    final FieldBuilder fieldBuilder = fieldBuilders.apply(0);

    assertThat(fieldBuilder.isDisableDefaultMethods()).isEqualTo(disableDefaultMethods);
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void run_when_fieldBuilderAnnotationOnClass_then_extractTheDisableMethodsFlagCorrectly(
      boolean disableDefaultMethods) {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        "package "
            + PACKAGE
            + ";\n"
            + "import io.github.muehmar.pojobuilder.annotations.PojoBuilder;\n"
            + "import io.github.muehmar.pojobuilder.annotations.FieldBuilder;\n"
            + "@PojoBuilder\n"
            + "public class "
            + pojoClassname.getSimpleName()
            + " {\n"
            + "  private final String id;\n"
            + "  public "
            + pojoClassname.getSimpleName()
            + "(String id) {\n"
            + "    this.id = id;\n"
            + "  }\n"
            + "\n"
            + "  @FieldBuilder(fieldName = \"id\", disableDefaultMethods="
            + disableDefaultMethods
            + ")\n"
            + "  static class CustomIdBuilder {\n"
            + "    static String randomId(int seed) {\n"
            + "      return \"random\" + seed;\n"
            + "    } \n"
            + "    static String constant() {\n"
            + "      return \"constant\";\n"
            + "    } \n"
            + "    static String varargs(String... args) {\n"
            + "      return String.join(\"\", args);\n"
            + "    } \n"
            + "  }\n"
            + "}";

    final BaseExtensionProcessorTest.PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(pojoClassname, classString);

    final PList<FieldBuilder> fieldBuilders = pojoAndSettings.getPojo().getFieldBuilders();

    assertThat(fieldBuilders.size()).isEqualTo(1);
    final FieldBuilder fieldBuilder = fieldBuilders.apply(0);

    assertThat(fieldBuilder.isDisableDefaultMethods()).isEqualTo(disableDefaultMethods);
  }
}
