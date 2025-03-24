package io.github.muehmar.pojobuilder.example.prepopulated;

import static io.github.muehmar.pojobuilder.example.prepopulated.PrePopulatedBuilder.fullPrePopulatedBuilder;
import static io.github.muehmar.pojobuilder.example.prepopulated.PrePopulatedBuilder.prePopulatedBuilder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PrePopulatedTest {
  @Test
  void toBuilder_when_standardBuilderAfterRequired_then_allRequiredPropertiesSet() {
    final PrePopulatedBuilder prePopulatedBuilder =
        prePopulatedBuilder().prop1("prop1").prop2(42).toBuilder();

    final PrePopulated prePopulated = prePopulatedBuilder.build();

    assertThat(prePopulated.getProp1()).isEqualTo("prop1");
    assertThat(prePopulated.getProp2()).isEqualTo(42);
    assertThat(prePopulated.getProp3()).isEmpty();
    assertThat(prePopulated.getProp4()).isEmpty();
  }

  @Test
  void toBuilder_when_standardBuilderAfterAllOptionals_then_allPropertiesSet() {
    final PrePopulatedBuilder prePopulatedBuilder =
        prePopulatedBuilder()
            .prop1("prop1")
            .prop2(42)
            .andAllOptionals()
            .prop3("prop3")
            .prop4(43)
            .toBuilder();

    final PrePopulated prePopulated = prePopulatedBuilder.build();

    assertThat(prePopulated.getProp1()).isEqualTo("prop1");
    assertThat(prePopulated.getProp2()).isEqualTo(42);
    assertThat(prePopulated.getProp3()).contains("prop3");
    assertThat(prePopulated.getProp4()).contains(43);
  }

  @Test
  void toBuilder_when_standardBuilderAfterOptionals_then_allPropertiesSet() {
    final PrePopulatedBuilder prePopulatedBuilder =
        prePopulatedBuilder().prop1("prop1").prop2(42).andOptionals().toBuilder();

    final PrePopulated prePopulated = prePopulatedBuilder.build();

    assertThat(prePopulated.getProp1()).isEqualTo("prop1");
    assertThat(prePopulated.getProp2()).isEqualTo(42);
    assertThat(prePopulated.getProp3()).isEmpty();
    assertThat(prePopulated.getProp4()).isEmpty();
  }

  @Test
  void toBuilder_when_fullBuilderAfterBuild_then_allPropertiesSet() {
    final PrePopulatedBuilder prePopulatedBuilder =
        PrePopulatedBuilder.fullPrePopulatedBuilder()
            .prop1("prop1")
            .prop2(42)
            .prop3("prop3")
            .prop4(43)
            .toBuilder();

    final PrePopulated prePopulated = prePopulatedBuilder.build();

    assertThat(prePopulated.getProp1()).isEqualTo("prop1");
    assertThat(prePopulated.getProp2()).isEqualTo(42);
    assertThat(prePopulated.getProp3()).contains("prop3");
    assertThat(prePopulated.getProp4()).contains(43);
  }

  @Test
  void toBuilder_when_propertyChanged_then_correctPropertyInInstance() {
    final PrePopulatedBuilder prePopulatedBuilder =
        PrePopulatedBuilder.fullPrePopulatedBuilder()
            .prop1("prop1")
            .prop2(42)
            .prop3("prop3")
            .prop4(43)
            .toBuilder();

    final PrePopulated prePopulated = prePopulatedBuilder.prop1("newProp1").build();

    assertThat(prePopulated.getProp1()).isEqualTo("newProp1");
    assertThat(prePopulated.getProp2()).isEqualTo(42);
    assertThat(prePopulated.getProp3()).contains("prop3");
    assertThat(prePopulated.getProp4()).contains(43);
  }

  @ParameterizedTest
  @MethodSource("stagesWithoutToBuilderMethod")
  void intermediateStage_noToBuilderMethodPresent(Class<?> intermediateStage) {

    for (Method declaredMethod : intermediateStage.getDeclaredMethods()) {
      assertThat(declaredMethod.getName()).isNotEqualTo("toBuilder");
    }
  }

  public static Stream<Arguments> stagesWithoutToBuilderMethod() {
    return Stream.of(
            prePopulatedBuilder(),
            prePopulatedBuilder().prop1("prop1"),
            prePopulatedBuilder().prop1("prop1").prop2(42).andAllOptionals().prop3("prop3"),
            fullPrePopulatedBuilder(),
            fullPrePopulatedBuilder().prop1("prop1"),
            fullPrePopulatedBuilder().prop1("prop1").prop2(42),
            fullPrePopulatedBuilder().prop1("prop1").prop2(42).prop3("prop3"))
        .map(Object::getClass)
        .map(Arguments::of);
  }

  @ParameterizedTest
  @MethodSource("stagesWithToBuilderMethod")
  void intermediateStage_toBuilderMethodPresent(Class<?> intermediateStage) {
    final List<String> methodNames =
        Arrays.stream(intermediateStage.getDeclaredMethods())
            .map(Method::getName)
            .collect(Collectors.toList());

    assertThat(methodNames).contains("toBuilder");
  }

  public static Stream<Arguments> stagesWithToBuilderMethod() {
    return Stream.of(
            prePopulatedBuilder().prop1("prop1").prop2(42),
            prePopulatedBuilder().prop1("prop1").prop2(42).andOptionals(),
            prePopulatedBuilder()
                .prop1("prop1")
                .prop2(42)
                .andAllOptionals()
                .prop3("prop3")
                .prop4(43),
            fullPrePopulatedBuilder().prop1("prop1").prop2(42).prop3("prop3").prop4(43))
        .map(Object::getClass)
        .map(Arguments::of);
  }
}
