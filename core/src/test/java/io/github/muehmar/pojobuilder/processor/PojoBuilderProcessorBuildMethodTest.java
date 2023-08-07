package io.github.muehmar.pojobuilder.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.BuildMethod;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PojoName;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class PojoBuilderProcessorBuildMethodTest extends BaseExtensionProcessorTest {
  @Test
  void run_when_simplePojoWithoutBuildMethod_then_noBuildMethodInPojo() {
    final PojoName pojoName = randomPojoName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .annotation(PojoBuilder.class)
            .className(pojoName.getName())
            .withField("String", "id")
            .constructor()
            .create();

    final BaseExtensionProcessorTest.PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedPojoName(pojoName), classString);

    final Optional<BuildMethod> buildMethod = pojoAndSettings.getPojo().getBuildMethod();
    assertEquals(Optional.empty(), buildMethod);
  }

  @Test
  void run_when_simplePojoWithCorrectBuildMethod_then_correctBuildMethodInPojo() {
    final PojoName pojoName = randomPojoName();

    final String classString =
        TestPojoComposer.ofPackage(PACKAGE)
            .withImport(PojoBuilder.class)
            .withImport(io.github.muehmar.pojobuilder.annotations.BuildMethod.class)
            .annotation(PojoBuilder.class)
            .className(pojoName.getName())
            .withField("String", "id")
            .constructor()
            .getter("String", "id")
            .methodWithAnnotation(
                "static String",
                "customBuildMethod",
                "return inst.toString()",
                "@BuildMethod",
                pojoName + " inst")
            .create();

    final BaseExtensionProcessorTest.PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(qualifiedPojoName(pojoName), classString);

    final Optional<BuildMethod> buildMethod = pojoAndSettings.getPojo().getBuildMethod();
    final BuildMethod expected =
        new BuildMethod(Name.fromString("customBuildMethod"), Types.string());
    assertEquals(Optional.of(expected), buildMethod);
  }
}
