package io.github.muehmar.pojobuilder.processor.mapper;

import static io.github.muehmar.pojobuilder.generator.model.Necessity.OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.REQUIRED;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.Nullable;
import io.github.muehmar.pojobuilder.annotations.OptionalDetection;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.type.DeclaredType;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import io.github.muehmar.pojobuilder.processor.model.DetectionSettings;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

public class PojoFieldMapper {
  private PojoFieldMapper() {}

  public static PojoField mapToPojoField(Element element, DetectionSettings settings) {
    final Name fieldName = Name.fromString(element.getSimpleName().toString());
    final Type fieldType = TypeMirrorMapper.map(element.asType());

    return mapToPojoField(element, fieldName, fieldType, settings);
  }

  private static PojoField mapToPojoField(
      Element element, Name name, Type type, DetectionSettings settings) {
    return Mapper.initial()
        .or(PojoFieldMapper::mapOptionalPojoField)
        .or(PojoFieldMapper::mapNullablePojoField)
        .mapWithDefault(element, name, type, settings, () -> new PojoField(name, type, REQUIRED));
  }

  private static Optional<PojoField> mapOptionalPojoField(
      Element element, Name name, Type type, DetectionSettings settings) {
    return Optional.of(type)
        .filter(
            ignore ->
                settings.getOptionalDetections().exists(OptionalDetection.OPTIONAL_CLASS::equals))
        .flatMap(PojoFieldMapper::getOptionalValueType)
        .map(typeParameter -> new PojoField(name, typeParameter, OPTIONAL));
  }

  private static Optional<Type> getOptionalValueType(Type type) {
    final Function<DeclaredType, Optional<Type>> getOptionalType =
        classType ->
            Optional.of(classType)
                .filter(DeclaredType::isOptional)
                .flatMap(t -> t.getTypeParameters().headOption());
    return type.onDeclaredType(getOptionalType).flatMap(Function.identity());
  }

  private static Optional<PojoField> mapNullablePojoField(
      Element element, Name name, Type type, DetectionSettings settings) {
    final PList<String> nullableAnnotationClasses =
        PList.of(
            Nullable.class.getName(), "javax.annotation.Nullable", "jakarta.annotation.Nullable");

    return PList.fromIter(element.getAnnotationMirrors())
        .map(AnnotationMirror::getAnnotationType)
        .map(javax.lang.model.type.DeclaredType::asElement)
        .map(Element::asType)
        .map(TypeMirror::toString)
        .find(annotationClassName -> nullableAnnotationClasses.exists(annotationClassName::equals))
        .filter(
            ignore ->
                settings
                    .getOptionalDetections()
                    .exists(OptionalDetection.NULLABLE_ANNOTATION::equals))
        .map(ignore -> new PojoField(name, type, OPTIONAL));
  }

  @FunctionalInterface
  private interface Mapper {
    Optional<PojoField> map(
        Element element, Name name, Type type, DetectionSettings detectionSettings);

    default Mapper or(Mapper next) {
      final Mapper self = this;
      return (element, name, type, settings) -> {
        final Optional<PojoField> result = self.map(element, name, type, settings);
        return result.isPresent() ? result : next.map(element, name, type, settings);
      };
    }

    default PojoField mapWithDefault(
        Element element, Name name, Type type, DetectionSettings settings, Supplier<PojoField> s) {
      return this.map(element, name, type, settings).orElseGet(s);
    }

    static Mapper initial() {
      return ((element, name, type, settings) -> Optional.empty());
    }
  }
}
