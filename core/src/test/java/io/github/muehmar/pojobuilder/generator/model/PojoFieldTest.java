package io.github.muehmar.pojobuilder.generator.model;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import io.github.muehmar.pojobuilder.FieldBuilderMethods;
import io.github.muehmar.pojobuilder.exception.PojoBuilderException;
import io.github.muehmar.pojobuilder.generator.PojoFields;
import io.github.muehmar.pojobuilder.generator.model.matching.FieldArgument;
import io.github.muehmar.pojobuilder.generator.model.matching.SingleArgumentMatchingResult;
import io.github.muehmar.pojobuilder.generator.model.settings.FieldMatching;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class PojoFieldTest {

  @Test
  void builderSetMethodName_when_noPrefix_then_isFieldName() {
    final PojoSettings settings =
        PojoSettings.defaultSettings().withBuilderSetMethodPrefix(empty());
    final Name builderSetMethodName = PojoFields.requiredId().builderSetMethodName(settings);

    assertThat(builderSetMethodName.asString()).isEqualTo("id");
  }

  @Test
  void builderSetMethodName_when_with_then_correctPrefixed() {
    final PojoSettings settings =
        PojoSettings.defaultSettings().withBuilderSetMethodPrefixOpt(Name.fromString("set"));
    final Name builderSetMethodName = PojoFields.requiredId().builderSetMethodName(settings);

    assertThat(builderSetMethodName.asString()).isEqualTo("setId");
  }

  @Test
  void isFieldBuilderMethod_when_everythingOk_then_true() {
    final PojoField field = PojoFields.requiredId();
    final FieldBuilderMethod fieldBuilderMethod =
        FieldBuilderMethods.forField(
            field, Name.fromString("method"), Argument.of(Name.fromString("val"), Types.string()));

    assertThat(field.isFieldBuilderMethod(Name.fromString("Class"), fieldBuilderMethod)).isTrue();
  }

  @Test
  void isFieldBuilderMethod_when_fieldNameDoesNotMatch_then_false() {
    final PojoField field = PojoFields.requiredId();
    final FieldBuilderMethod fieldBuilderMethod =
        FieldBuilderMethods.forField(
                field,
                Name.fromString("method"),
                Argument.of(Name.fromString("val"), Types.string()))
            .withFieldName(Name.fromString("anyOtherName"));

    assertThat(field.isFieldBuilderMethod(Name.fromString("Class"), fieldBuilderMethod)).isFalse();
  }

  @Test
  void
      isFieldBuilderMethod_when_fieldNameDoesNotMatchAndReturnTypeDoesNotMatch_then_falseAndDoNotThrow() {
    final PojoField field = PojoFields.requiredId();
    final FieldBuilderMethod fieldBuilderMethod =
        FieldBuilderMethods.forField(
                field,
                Name.fromString("method"),
                Argument.of(Name.fromString("val"), Types.string()))
            .withFieldName(Name.fromString("anyOtherName"))
            .withReturnType(Types.voidType());

    assertThat(field.isFieldBuilderMethod(Name.fromString("Class"), fieldBuilderMethod)).isFalse();
  }

  @Test
  void isFieldBuilderMethod_when_returnTypeDoesNotMatchForRequiredField_then_throwException() {
    final PojoField field = PojoFields.requiredId();
    final FieldBuilderMethod fieldBuilderMethod =
        FieldBuilderMethods.forField(
                field,
                Name.fromString("method"),
                Argument.of(Name.fromString("val"), Types.string()))
            .withReturnType(Types.string());

    assertThatExceptionOfType(PojoBuilderException.class)
        .isThrownBy(() -> field.isFieldBuilderMethod(Name.fromString("Class"), fieldBuilderMethod));
  }

  @Test
  void isFieldBuilderMethod_when_optionalReturnTypeForRequiredField_then_throwException() {
    final PojoField field = PojoFields.requiredId();
    final FieldBuilderMethod fieldBuilderMethod =
        FieldBuilderMethods.forField(
                field,
                Name.fromString("method"),
                Argument.of(Name.fromString("val"), Types.string()))
            .withReturnType(Types.optional(field.getType()));

    assertThatExceptionOfType(PojoBuilderException.class)
        .isThrownBy(() -> field.isFieldBuilderMethod(Name.fromString("Class"), fieldBuilderMethod));
  }

  @Test
  void isFieldBuilderMethod_when_returnTypeDoesNotMatchForOptionalField_then_throwException() {
    final PojoField field = PojoFields.optionalName();
    final FieldBuilderMethod fieldBuilderMethod =
        FieldBuilderMethods.forField(
                field,
                Name.fromString("method"),
                Argument.of(Name.fromString("val"), Types.string()))
            .withReturnType(Types.integer());

    assertThatExceptionOfType(PojoBuilderException.class)
        .isThrownBy(() -> field.isFieldBuilderMethod(Name.fromString("Class"), fieldBuilderMethod));
  }

  @Test
  void isFieldBuilderMethod_when_returnTypeIsOptionalOfOptionalField_then_true() {
    final PojoField field = PojoFields.optionalName();
    final FieldBuilderMethod fieldBuilderMethod =
        FieldBuilderMethods.forField(
                field,
                Name.fromString("method"),
                Argument.of(Name.fromString("val"), Types.string()))
            .withReturnType(Types.optional(field.getType()));

    assertThat(field.isFieldBuilderMethod(Name.fromString("Class"), fieldBuilderMethod)).isTrue();
  }

  @ParameterizedTest
  @EnumSource(FieldMatching.class)
  void match_when_typeDoesNotMatch_then_doesNotMatchIndependentOfFieldMatching(
      FieldMatching fieldMatching) {
    final PojoField pojoField = PojoFields.requiredId();
    final Argument argument = new Argument(pojoField.getName(), Types.string());

    final SingleArgumentMatchingResult result = pojoField.match(argument, fieldMatching);

    assertThat(result.getFieldArgument()).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"id", "id_"})
  void match_when_fieldMatchingOnlyType_then_matchesIndependentOfTheName(String name) {
    final PojoField pojoField = PojoFields.requiredId();
    final Argument argument = new Argument(Name.fromString(name), pojoField.getType());

    final SingleArgumentMatchingResult result = pojoField.match(argument, FieldMatching.TYPE);

    final FieldArgument expectedFieldArgument =
        new FieldArgument(pojoField, argument, OptionalFieldRelation.SAME_TYPE);
    assertThat(result.getFieldArgument()).isEqualTo(Optional.of(expectedFieldArgument));
  }

  @Test
  void match_when_fieldMatchingTypeAndName_then_doesNotMatchForDifferentName() {
    final PojoField pojoField = PojoFields.requiredId();
    final Argument argument = new Argument(Name.fromString("id_"), pojoField.getType());

    final SingleArgumentMatchingResult result =
        pojoField.match(argument, FieldMatching.TYPE_AND_NAME);

    assertThat(result.getFieldArgument()).isEmpty();
  }

  @Test
  void match_when_fieldMatchingTypeAndName_then_doesMatchForSameName() {
    final PojoField pojoField = PojoFields.requiredId();
    final Argument argument = new Argument(pojoField.getName(), pojoField.getType());

    final SingleArgumentMatchingResult result =
        pojoField.match(argument, FieldMatching.TYPE_AND_NAME);

    final FieldArgument expectedFieldArgument =
        new FieldArgument(pojoField, argument, OptionalFieldRelation.SAME_TYPE);
    assertThat(result.getFieldArgument()).isEqualTo(Optional.of(expectedFieldArgument));
  }
}
