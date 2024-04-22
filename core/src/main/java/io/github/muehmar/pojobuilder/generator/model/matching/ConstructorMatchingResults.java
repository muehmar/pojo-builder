package io.github.muehmar.pojobuilder.generator.model.matching;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.exception.PojoBuilderException;
import io.github.muehmar.pojobuilder.generator.model.Constructor;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.settings.FieldMatching;
import java.util.Optional;
import lombok.Value;

@Value
public class ConstructorMatchingResults {
  PList<SingleConstructorMatchingResult> results;

  public Optional<MatchingConstructor> getFirstMatchingConstructor() {
    return results
        .flatMapOptional(SingleConstructorMatchingResult::getMatchingConstructor)
        .headOption();
  }

  public MatchingConstructor getFirstMatchingConstructorOrThrow(
      Pojo pojo, FieldMatching fieldMatching) {
    return getFirstMatchingConstructor()
        .orElseThrow(() -> createNoMatchFoundException(pojo, fieldMatching));
  }

  private PojoBuilderException createNoMatchFoundException(Pojo pojo, FieldMatching fieldMatching) {
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(
        String.format(
            "No matching constructor found for class/record %s.\n", pojo.getPojoClassname()));
    stringBuilder.append(
        String.format(
            "The class/record %s needs a constructor which is:\n", pojo.getPojoClassname()));
    stringBuilder.append(
        "  * Accessible from within the same package, i.e. at least package-private\n");
    stringBuilder.append(
        String.format(
            "  * Has the following %d arguments in the same order:\n", pojo.getFields().size()));
    pojo.getFields()
        .forEach(
            field ->
                stringBuilder.append(formatExpectedArgument(field, fieldMatching).concat("\n")));

    stringBuilder.append("\nThe following accessible constructors are found but do not match:");
    results.forEach(
        result ->
            result
                .getMismatchReasons()
                .ifPresent(
                    mismatchReasons ->
                        stringBuilder
                            .append("\n")
                            .append(
                                formatConstructorMismatch(
                                    result.getConstructor(), mismatchReasons))));

    return new PojoBuilderException(stringBuilder.toString());
  }

  private static String formatExpectedArgument(PojoField pojoField, FieldMatching fieldMatching) {
    final String additionalPossibleType =
        pojoField.isRequired()
            ? ""
            : String.format("or Optional<%s>", pojoField.getType().getName());
    final String nameCondition =
        fieldMatching.equals(FieldMatching.TYPE_AND_NAME)
            ? String.format(" named %s", pojoField.getName())
            : "";
    return "    "
        + String.format(
                "* Argument%s with type %s %s",
                nameCondition, pojoField.getType().getName(), additionalPossibleType)
            .trim();
  }

  private static String formatConstructorMismatch(
      Constructor constructor, PList<MismatchReason> mismatchReasons) {
    final StringBuilder stringBuilder = new StringBuilder();
    final String argumentsFormatted =
        constructor
            .getArguments()
            .map(arg -> String.format("%s %s", arg.getType().getTypeDeclaration(), arg.getName()))
            .mkString(", ");
    stringBuilder.append(String.format("  * %s(%s)", constructor.getName(), argumentsFormatted));
    mismatchReasons.forEach(
        mismatchReason ->
            stringBuilder
                .append("\n")
                .append(String.format("    * Mismatch reason: %s", mismatchReason.getReason())));
    return stringBuilder.toString();
  }
}
