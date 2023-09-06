package io.github.muehmar.pojobuilder.generator.impl.gen.builder.full;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.annotations.FullBuilderFieldOrder;
import io.github.muehmar.pojobuilder.generator.PojoFields;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.snapshottesting.SnapshotTest;
import org.junit.jupiter.api.Test;

@SnapshotTest
class FullBuilderGeneratorTest {
  private Expect expect;

  @Test
  @SnapshotName("declarationOrder")
  void fullBuilder_when_declarationOrder_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = FullBuilderGenerator.fullBuilderGenerator();

    final Pojo pojo =
        Pojos.fromFields(
            PojoFields.requiredId(), PojoFields.optionalName(), PojoFields.requiredMap());
    final Writer writer =
        generator.generate(
            pojo,
            PojoSettings.defaultSettings()
                .withFullBuilderFieldOrder(FullBuilderFieldOrder.DECLARATION_ORDER),
            javaWriter());

    expect.toMatchSnapshot(writer.asString());
  }

  @Test
  @SnapshotName("requiredFirstOrder")
  void fullBuilder_when_requiredFirstOrder_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = FullBuilderGenerator.fullBuilderGenerator();

    final Pojo pojo =
        Pojos.fromFields(
            PojoFields.requiredId(), PojoFields.optionalName(), PojoFields.requiredMap());
    final Writer writer =
        generator.generate(
            pojo,
            PojoSettings.defaultSettings()
                .withFullBuilderFieldOrder(FullBuilderFieldOrder.REQUIRED_FIELDS_FIRST),
            javaWriter());

    expect.toMatchSnapshot(writer.asString());
  }

  @Test
  @SnapshotName("genericPojo")
  void fullBuilder_when_genericPojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = FullBuilderGenerator.fullBuilderGenerator();

    final Writer writer =
        generator.generate(Pojos.genericSample(), PojoSettings.defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writer.asString());
  }

  @Test
  void fullBuilderGenerator_when_disabledFullBuilder_then_noOutput() {
    final Generator<Pojo, PojoSettings> generator = FullBuilderGenerator.fullBuilderGenerator();
    final Writer writer =
        generator.generate(
            Pojos.sample(),
            PojoSettings.defaultSettings().withFullBuilderEnabled(false),
            javaWriter());

    assertEquals("", writer.asString());
  }
}
