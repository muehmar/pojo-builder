package io.github.muehmar.pojobuilder.generator.model.matching;

import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class SingleArgumentMatchingResult {
  private final FieldArgument fieldArgument;
  private final MismatchReason mismatchReason;

  private SingleArgumentMatchingResult(FieldArgument fieldArgument, MismatchReason mismatchReason) {
    this.fieldArgument = fieldArgument;
    this.mismatchReason = mismatchReason;
  }

  public static SingleArgumentMatchingResult fromFieldArgument(FieldArgument fieldArgument) {
    return new SingleArgumentMatchingResult(fieldArgument, null);
  }

  public static SingleArgumentMatchingResult fromMismatchReason(MismatchReason mismatchReason) {
    return new SingleArgumentMatchingResult(null, mismatchReason);
  }

  public Optional<FieldArgument> getFieldArgument() {
    return Optional.ofNullable(fieldArgument);
  }

  public Optional<MismatchReason> getMismatchReason() {
    return Optional.ofNullable(mismatchReason);
  }
}
