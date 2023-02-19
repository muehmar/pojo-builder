package io.github.muehmar.pojobuilder.generator.impl.gen.instantiation;

import static io.github.muehmar.pojobuilder.generator.model.Necessity.OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation.SAME_TYPE;
import static io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation.UNWRAP_OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation.WRAP_INTO_OPTIONAL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.muehmar.pojobuilder.generator.Names;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.FieldArgument;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import org.junit.jupiter.api.Test;

class FinalConstructorArgumentTest {

  @Test
  void
      ofFieldVariable_when_variableIsWrappedIntoOptionalAndArgumentIsWrappedIntoOptional_then_correctResult() {
    final Name name = Names.id();
    final PojoField pojoField = new PojoField(name, Types.string(), OPTIONAL);
    final FieldVariable fieldVariable =
        new FieldVariable(Pojos.sample(), pojoField, UNWRAP_OPTIONAL);
    final FieldArgument fieldArgument =
        new FieldArgument(
            pojoField, new Argument(name, Types.optional(Types.string())), WRAP_INTO_OPTIONAL);

    // method call
    final FinalConstructorArgument finalConstructorArgument =
        FinalConstructorArgument.ofFieldVariable(fieldVariable, fieldArgument);

    assertEquals("id", finalConstructorArgument.getFieldString());
    assertEquals(SAME_TYPE, finalConstructorArgument.getRelation());
  }

  @Test
  void
      ofFieldVariable_when_variableIsSameTypeAndArgumentIsWrappedIntoOptional_then_correctResult() {
    final Name name = Names.id();
    final PojoField pojoField = new PojoField(name, Types.string(), OPTIONAL);
    final FieldVariable fieldVariable = new FieldVariable(Pojos.sample(), pojoField, SAME_TYPE);
    final FieldArgument fieldArgument =
        new FieldArgument(
            pojoField, new Argument(name, Types.optional(Types.string())), WRAP_INTO_OPTIONAL);

    // method call
    final FinalConstructorArgument finalConstructorArgument =
        FinalConstructorArgument.ofFieldVariable(fieldVariable, fieldArgument);

    assertEquals("id", finalConstructorArgument.getFieldString());
    assertEquals(WRAP_INTO_OPTIONAL, finalConstructorArgument.getRelation());
  }

  @Test
  void ofFieldVariable_when_variableIsSameTypeAndArgumentIsSameType_then_correctResult() {
    final Name name = Names.id();
    final PojoField pojoField = new PojoField(name, Types.string(), OPTIONAL);
    final FieldVariable fieldVariable =
        new FieldVariable(Pojos.sample(), pojoField, UNWRAP_OPTIONAL);
    final FieldArgument fieldArgument =
        new FieldArgument(pojoField, new Argument(name, Types.optional(Types.string())), SAME_TYPE);

    // method call
    final FinalConstructorArgument finalConstructorArgument =
        FinalConstructorArgument.ofFieldVariable(fieldVariable, fieldArgument);

    assertEquals("id", finalConstructorArgument.getFieldString());
    assertEquals(UNWRAP_OPTIONAL, finalConstructorArgument.getRelation());
  }
}
