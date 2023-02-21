package io.github.muehmar.pojobuilder.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.OptionalDetection;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.ClassAccessLevelModifier;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import org.junit.jupiter.api.Test;

class PojoBuilderProcessorSettingsTest extends BaseExtensionProcessorTest {

  @Test
  void run_when_safeBuilderAnnotation_then_correctSettings() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotation(PojoBuilder.class)
            .className(className)
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    assertEquals(PojoSettings.defaultSettings(), pojoAndSettings.getSettings());
  }

  @Test
  void run_when_overrideBuilderName_then_correctSettings() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationStringParam(PojoBuilder.class, "builderName", "CustomBuilderName")
            .className(className)
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    assertEquals(
        PojoSettings.defaultSettings().withBuilderNameOpt(Name.fromString("CustomBuilderName")),
        pojoAndSettings.getSettings());
  }

  @Test
  void run_when_overridePackagePrivateBuilder_then_correctSettings() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationBooleanParam(PojoBuilder.class, "packagePrivateBuilder", true)
            .className(className)
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    assertEquals(
        PojoSettings.defaultSettings()
            .withBuilderAccessLevel(ClassAccessLevelModifier.PACKAGE_PRIVATE),
        pojoAndSettings.getSettings());
  }

  @Test
  void run_when_overrideOptionalDetection_then_correctSettings() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationEnumParam(
                PojoBuilder.class,
                "optionalDetection",
                OptionalDetection.class,
                OptionalDetection.NONE)
            .className(className)
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    assertEquals(
        PojoSettings.defaultSettings().withOptionalDetections(PList.single(OptionalDetection.NONE)),
        pojoAndSettings.getSettings());
  }

  @Test
  void run_when_safeBuilderAnnotationWithCustomBuilderName_then_correctSettings() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationStringParam(PojoBuilder.class, "builderName", "SafeBuilder")
            .className(className)
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    assertEquals(
        PojoSettings.defaultSettings().withBuilderNameOpt(Name.fromString("SafeBuilder")),
        pojoAndSettings.getSettings());
  }
}
