package io.github.muehmar.pojobuilder.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.FullBuilderFieldOrder;
import io.github.muehmar.pojobuilder.annotations.OptionalDetection;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.ClassAccessLevelModifier;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import org.junit.jupiter.api.Test;

class PojoBuilderProcessorSettingsTest extends BaseExtensionProcessorTest {

  @Test
  void run_when_pojoBuilderAnnotation_then_correctDefaultSettings() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotation(PojoBuilder.class)
            .className(pojoClassname.getName())
            .create();

    final PojoAndSettings pojoAndSettings = runAnnotationProcessor(pojoClassname, classString);

    assertEquals(PojoSettings.defaultSettings(), pojoAndSettings.getSettings());
  }

  @Test
  void run_when_overrideBuilderName_then_correctSettings() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationStringParam(PojoBuilder.class, "builderName", "CustomBuilderName")
            .className(pojoClassname.getName())
            .create();

    final PojoAndSettings pojoAndSettings = runAnnotationProcessor(pojoClassname, classString);

    assertEquals(
        PojoSettings.defaultSettings().withBuilderNameOpt(Name.fromString("CustomBuilderName")),
        pojoAndSettings.getSettings());
  }

  @Test
  void run_when_overridePackagePrivateBuilder_then_correctSettings() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationBooleanParam(PojoBuilder.class, "packagePrivateBuilder", true)
            .className(pojoClassname.getName())
            .create();

    final PojoAndSettings pojoAndSettings = runAnnotationProcessor(pojoClassname, classString);

    assertEquals(
        PojoSettings.defaultSettings()
            .withBuilderAccessLevel(ClassAccessLevelModifier.PACKAGE_PRIVATE),
        pojoAndSettings.getSettings());
  }

  @Test
  void run_when_overrideOptionalDetection_then_correctSettings() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationEnumParam(
                PojoBuilder.class,
                "optionalDetection",
                OptionalDetection.class,
                OptionalDetection.NONE)
            .className(pojoClassname.getName())
            .create();

    final PojoAndSettings pojoAndSettings = runAnnotationProcessor(pojoClassname, classString);

    assertEquals(
        PojoSettings.defaultSettings().withOptionalDetections(PList.single(OptionalDetection.NONE)),
        pojoAndSettings.getSettings());
  }

  @Test
  void run_when_pojoBuilderAnnotationWithCustomBuilderName_then_correctSettings() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationStringParam(PojoBuilder.class, "builderName", "PojoBuilder")
            .className(pojoClassname.getName())
            .create();

    final PojoAndSettings pojoAndSettings = runAnnotationProcessor(pojoClassname, classString);

    assertEquals(
        PojoSettings.defaultSettings().withBuilderNameOpt(Name.fromString("PojoBuilder")),
        pojoAndSettings.getSettings());
  }

  @Test
  void run_when_pojoBuilderAnnotationWithDisabledStandardBuilder_then_correctSettings() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationBooleanParam(PojoBuilder.class, "enableStandardBuilder", false)
            .className(pojoClassname.getName())
            .create();

    final PojoAndSettings pojoAndSettings = runAnnotationProcessor(pojoClassname, classString);

    assertEquals(
        PojoSettings.defaultSettings().withStandardBuilderEnabled(false),
        pojoAndSettings.getSettings());
  }

  @Test
  void run_when_pojoBuilderAnnotationWithDisabledFullBuilder_then_correctSettings() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationBooleanParam(PojoBuilder.class, "enableFullBuilder", false)
            .className(pojoClassname.getName())
            .create();

    final PojoAndSettings pojoAndSettings = runAnnotationProcessor(pojoClassname, classString);

    assertEquals(
        PojoSettings.defaultSettings().withFullBuilderEnabled(false),
        pojoAndSettings.getSettings());
  }

  @Test
  void run_when_pojoBuilderAnnotationWithFullBuilderFieldOrderDeclaration_then_correctSettings() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationEnumParam(
                PojoBuilder.class,
                "fullBuilderFieldOrder",
                FullBuilderFieldOrder.class,
                FullBuilderFieldOrder.DECLARATION_ORDER)
            .className(pojoClassname.getName())
            .create();

    final PojoAndSettings pojoAndSettings = runAnnotationProcessor(pojoClassname, classString);

    assertEquals(
        PojoSettings.defaultSettings()
            .withFullBuilderFieldOrder(FullBuilderFieldOrder.DECLARATION_ORDER),
        pojoAndSettings.getSettings());
  }

  @Test
  void run_when_pojoBuilderAnnotationWithNotIncludeOuterClassName_then_correctSettings() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationBooleanParam(PojoBuilder.class, "includeOuterClassName", false)
            .className(pojoClassname.getName())
            .create();

    final PojoAndSettings pojoAndSettings = runAnnotationProcessor(pojoClassname, classString);

    assertEquals(
        PojoSettings.defaultSettings().withIncludeOuterClassName(false),
        pojoAndSettings.getSettings());
  }
}
