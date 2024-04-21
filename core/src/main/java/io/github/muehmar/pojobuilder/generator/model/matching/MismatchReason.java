package io.github.muehmar.pojobuilder.generator.model.matching;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import lombok.Value;

@Value
public class MismatchReason {
  String reason;

  public static MismatchReason nonMatchingArgumentType(PojoField field, Argument argument) {
    final String reason =
        String.format(
            "The type '%s' of argument '%s' does not match the type '%s' of field '%s'.",
            argument.getType().getName(),
            argument.getName(),
            field.getType().getName(),
            field.getName());
    return new MismatchReason(reason);
  }

  public static MismatchReason nonMatchingName(PojoField field, Argument argument) {
    final String reason =
        String.format(
            "Field '%s' does not match the name of argument '%s'.",
            field.getName(), argument.getName());
    return new MismatchReason(reason);
  }

  public static MismatchReason nonMatchingArgumentCount(Pojo pojo, PList<Argument> arguments) {
    return new MismatchReason(
        String.format(
            "The number of arguments (%d) does not match the number of fields (%d).",
            arguments.size(), pojo.getFields().size()));
  }
}
