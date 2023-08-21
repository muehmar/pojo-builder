package io.github.muehmar.pojobuilder.generator.model.type;

import io.github.muehmar.pojobuilder.exception.PojoBuilderException;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassnameParser {
  private static final String PACKAGE_NAME_PATTERN = "[a-z][A-Za-z0-9_$]*";
  private static final String IDENTIFIER_PATTERN = "[A-Z][A-Za-z0-9_$]*";

  private static final Pattern QUALIFIED_CLASS_NAME_PATTERN =
      Pattern.compile(
          String.format(
              "^(?:(%s(?:\\.%s)*)\\.)(%s(?:\\.%s)*)",
              PACKAGE_NAME_PATTERN, PACKAGE_NAME_PATTERN, IDENTIFIER_PATTERN, IDENTIFIER_PATTERN));

  private ClassnameParser() {}

  public static QualifiedClassname parseThrowing(String classname) {
    return parse(classname)
        .orElseThrow(
            () ->
                new PojoBuilderException(
                    "Class "
                        + classname
                        + " cannot be parsed. It does not match the pattern "
                        + QUALIFIED_CLASS_NAME_PATTERN.pattern()));
  }

  public static Optional<QualifiedClassname> parse(String classname) {
    final Matcher matcher = QUALIFIED_CLASS_NAME_PATTERN.matcher(classname);
    if (matcher.find()) {
      final Classname name = Classname.fromString(matcher.group(2));
      final PackageName packageName = PackageName.fromString(matcher.group(1));
      return Optional.of(new QualifiedClassname(name, packageName));
    }

    return Optional.empty();
  }
}
