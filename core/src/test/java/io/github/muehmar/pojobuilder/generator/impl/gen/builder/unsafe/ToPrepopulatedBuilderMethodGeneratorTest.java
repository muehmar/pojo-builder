package io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe.ToPrepopulatedBuilderMethodGenerator.toPrepopulatedBuilderMethodGenerator;
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
class ToPrepopulatedBuilderMethodGeneratorTest {
  private Expect expect;

  @Test
  @SnapshotName("samplePojo")
  void toPrepopulatedBuilderMethodGenerator_when_samplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = toPrepopulatedBuilderMethodGenerator();
    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("genericSamplePojo")
  void generate_when_genericSamplePojo_then_matchSnapshot() {
    final Generator<Pojo, PojoSettings> generator = toPrepopulatedBuilderMethodGenerator();
    final Writer writer =
        generator.generate(Pojos.genericSample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }
}
