package io.github.muehmar.pojobuilder.generator.impl.gen.builder.builderfactory;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings.defaultSettings;
import static io.github.muehmar.pojobuilder.snapshottesting.SnapshotUtil.writerSnapshot;
import static org.junit.jupiter.api.Assertions.*;

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
class BuilderFactoryMethodsTest {
  private Expect expect;

  @Test
  @SnapshotName("samplePojo")
  void generate_when_samplePojo_then_matchSnapshot() {
    final Generator<Pojo, PojoSettings> generator = BuilderFactoryMethods.builderFactoryMethods();

    final Writer writer = generator.generate(Pojos.sample(), defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }
}
