package io.github.muehmar.pojobuilder.example.fieldbuilder;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class FieldBuilderClassTest {
  @Test
  void randomString_when_twoInstancesCreated_then_notEqualsProp1() {
    final FieldBuilderClass<String> fieldBuilder1 =
        FieldBuilderClassBuilder.<String>create().randomString().prop2("asd").build();
    final FieldBuilderClass<String> fieldBuilder2 =
        FieldBuilderClassBuilder.<String>create().randomString().prop2("asd").build();

    assertThat(fieldBuilder2.getProp1()).isNotEqualTo(fieldBuilder1.getProp1());
  }

  @Test
  void builderForProp1ContainsOnlyCustomMethods() {
    final Class<?> clazz = FieldBuilderClassBuilder.BuilderStages.Builder0.class;
    assertThat(classHasMethod(clazz, "prop1")).isFalse();
    assertThat(classHasMethod(clazz, "randomString")).isTrue();
    assertThat(classHasMethod(clazz, "fromInt")).isTrue();
    assertThat(classHasMethod(clazz, "fromVarargs")).isTrue();
  }

  @Test
  void fromInt_when_usedInBuilder_then_prop1IsIntegerString() {
    final FieldBuilderClass<String> fieldBuilder =
        FieldBuilderClassBuilder.<String>create().fromInt(52).prop2("asd").build();

    assertThat(fieldBuilder.getProp1()).isEqualTo("52");
  }

  @Test
  void fromVarargs_when_usedInBuilder_then_compilesAndCorrectProp1() {
    final FieldBuilderClass<String> fieldBuilder =
        FieldBuilderClassBuilder.<String>create()
            .fromVarargs("hello", "world", "!")
            .prop2("prop2")
            .build();

    assertThat(fieldBuilder.getProp1()).isEqualTo("hello-world-!");
  }

  @Test
  void constant_when_usedInBuilder_then_prop3WithConstantValue() {
    final FieldBuilderClass<String> fieldBuilder =
        FieldBuilderClassBuilder.<String>create()
            .randomString()
            .prop2("asd")
            .andAllOptionals()
            .constant()
            .data("")
            .build();

    assertThat(fieldBuilder.getProp3()).isEqualTo(Optional.of("CONSTANT"));
  }

  @Test
  void supplier_when_usedInBuilder_then_dataFilledFromSupplier() {
    final FieldBuilderClass<String> fieldBuilder =
        FieldBuilderClassBuilder.<String>create()
            .randomString()
            .prop2("asd")
            .andAllOptionals()
            .constant()
            .supplier(() -> "supplierData")
            .build();

    assertThat(fieldBuilder.getData()).isEqualTo(Optional.of("supplierData"));
  }

  private static boolean classHasMethod(Class<?> clazz, String methodName) {
    return Arrays.stream(clazz.getDeclaredMethods())
        .anyMatch(method -> method.getName().equals(methodName));
  }
}
