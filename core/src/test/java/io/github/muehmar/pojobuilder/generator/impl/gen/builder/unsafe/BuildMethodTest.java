package io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe.BuildMethod.buildMethodContent;
import static io.github.muehmar.pojobuilder.snapshottesting.SnapshotUtil.writerSnapshot;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.BuildMethod;
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
  @SnapshotName("sample")
  void buildMethodContent_when_calledWithSample_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethodContent();
    final String output =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), javaWriter()).asString();

    expect.toMatchSnapshot(output);
  }

  @Test
  @SnapshotName("genericSample")
  void buildMethodContent_when_calledWithGenericSample_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethodContent();
    final String output =
        generator
            .generate(Pojos.genericSample(), PojoSettings.defaultSettings(), javaWriter())
            .asString();

    expect.toMatchSnapshot(output);
  }

  @Test
  @SnapshotName("genericSampleAndBuildMethod")
  void buildMethodContent_when_calledWithGenericSampleAndBuildMethod_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethodContent();
    final io.github.muehmar.pojobuilder.generator.model.BuildMethod buildMethod =
        new BuildMethod(Name.fromString("customBuildMethod"), Types.string(), PList.empty());
    final Pojo pojo = Pojos.genericSample().withBuildMethod(Optional.of(buildMethod));
    final String output =
        generator.generate(pojo, PojoSettings.defaultSettings(), javaWriter()).asString();

    expect.toMatchSnapshot(output);
  }

  @Test
  @SnapshotName("sampleAndConstructorWithOptionalArgument")
  void
      buildMethodContent_when_generatorUsedWithSamplePojoAndConstructorWithOptionalArgument_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethodContent();
    final Writer writer =
        generator.generate(
            Pojos.sampleWithConstructorWithOptionalArgument(),
            PojoSettings.defaultSettings(),
            javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("sampleWithFactoryMethod")
  void buildMethodContent_when_sampleWithFactoryMethod_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethodContent();
    final Writer writer =
        generator.generate(
            Pojos.factoryMethodSample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }
}
