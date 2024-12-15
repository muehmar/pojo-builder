package io.github.muehmar.pojobuilder.processor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.ConstructorMatching;
import io.github.muehmar.pojobuilder.annotations.FullBuilderFieldOrder;
import io.github.muehmar.pojobuilder.annotations.OptionalDetection;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.ClassAccessLevelModifier;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.settings.FieldMatching;
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

    assertThat(PojoSettings.defaultSettings()).isEqualTo(pojoAndSettings.getSettings());
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

    assertThat(
            PojoSettings.defaultSettings().withBuilderNameOpt(Name.fromString("CustomBuilderName")))
        .isEqualTo(pojoAndSettings.getSettings());
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

    assertThat(
            PojoSettings.defaultSettings()
                .withBuilderAccessLevel(ClassAccessLevelModifier.PACKAGE_PRIVATE))
        .isEqualTo(pojoAndSettings.getSettings());
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

    assertThat(
            PojoSettings.defaultSettings()
                .withOptionalDetections(PList.single(OptionalDetection.NONE)))
        .isEqualTo(pojoAndSettings.getSettings());
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

    assertThat(PojoSettings.defaultSettings().withBuilderNameOpt(Name.fromString("PojoBuilder")))
        .isEqualTo(pojoAndSettings.getSettings());
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

    assertThat(PojoSettings.defaultSettings().withStandardBuilderEnabled(false))
        .isEqualTo(pojoAndSettings.getSettings());
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

    assertThat(PojoSettings.defaultSettings().withFullBuilderEnabled(false))
        .isEqualTo(pojoAndSettings.getSettings());
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

    assertThat(
            PojoSettings.defaultSettings()
                .withFullBuilderFieldOrder(FullBuilderFieldOrder.DECLARATION_ORDER))
        .isEqualTo(pojoAndSettings.getSettings());
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

    assertThat(PojoSettings.defaultSettings().withIncludeOuterClassName(false))
        .isEqualTo(pojoAndSettings.getSettings());
  }

  @Test
  void run_when_pojoBuilderAnnotationWithConstructorMatchingNameAndType_then_correctSettings() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotationEnumParam(
                PojoBuilder.class,
                "constructorMatching",
                ConstructorMatching.class,
                ConstructorMatching.TYPE_AND_NAME)
            .className(pojoClassname.getName())
            .create();

    final PojoAndSettings pojoAndSettings = runAnnotationProcessor(pojoClassname, classString);

    assertThat(PojoSettings.defaultSettings().withFieldMatching(FieldMatching.TYPE_AND_NAME))
        .isEqualTo(pojoAndSettings.getSettings());
  }
}
