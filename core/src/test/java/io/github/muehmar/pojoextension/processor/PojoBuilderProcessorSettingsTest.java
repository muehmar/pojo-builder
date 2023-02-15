package io.github.muehmar.pojoextension.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojoextension.annotations.OptionalDetection;
import io.github.muehmar.pojoextension.annotations.SafeBuilder;
import io.github.muehmar.pojoextension.generator.model.ClassAccessLevelModifier;
import io.github.muehmar.pojoextension.generator.model.Name;
import io.github.muehmar.pojoextension.generator.model.settings.PojoSettings;
import org.junit.jupiter.api.Test;

class PojoBuilderProcessorSettingsTest extends BaseExtensionProcessorTest {

  @Test
  void run_when_safeBuilderAnnotation_then_correctSettings() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(SafeBuilder.class)
            .annotation(SafeBuilder.class)
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
            .withImport(SafeBuilder.class)
            .annotationStringParam(SafeBuilder.class, "builderName", "CustomBuilderName")
            .className(className)
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    assertEquals(
        PojoSettings.defaultSettings().withBuilderName(Name.fromString("CustomBuilderName")),
        pojoAndSettings.getSettings());
  }

  @Test
  void run_when_overridePackagePrivateBuilder_then_correctSettings() {
    final Name className = randomClassName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(SafeBuilder.class)
            .annotationBooleanParam(SafeBuilder.class, "packagePrivateBuilder", true)
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
            .withImport(SafeBuilder.class)
            .annotationEnumParam(
                SafeBuilder.class,
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
            .withImport(SafeBuilder.class)
            .annotationStringParam(SafeBuilder.class, "builderName", "SafeBuilder")
            .className(className)
            .create();

    final PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedClassName(className), classString);

    assertEquals(
        PojoSettings.defaultSettings().withBuilderName(Name.fromString("SafeBuilder")),
        pojoAndSettings.getSettings());
  }
}
