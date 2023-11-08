package io.github.muehmar.pojobuilder.generator.impl.gen;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.impl.gen.ThrowsGenerator.throwsGenerator;
import static io.github.muehmar.pojobuilder.snapshottesting.SnapshotUtil.writerSnapshot;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Classname;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import io.github.muehmar.pojobuilder.snapshottesting.SnapshotTest;
import org.junit.jupiter.api.Test;

@SnapshotTest
class ThrowsGeneratorTest {
  private Expect expect;

  @Test
  @SnapshotName("twoExceptions")
  void generate_when_twoExceptions_then_matchSnapshot() {
    final Generator<PList<QualifiedClassname>, PojoSettings> generator = throwsGenerator();

    final PList<QualifiedClassname> exceptions =
        PList.of(
            new QualifiedClassname(
                Classname.fromString("SomeException"),
                PackageName.fromString("io.github.muehmar.exception")),
            new QualifiedClassname(
                Classname.fromString("IllegalArgumentException"), PackageName.javaLang()));

    final Writer writer =
        generator.generate(exceptions, PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }
}
