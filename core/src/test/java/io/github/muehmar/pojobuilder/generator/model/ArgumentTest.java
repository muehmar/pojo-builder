package io.github.muehmar.pojobuilder.generator.model;

import static io.github.muehmar.pojobuilder.generator.model.Necessity.OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.REQUIRED;
import static io.github.muehmar.pojobuilder.generator.model.type.Types.optional;
import static io.github.muehmar.pojobuilder.generator.model.type.Types.string;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.muehmar.pojobuilder.generator.Names;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ArgumentTest {

  @ParameterizedTest
  @EnumSource(Necessity.class)
  void getRelation_when_typesAreTheSame_then_sameType(Necessity necessity) {
    final Argument argument = new Argument(Names.id(), string());
    final PojoField field = new PojoField(Name.fromString("extId"), string(), necessity);

    assertThat(argument.getRelationFromField(field))
        .isEqualTo(Optional.of(OptionalFieldRelation.SAME_TYPE));
  }

  @Test
  void
      getRelation_when_argumentTypeWrappedIntoOptionalAndFieldNotRequired_then_relationIsWrappedIntoOptional() {
    final Argument argument = new Argument(Names.id(), optional(string()));
    final PojoField field = new PojoField(Name.fromString("extId"), string(), OPTIONAL);

    assertThat(argument.getRelationFromField(field))
        .isEqualTo(Optional.of(OptionalFieldRelation.WRAP_INTO_OPTIONAL));
  }

  @Test
  void getRelation_when_argumentTypeWrappedIntoOptionalAndFieldRequired_then_noRelation() {
    final Argument argument = new Argument(Names.id(), optional(string()));
    final PojoField field = new PojoField(Name.fromString("extId"), string(), REQUIRED);

    assertThat(argument.getRelationFromField(field)).isEmpty();
  }

  @Test
  void getRelation_when_fieldTypeWrappedIntoOptionalAndFieldRequired_then_noRelation() {
    final Argument argument = new Argument(Names.id(), string());
    final PojoField field = new PojoField(Name.fromString("extId"), optional(string()), REQUIRED);

    assertThat(argument.getRelationFromField(field)).isEmpty();
  }

  @Test
  void formatted_when_calledForSimpleArgument_then_correctFormatted() {
    final Argument argument = new Argument(Names.id(), string());
    assertThat(argument.formatted()).isEqualTo("String id");
  }

  @Test
  void formatted_when_calledForComplexArgumentType_then_correctFormatted() {
    final Argument argument =
        new Argument(
            Name.fromString("map"),
            Types.map(Types.typeVariable(Name.fromString("T")), Types.optional(string())));
    assertThat(argument.formatted()).isEqualTo("Map<T,Optional<String>> map");
  }
}
