package io.github.muehmar.pojobuilder.processor;

import static io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassnames.ioException;
import static io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassnames.malformedUrlException;
import static org.assertj.core.api.Assertions.assertThat;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.BuildMethod;
import io.github.muehmar.pojobuilder.generator.model.Name;
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
    assertThat(buildMethod).isEmpty();
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
    assertThat(buildMethod).isEqualTo(Optional.of(expected));
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
                "java.io.IOException, java.net.MalformedURLException")
            .create();

    final BaseExtensionProcessorTest.PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(pojoClassname, classString);

    final Optional<BuildMethod> buildMethod = pojoAndSettings.getPojo().getBuildMethod();
    final BuildMethod expected =
        new BuildMethod(
            Name.fromString("customBuildMethod"),
            Types.string(),
            PList.of(ioException(), malformedUrlException()));
    assertThat(buildMethod).isEqualTo(Optional.of(expected));
  }
}
