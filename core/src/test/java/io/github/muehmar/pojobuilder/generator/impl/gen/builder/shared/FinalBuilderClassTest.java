package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.FinalBuilderClass.finalBuilderClass;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.StandardBuilderGenerator.CLASS_NAME_FOR_OPTIONAL;
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
class FinalBuilderClassTest {
  private Expect expect;

  @Test
  @SnapshotName("samplePojo")
  void finalBuilderClass_when_samplePojo_then_correctClassOutput() {
    final Generator<Pojo, PojoSettings> generator =
        finalBuilderClass(CLASS_NAME_FOR_OPTIONAL, ignore -> 1);
    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("genericPojo")
  void finalOptionalBuilder_when_genericPojo_then_correctClassOutput() {
    final Generator<Pojo, PojoSettings> generator =
        finalBuilderClass(CLASS_NAME_FOR_OPTIONAL, ignore -> 1);
    final Writer writer =
        generator.generate(Pojos.genericSample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("factoryMethodSample")
  void finalOptionalBuilder_when_factoryMethodSample_then_correctClassOutput() {
    final Generator<Pojo, PojoSettings> generator =
        finalBuilderClass(CLASS_NAME_FOR_OPTIONAL, ignore -> 1);
    final Writer writer =
        generator.generate(
            Pojos.factoryMethodSample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }
}
