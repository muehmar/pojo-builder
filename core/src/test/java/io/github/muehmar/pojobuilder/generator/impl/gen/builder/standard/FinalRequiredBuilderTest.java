package io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.FinalRequiredBuilder.finalRequiredBuilder;
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
class FinalRequiredBuilderTest {
  private Expect expect;

  @Test
  @SnapshotName("samplePojo")
  void finalRequiredBuilder_when_samplePojo_then_correctClassOutput() {
    final Generator<Pojo, PojoSettings> generator = finalRequiredBuilder();
    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("genericPojo")
  void finalRequiredBuilder_when_genericPojo_then_correctClassOutput() {
    final Generator<Pojo, PojoSettings> generator = finalRequiredBuilder();
    final Writer writer =
        generator.generate(Pojos.genericSample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }
}
