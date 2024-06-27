package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.BuildMethod.buildMethod;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.BuildMethod.standardBuilderBuildMethod;
import static io.github.muehmar.pojobuilder.snapshottesting.SnapshotUtil.writerSnapshot;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import io.github.muehmar.pojobuilder.snapshottesting.SnapshotTest;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@SnapshotTest
class BuildMethodTest {
  private Expect expect;

  @Test
  @SnapshotName("calledWithGenericSample")
  void standardBuilderBuildMethod_when_calledWithGenericSample_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = standardBuilderBuildMethod();
    final Writer writer =
        generator.generate(Pojos.genericSample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("calledWithGenericSampleWithCustomBuildMethod")
  void
      standardBuilderBuildMethod_when_calledWithGenericSampleWithCustomBuildMethod_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = standardBuilderBuildMethod();
    final io.github.muehmar.pojobuilder.generator.model.BuildMethod buildMethod =
        new io.github.muehmar.pojobuilder.generator.model.BuildMethod(
            Name.fromString("customBuildMethod"), Types.string(), PList.empty());
    final Writer writer =
        generator.generate(
            Pojos.genericSample().withBuildMethod(Optional.of(buildMethod)),
            PojoSettings.defaultSettings(),
            javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("factoryMethodSample")
  void standardBuilderBuildMethod_when_factoryMethodSample_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = standardBuilderBuildMethod();
    final Writer writer =
        generator.generate(
            Pojos.factoryMethodSample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }
}
