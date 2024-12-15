package io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.snapshottesting.SnapshotUtil.writerSnapshot;
import static org.assertj.core.api.Assertions.assertThat;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.snapshottesting.SnapshotTest;
import org.junit.jupiter.api.Test;

@SnapshotTest
class StandardPojoBuilderGeneratorTest {
  private Expect expect;

  @Test
  @SnapshotName("samplePojo")
  void standardBuilderGenerator_when_generatorUsedWithSamplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator =
        StandardBuilderGenerator.standardBuilderGenerator();
    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("samplePojoAndBuilderSetMethodPrefix")
  void standardBuilderGenerator_when_samplePojoAndBuilderSetMethodPrefix_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator =
        StandardBuilderGenerator.standardBuilderGenerator();
    final Writer writer =
        generator.generate(
            Pojos.sample(),
            PojoSettings.defaultSettings().withBuilderSetMethodPrefixOpt(Name.fromString("set")),
            javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("genericPojo")
  void standardBuilderGenerator_when_genericPojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator =
        StandardBuilderGenerator.standardBuilderGenerator();
    final Writer writer =
        generator.generate(Pojos.genericSample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  void standardBuilderGenerator_when_disabledStandardBuilder_then_noOutput() {
    final Generator<Pojo, PojoSettings> generator =
        StandardBuilderGenerator.standardBuilderGenerator();
    final Writer writer =
        generator.generate(
            Pojos.sample(),
            PojoSettings.defaultSettings().withStandardBuilderEnabled(false),
            javaWriter());

    assertThat(writer.asString()).isEqualTo("");
  }
}
