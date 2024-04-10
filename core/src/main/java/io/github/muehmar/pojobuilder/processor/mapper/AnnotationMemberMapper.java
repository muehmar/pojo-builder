package io.github.muehmar.pojobuilder.processor.mapper;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.Optionals;
import io.github.muehmar.pojobuilder.annotations.ConstructorMatching;
import io.github.muehmar.pojobuilder.annotations.FullBuilderFieldOrder;
import io.github.muehmar.pojobuilder.annotations.OptionalDetection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

public class AnnotationMemberMapper {

  public static final String OPTIONAL_DETECTION = "optionalDetection";
  public static final String PACKAGE_PRIVATE_BUILDER = "packagePrivateBuilder";
  public static final String BUILDER_NAME = "builderName";
  public static final String BUILDER_SET_METHOD_PREFIX = "builderSetMethodPrefix";
  public static final String FULL_BUILDER_FIELD_ORDER = "fullBuilderFieldOrder";
  public static final String ENABLE_FULL_BUILDER = "enableFullBuilder";
  public static final String ENABLE_STANDARD_BUILDER = "enableStandardBuilder";
  public static final String INCLUDE_OUTER_CLASS_NAME = "includeOuterClassName";
  public static final String CONSTRUCTOR_MATCHING = "constructorMatching";

  private AnnotationMemberMapper() {}

  public static Optional<PList<OptionalDetection>> getOptionalDetection(
      AnnotationMirror annotationMirror) {
    return getMember(
        annotationMirror,
        new ExtensionMember<>(
            OPTIONAL_DETECTION,
            v ->
                PList.fromIter((Iterable<Object>) v)
                    .flatMapOptional(
                        o -> {
                          final String s = o.toString();
                          final int index = s.lastIndexOf(".");
                          final String name = index >= 0 ? s.substring(index + 1) : s;
                          return OptionalDetection.fromString(name);
                        })));
  }

  public static Optional<Boolean> getPackagePrivateBuilder(AnnotationMirror annotationMirror) {
    return getMember(
        annotationMirror, new ExtensionMember<>(PACKAGE_PRIVATE_BUILDER, Boolean.class::cast));
  }

  public static Optional<String> getBuilderName(AnnotationMirror annotationMirror) {
    return getMember(annotationMirror, new ExtensionMember<>(BUILDER_NAME, String.class::cast));
  }

  public static Optional<String> getBuilderSetMethodPrefix(AnnotationMirror annotationMirror) {
    return getMember(
        annotationMirror, new ExtensionMember<>(BUILDER_SET_METHOD_PREFIX, String.class::cast));
  }

  public static Optional<Boolean> getEnableStandardBuilder(AnnotationMirror annotationMirror) {
    return getMember(
        annotationMirror, new ExtensionMember<>(ENABLE_STANDARD_BUILDER, Boolean.class::cast));
  }

  public static Optional<Boolean> getEnableFullBuilder(AnnotationMirror annotationMirror) {
    return getMember(
        annotationMirror, new ExtensionMember<>(ENABLE_FULL_BUILDER, Boolean.class::cast));
  }

  public static Optional<FullBuilderFieldOrder> getFullBuilderFieldOrder(
      AnnotationMirror annotationMirror) {
    return getMember(
        annotationMirror,
        new ExtensionMember<>(
            FULL_BUILDER_FIELD_ORDER,
            o ->
                FullBuilderFieldOrder.fromString(o.toString())
                    .orElseThrow(IllegalArgumentException::new)));
  }

  public static Optional<ConstructorMatching> getConstructorMatching(
      AnnotationMirror annotationMirror) {
    return getMember(
        annotationMirror,
        new ExtensionMember<>(
            CONSTRUCTOR_MATCHING,
            o ->
                ConstructorMatching.fromString(o.toString())
                    .orElseThrow(IllegalArgumentException::new)));
  }

  public static Optional<Boolean> getIncludeOuterClassName(AnnotationMirror annotationMirror) {
    return getMember(
        annotationMirror, new ExtensionMember<>(INCLUDE_OUTER_CLASS_NAME, Boolean.class::cast));
  }

  private static <T> Optional<T> getMember(
      AnnotationMirror annotationMirror, ExtensionMember<T> extensionMember) {
    final Function<AnnotationValue, Optional<T>> mapAnnotationValue =
        val -> {
          try {
            return Optional.of(extensionMember.map.apply(val.getValue()));
          } catch (ClassCastException e) {
            return Optional.empty();
          }
        };

    final Optional<T> explicitValue =
        PList.fromIter(annotationMirror.getElementValues().entrySet())
            .find(e -> e.getKey().getSimpleName().toString().equals(extensionMember.name))
            .map(Map.Entry::getValue)
            .flatMap(mapAnnotationValue);

    final Supplier<Optional<T>> defaultValue =
        () ->
            PList.fromIter(annotationMirror.getAnnotationType().asElement().getEnclosedElements())
                .filter(e -> e.getKind().equals(ElementKind.METHOD))
                .map(ExecutableElement.class::cast)
                .find(e -> e.getSimpleName().toString().equals(extensionMember.name))
                .map(ExecutableElement::getDefaultValue)
                .flatMap(mapAnnotationValue);

    return Optionals.or(explicitValue, defaultValue);
  }

  private static class ExtensionMember<T> {
    private final String name;
    private final Function<Object, T> map;

    public ExtensionMember(String name, Function<Object, T> map) {
      this.name = name;
      this.map = map;
    }
  }
}
