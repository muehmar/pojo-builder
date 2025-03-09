package io.github.muehmar.pojobuilder.generator.impl.gen.builder.prepopulated;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings.defaultSettings;
import static io.github.muehmar.pojobuilder.snapshottesting.SnapshotUtil.writerSnapshot;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.snapshottesting.SnapshotTest;
import org.junit.jupiter.api.Test;

@SnapshotTest
class PrePopulatedBuilderGeneratorTest {
  private Expect expect;

  @Test
  @SnapshotName("genericPojo")
  void prePopulatedBuilderGenerator_when_genericPojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> gen =
        PrePopulatedBuilderGenerator.prePopulatedBuilderGenerator();

    final Writer writer = gen.generate(Pojos.genericSample(), defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("samplePojo")
  void prePopulatedBuilderGenerator_when_samplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> gen =
        PrePopulatedBuilderGenerator.prePopulatedBuilderGenerator();

    final Writer writer = gen.generate(Pojos.sample(), defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }
}
