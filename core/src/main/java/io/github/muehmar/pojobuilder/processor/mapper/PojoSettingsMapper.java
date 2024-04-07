package io.github.muehmar.pojobuilder.processor.mapper;

import static io.github.muehmar.pojobuilder.processor.mapper.AnnotationMemberMapper.getBuilderName;
import static io.github.muehmar.pojobuilder.processor.mapper.AnnotationMemberMapper.getBuilderSetMethodPrefix;
import static io.github.muehmar.pojobuilder.processor.mapper.AnnotationMemberMapper.getEnableFullBuilder;
import static io.github.muehmar.pojobuilder.processor.mapper.AnnotationMemberMapper.getEnableStandardBuilder;
import static io.github.muehmar.pojobuilder.processor.mapper.AnnotationMemberMapper.getFullBuilderFieldOrder;
import static io.github.muehmar.pojobuilder.processor.mapper.AnnotationMemberMapper.getIncludeOuterClassName;
import static io.github.muehmar.pojobuilder.processor.mapper.AnnotationMemberMapper.getOptionalDetection;
import static io.github.muehmar.pojobuilder.processor.mapper.AnnotationMemberMapper.getPackagePrivateBuilder;

import ch.bluecare.commons.data.NonEmptyList;
import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.Strings;
import io.github.muehmar.pojobuilder.generator.model.ClassAccessLevelModifier;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import javax.lang.model.element.AnnotationMirror;

public class PojoSettingsMapper {
  private PojoSettingsMapper() {}

  public static PojoSettings extractSettingsFromAnnotationPath(
      NonEmptyList<AnnotationMirror> annotations) {
    return extractSettingsFromAnnotationPath(annotations.toPList(), PojoSettings.defaultSettings());
  }

  private static PojoSettings extractSettingsFromAnnotationPath(
      PList<AnnotationMirror> annotations, PojoSettings currentSettings) {
    return annotations
        .headOption()
        .map(
            a ->
                extractSettingsFromAnnotationPath(
                    annotations.tail(), overrideWithAnnotationValues(a, currentSettings)))
        .orElse(currentSettings);
  }

  private static PojoSettings overrideWithAnnotationValues(
      AnnotationMirror annotation, PojoSettings currentSettings) {
    return currentSettings
        .overrideOptionalDetection(getOptionalDetection(annotation))
        .overrideBuilderName(
            getBuilderName(annotation)
                .map(String::trim)
                .filter(Strings::nonEmpty)
                .map(Name::fromString))
        .overrideBuilderSetMethodPrefix(
            getBuilderSetMethodPrefix(annotation)
                .map(String::trim)
                .filter(Strings::nonEmpty)
                .map(Name::fromString))
        .overrideBuilderAccessLevel(
            getPackagePrivateBuilder(annotation)
                .map(PojoSettingsMapper::classAccessLevelModifierFromIsPackagePrivateFlag))
        .overrideEnableStandardBuilder(getEnableStandardBuilder(annotation))
        .overrideEnableFullBuilder(getEnableFullBuilder(annotation))
        .overrideFullBuilderFieldOrder(getFullBuilderFieldOrder(annotation))
        .overrideIncludeOuterClassName(getIncludeOuterClassName(annotation));
  }

  private static ClassAccessLevelModifier classAccessLevelModifierFromIsPackagePrivateFlag(
      boolean isPackagePrivate) {
    return isPackagePrivate
        ? ClassAccessLevelModifier.PACKAGE_PRIVATE
        : ClassAccessLevelModifier.PUBLIC;
  }
}
