package io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.snapshottesting.SnapshotUtil.writerSnapshot;

import au.com.origin.snapshots.Expect;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.snapshottesting.SnapshotTest;
import org.junit.jupiter.api.Test;

@SnapshotTest
class UnsafeBuilderGeneratorTest {
  private Expect expect;

  @Test
  void builderClass_when_generatorUsedWithSamplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = UnsafeBuilderGenerator.unsafeBuilderGenerator();
    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  void builderClass_when_generatorUsedWithGenericSamplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = UnsafeBuilderGenerator.unsafeBuilderGenerator();
    final Writer writer =
        generator.generate(Pojos.genericSample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }
}
