package io.github.muehmar.pojobuilder.generator.model.matching;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Constructor;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class SingleConstructorMatchingResult {
  private final Constructor constructor;
  private final MatchingConstructor matchingConstructor;
  private final PList<MismatchReason> mismatchReasons;

  private SingleConstructorMatchingResult(
      Constructor constructor,
      MatchingConstructor matchingConstructor,
      PList<MismatchReason> mismatchReasons) {
    this.constructor = constructor;
    this.matchingConstructor = matchingConstructor;
    this.mismatchReasons = mismatchReasons;
  }

  public static SingleConstructorMatchingResult ofMatchingConstructor(
      MatchingConstructor matchingConstructor) {
    return new SingleConstructorMatchingResult(
        matchingConstructor.getConstructor(), matchingConstructor, null);
  }

  public static SingleConstructorMatchingResult ofMismatchReasons(
      Constructor constructor, PList<MismatchReason> mismatchReasons) {
    return new SingleConstructorMatchingResult(constructor, null, mismatchReasons);
  }

  public Constructor getConstructor() {
    return constructor;
  }

  public Optional<MatchingConstructor> getMatchingConstructor() {
    return Optional.ofNullable(matchingConstructor);
  }

  public Optional<PList<MismatchReason>> getMismatchReasons() {
    return Optional.ofNullable(mismatchReasons);
  }
}
