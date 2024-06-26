package io.github.muehmar.pojobuilder.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.BuildMethod;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import io.github.muehmar.pojobuilder.generator.model.type.Classname;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class PojoBuilderProcessorBuildMethodTest extends BaseExtensionProcessorTest {
  @Test
  void run_when_simplePojoWithoutBuildMethod_then_noBuildMethodInPojo() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotation(PojoBuilder.class)
            .className(pojoClassname.getName())
            .withField("String", "id")
            .constructor()
            .create();

    final BaseExtensionProcessorTest.PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(pojoClassname, classString);

    final Optional<BuildMethod> buildMethod = pojoAndSettings.getPojo().getBuildMethod();
    assertEquals(Optional.empty(), buildMethod);
  }

  @Test
  void run_when_simplePojoWithCorrectBuildMethod_then_correctBuildMethodInPojo() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .withImport(io.github.muehmar.pojobuilder.annotations.BuildMethod.class)
            .annotation(PojoBuilder.class)
            .className(pojoClassname.getName())
            .withField("String", "id")
            .constructor()
            .getter("String", "id")
            .methodWithAnnotation(
                "static String",
                "customBuildMethod",
                "return inst.toString()",
                "@BuildMethod",
                pojoClassname.getSimpleName() + " inst")
            .create();

    final BaseExtensionProcessorTest.PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(pojoClassname, classString);

    final Optional<BuildMethod> buildMethod = pojoAndSettings.getPojo().getBuildMethod();
    final BuildMethod expected =
        new BuildMethod(Name.fromString("customBuildMethod"), Types.string(), PList.empty());
    assertEquals(Optional.of(expected), buildMethod);
  }

  @Test
  void run_when_simplePojoWithThrowingBuildMethod_then_correctBuildMethodInPojo() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .withImport(io.github.muehmar.pojobuilder.annotations.BuildMethod.class)
            .annotation(PojoBuilder.class)
            .className(pojoClassname.getName())
            .withField("String", "id")
            .constructor()
            .getter("String", "id")
            .methodWithAnnotation(
                "static String",
                "customBuildMethod",
                "return inst.toString()",
                "@BuildMethod",
                pojoClassname.getSimpleName() + " inst",
                "java.io.IOException, java.net.URISyntaxException")
            .create();

    final BaseExtensionProcessorTest.PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(pojoClassname, classString);

    final Optional<BuildMethod> buildMethod = pojoAndSettings.getPojo().getBuildMethod();
    final BuildMethod expected =
        new BuildMethod(
            Name.fromString("customBuildMethod"),
            Types.string(),
            PList.of(
                new QualifiedClassname(
                    Classname.fromString("IOException"), PackageName.fromString("java.io")),
                new QualifiedClassname(
                    Classname.fromString("URISyntaxException"),
                    PackageName.fromString("java.net"))));
    assertEquals(Optional.of(expected), buildMethod);
  }
}
