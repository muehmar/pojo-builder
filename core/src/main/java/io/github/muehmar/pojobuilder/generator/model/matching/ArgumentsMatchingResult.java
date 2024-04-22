package io.github.muehmar.pojobuilder.generator.model.matching;

import ch.bluecare.commons.data.PList;
import java.util.Optional;
import java.util.function.Function;
import lombok.Value;

@Value
public class ArgumentsMatchingResult {
  PList<SingleArgumentMatchingResult> matchingResults;

  public static ArgumentsMatchingResult fromSingleArgumentMatchingResult(
      SingleArgumentMatchingResult matchingResult) {
    return new ArgumentsMatchingResult(PList.single(matchingResult));
  }

  public Optional<PList<FieldArgument>> getFieldArguments() {
    final PList<FieldArgument> fieldArguments =
        matchingResults.flatMapOptional(SingleArgumentMatchingResult::getFieldArgument);
    if (fieldArguments.size() == matchingResults.size()) {
      return Optional.of(fieldArguments);
    }
    return Optional.empty();
  }

  public <T> T fold(
      Function<PList<FieldArgument>, T> onFieldArguments,
      Function<PList<MismatchReason>, T> onMismatchReasons) {
    return getFieldArguments()
        .map(onFieldArguments)
        .orElseGet(
            () -> {
              final PList<MismatchReason> mismatchReasons =
                  matchingResults.flatMapOptional(SingleArgumentMatchingResult::getMismatchReason);
              return onMismatchReasons.apply(mismatchReasons);
            });
  }
}
