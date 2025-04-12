package io.github.muehmar.pojobuilder.generator.impl.gen.builder;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings.defaultSettings;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.ClassAccessLevelModifier;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.snapshottesting.SnapshotTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SnapshotTest
class PojoBuilderClassGensTest {
  private Expect expect;

  @ParameterizedTest
  @EnumSource(ClassAccessLevelModifier.class)
  @SnapshotName("samplePojoAndDifferentAccessLevelModifiers")
  void safeBuilderClass_when_samplePojoAndDifferentAccessLevelModifiers_then_correctOutput(
      ClassAccessLevelModifier accessLevelModifier) {
    final Generator<Pojo, PojoSettings> gen = PojoBuilderGenerator.pojoBuilderGenerator();

    final Writer writer =
        gen.generate(
            Pojos.sample(),
            defaultSettings().withBuilderAccessLevel(accessLevelModifier),
            javaWriter());

    expect.scenario(accessLevelModifier.name()).toMatchSnapshot(writer.asString());
  }

  @Test
  @SnapshotName("genericPojo")
  void safeBuilderClass_when_genericPojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> gen = PojoBuilderGenerator.pojoBuilderGenerator();

    final Writer writer = gen.generate(Pojos.genericSample(), defaultSettings(), javaWriter());

    expect.toMatchSnapshot(writer.asString());
  }

  @Test
  @SnapshotName("samplePojoStandardBuilderDisabled")
  void safeBuilderClass_when_samplePojoStandardBuilderDisabled_then_correctOutput() {
    final Generator<Pojo, PojoSettings> gen = PojoBuilderGenerator.pojoBuilderGenerator();

    final Writer writer =
        gen.generate(
            Pojos.sample(), defaultSettings().withStandardBuilderEnabled(false), javaWriter());

    expect.toMatchSnapshot(writer.asString());
  }

  @Test
  @SnapshotName("genericPojoStandardBuilderDisabled")
  void safeBuilderClass_when_genericPojoStandardBuilderDisabled_then_correctOutput() {
    final Generator<Pojo, PojoSettings> gen = PojoBuilderGenerator.pojoBuilderGenerator();

    final Writer writer =
        gen.generate(
            Pojos.genericSample(),
            defaultSettings().withStandardBuilderEnabled(false),
            javaWriter());

    expect.toMatchSnapshot(writer.asString());
  }

  @Test
  @SnapshotName("samplePojoFullBuilderDisabled")
  void safeBuilderClass_when_samplePojoFullBuilderDisabled_then_correctOutput() {
    final Generator<Pojo, PojoSettings> gen = PojoBuilderGenerator.pojoBuilderGenerator();

    final Writer writer =
        gen.generate(Pojos.sample(), defaultSettings().withFullBuilderEnabled(false), javaWriter());

    expect.toMatchSnapshot(writer.asString());
  }

  @Test
  @SnapshotName("genericPojoFullBuilderDisabled")
  void safeBuilderClass_when_genericPojoFullBuilderDisabled_then_correctOutput() {
    final Generator<Pojo, PojoSettings> gen = PojoBuilderGenerator.pojoBuilderGenerator();

    final Writer writer =
        gen.generate(
            Pojos.genericSample(), defaultSettings().withFullBuilderEnabled(false), javaWriter());

    expect.toMatchSnapshot(writer.asString());
  }
}
