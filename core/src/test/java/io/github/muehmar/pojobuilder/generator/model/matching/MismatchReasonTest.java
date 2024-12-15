package io.github.muehmar.pojobuilder.generator.model.matching;

import static org.assertj.core.api.Assertions.assertThat;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.Names;
import io.github.muehmar.pojobuilder.generator.PojoFields;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import org.junit.jupiter.api.Test;

class MismatchReasonTest {
  @Test
  void nonMatchingName_when_fieldAndArgument_then_correctReason() {
    final MismatchReason mismatchReason =
        MismatchReason.nonMatchingName(
            PojoFields.optionalName(), new Argument(Names.id(), Types.string()));

    assertThat(mismatchReason.getReason())
        .isEqualTo("Field 'name' does not match the name of argument 'id'.");
  }

  @Test
  void nonMatchingArgumentType_when_fieldAndArgument_then_correctReason() {
    final MismatchReason mismatchReason =
        MismatchReason.nonMatchingArgumentType(
            PojoFields.optionalName(), new Argument(Names.id(), Types.string()));

    assertThat(mismatchReason.getReason())
        .isEqualTo(
            "The type 'String' of argument 'id' does not match the type 'String' of field 'name'.");
  }

  @Test
  void nonMatchingArgumentCount_when_samplePojoAndEmptyArgumentList_then_correctReason() {
    final MismatchReason mismatchReason =
        MismatchReason.nonMatchingArgumentCount(Pojos.sample(), PList.empty());

    assertThat(mismatchReason.getReason())
        .isEqualTo("The number of arguments (0) does not match the number of fields (3).");
  }
}
