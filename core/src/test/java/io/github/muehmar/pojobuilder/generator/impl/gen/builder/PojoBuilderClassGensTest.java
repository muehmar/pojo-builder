package io.github.muehmar.pojobuilder.generator.impl.gen.builder;

import static io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings.defaultSettings;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.ClassAccessLevelModifier;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@ExtendWith(SnapshotExtension.class)
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
            Writer.createDefault());

    expect.scenario(accessLevelModifier.name()).toMatchSnapshot(writer.asString());
  }

  @Test
  @SnapshotName("genericPojo")
  void safeBuilderClass_when_genericPojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> gen = PojoBuilderGenerator.pojoBuilderGenerator();

    final Writer writer =
        gen.generate(Pojos.genericSample(), defaultSettings(), Writer.createDefault());

    expect.toMatchSnapshot(writer.asString());
  }

  @Test
  @SnapshotName("samplePojoStandardBuilderDisabled")
  void safeBuilderClass_when_samplePojoStandardBuilderDisabled_then_correctOutput() {
    final Generator<Pojo, PojoSettings> gen = PojoBuilderGenerator.pojoBuilderGenerator();

    final Writer writer =
        gen.generate(
            Pojos.sample(),
            defaultSettings().withStandardBuilderEnabled(false),
            Writer.createDefault());

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
            Writer.createDefault());

    expect.toMatchSnapshot(writer.asString());
  }

  @Test
  @SnapshotName("samplePojoFullBuilderDisabled")
  void safeBuilderClass_when_samplePojoFullBuilderDisabled_then_correctOutput() {
    final Generator<Pojo, PojoSettings> gen = PojoBuilderGenerator.pojoBuilderGenerator();

    final Writer writer =
        gen.generate(
            Pojos.sample(),
            defaultSettings().withFullBuilderEnabled(false),
            Writer.createDefault());

    expect.toMatchSnapshot(writer.asString());
  }

  @Test
  @SnapshotName("genericPojoFullBuilderDisabled")
  void safeBuilderClass_when_genericPojoFullBuilderDisabled_then_correctOutput() {
    final Generator<Pojo, PojoSettings> gen = PojoBuilderGenerator.pojoBuilderGenerator();

    final Writer writer =
        gen.generate(
            Pojos.genericSample(),
            defaultSettings().withFullBuilderEnabled(false),
            Writer.createDefault());

    expect.toMatchSnapshot(writer.asString());
  }
}
