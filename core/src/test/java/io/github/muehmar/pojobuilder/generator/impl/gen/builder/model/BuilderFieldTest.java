package io.github.muehmar.pojobuilder.generator.impl.gen.builder.model;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.bluecare.commons.data.NonEmptyList;
import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.FieldBuilderMethods;
import io.github.muehmar.pojobuilder.annotations.FullBuilderFieldOrder;
import io.github.muehmar.pojobuilder.generator.BuilderFields;
import io.github.muehmar.pojobuilder.generator.PojoFields;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.FieldBuilder;
import io.github.muehmar.pojobuilder.generator.model.FieldBuilderMethod;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BuilderFieldTest {

  @Test
  void hasFieldBuilder_when_noBuilderPresent_then_false() {
    final BuilderField builderField =
        BuilderFields.of(Pojos.sample(), Pojos.sample().getFields().apply(0), 0)
            .withFieldBuilder(empty());

    assertFalse(builderField.hasFieldBuilder());
  }

  @Test
  void hasFieldBuilder_when_builderPresent_then_true() {
    final FieldBuilderMethod fieldBuilderMethod =
        FieldBuilderMethods.forField(
            Pojos.sample().getFields().apply(0),
            Name.fromString("method"),
            new Argument(Name.fromString("arg"), Types.string()));

    final FieldBuilder fieldBuilder = new FieldBuilder(false, NonEmptyList.of(fieldBuilderMethod));

    final BuilderField builderField =
        BuilderFields.of(Pojos.sample(), Pojos.sample().getFields().apply(0), 0)
            .withFieldBuilder(Optional.of(fieldBuilder));

    assertTrue(builderField.hasFieldBuilder());
  }

  @Test
  void isDisableDefaultMethods_when_noBuilderPresent_then_false() {
    final BuilderField builderField =
        BuilderFields.of(Pojos.sample(), Pojos.sample().getFields().apply(0), 0)
            .withFieldBuilder(empty());

    assertFalse(builderField.isDisableDefaultMethods());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void isDisableDefaultMethods_when_builderPresent_then_returnValueFromFieldBuilder(
      boolean disableDefaultMethods) {
    final FieldBuilderMethod fieldBuilderMethod =
        FieldBuilderMethods.forField(
            Pojos.sample().getFields().apply(0),
            Name.fromString("method"),
            new Argument(Name.fromString("arg"), Types.string()));

    final FieldBuilder fieldBuilder =
        new FieldBuilder(disableDefaultMethods, NonEmptyList.of(fieldBuilderMethod));

    final BuilderField builderField =
        BuilderFields.of(Pojos.sample(), Pojos.sample().getFields().apply(0), 0)
            .withFieldBuilder(Optional.of(fieldBuilder));

    assertEquals(disableDefaultMethods, builderField.isDisableDefaultMethods());
  }

  @Test
  void isFieldOptional_when_requiredField_then_false() {
    final BuilderField builderField =
        BuilderFields.of(Pojos.sample(), PojoFields.requiredMap(), 0).withFieldBuilder(empty());

    assertFalse(builderField.isFieldOptional());
  }

  @Test
  void isFieldOptional_when_optionalField_then_true() {
    final BuilderField builderField =
        BuilderFields.of(Pojos.sample(), PojoFields.optionalName(), 0).withFieldBuilder(empty());

    assertTrue(builderField.isFieldOptional());
  }

  @Test
  void allFromPojo_when_requiredFieldsFirst_then_correctOrder() {
    final PList<BuilderField> builderFields =
        BuilderField.allFromPojo(Pojos.sample2(), FullBuilderFieldOrder.REQUIRED_FIELDS_FIRST);
    assertEquals(
        PList.of("id", "username", "zip", "nickname"),
        builderFields.map(bf -> bf.getField().getName().asString()));
  }

  @Test
  void allFromPojo_when_declarationOrder_then_correctOrder() {
    final PList<BuilderField> builderFields =
        BuilderField.allFromPojo(Pojos.sample2(), FullBuilderFieldOrder.DECLARATION_ORDER);
    assertEquals(
        PList.of("id", "zip", "username", "nickname"),
        builderFields.map(bf -> bf.getField().getName().asString()));
  }
}
