package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_STRING;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_LIST;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_MAP;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;
import static io.github.muehmar.pojobuilder.snapshottesting.SnapshotUtil.writerSnapshot;
import static org.assertj.core.api.Assertions.assertThat;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.PojoFields;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.SetMethodGenerator.ModifierOption;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.snapshottesting.SnapshotTest;
import org.junit.jupiter.api.Test;

@SnapshotTest
class SetMethodGeneratorTest {
  private Expect expect;

  @Test
  @SnapshotName("requiredField")
  void setMethodGenerator_when_requiredField_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator =
        SetMethodGenerator.setMethodGenerator((p, s) -> "Builder", ModifierOption.REQUIRED_PRIVATE);

    final Writer writer =
        generator.generate(
            Pojos.fromFields(PojoFields.requiredId()),
            PojoSettings.defaultSettings(),
            javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("requiredFieldAndPublicModifier")
  void setMethodGenerator_when_requiredFieldAndPublicModifier_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator =
        SetMethodGenerator.setMethodGenerator((p, s) -> "Builder", ModifierOption.PUBLIC);

    final Writer writer =
        generator.generate(
            Pojos.fromFields(PojoFields.requiredId()),
            PojoSettings.defaultSettings(),
            javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("optionalField")
  void setMethodGenerator_when_optionalField_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator =
        SetMethodGenerator.setMethodGenerator((p, s) -> "Builder", ModifierOption.REQUIRED_PRIVATE);

    final Writer writer =
        generator.generate(
            Pojos.fromFields(PojoFields.optionalId()),
            PojoSettings.defaultSettings(),
            javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  void setMethodGenerator_when_generatorUsedWithGenericType_then_correctRefs() {
    final Generator<Pojo, PojoSettings> generator =
        SetMethodGenerator.setMethodGenerator((p, s) -> "Builder", ModifierOption.REQUIRED_PRIVATE);

    final Writer writer =
        generator.generate(
            Pojos.fromFields(PojoFields.requiredMap()),
            PojoSettings.defaultSettings(),
            javaWriter());

    assertThat(writer.getRefs().exists(JAVA_UTIL_LIST::equals)).isTrue();
    assertThat(writer.getRefs().exists(JAVA_LANG_STRING::equals)).isTrue();
    assertThat(writer.getRefs().exists(JAVA_UTIL_MAP::equals)).isTrue();
  }

  @Test
  @SnapshotName("fieldOfGenericClass")
  void setMethodGenerator_when_fieldOfGenericClass_then_correctParameterizableBuilder() {
    final Generator<Pojo, PojoSettings> generator =
        SetMethodGenerator.setMethodGenerator((p, s) -> "Builder", ModifierOption.REQUIRED_PRIVATE);

    final Writer writer =
        generator.generate(
            Pojos.genericSample().withFields(PList.single(PojoFields.optionalId())),
            PojoSettings.defaultSettings(),
            javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("builderSetMethodePrefix")
  void setMethodGenerator_when_builderSetMethodePrefix_then_correctPublicMethodGenerated() {
    final Generator<Pojo, PojoSettings> generator =
        SetMethodGenerator.setMethodGenerator((p, s) -> "Builder", ModifierOption.REQUIRED_PRIVATE);

    final PojoSettings settings =
        PojoSettings.defaultSettings().withBuilderSetMethodPrefixOpt(Name.fromString("set"));

    final Writer writer =
        generator.generate(Pojos.fromFields(PojoFields.optionalId()), settings, javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("optionalFieldWithGenericType")
  void setMethodGenerator_when_optionalFieldWithGenericType_then_correctRefs() {
    final Generator<Pojo, PojoSettings> generator =
        SetMethodGenerator.setMethodGenerator((p, s) -> "Builder", ModifierOption.REQUIRED_PRIVATE);

    final Writer writer =
        generator.generate(
            Pojos.genericSample().withFields(PList.single(PojoFields.optionalMap())),
            PojoSettings.defaultSettings(),
            javaWriter());

    assertThat(writer.getRefs().exists(JAVA_UTIL_OPTIONAL::equals)).isTrue();
    assertThat(writer.getRefs().exists(JAVA_UTIL_MAP::equals)).isTrue();
    assertThat(writer.getRefs().exists(JAVA_LANG_STRING::equals)).isTrue();
    assertThat(writer.getRefs().exists(JAVA_UTIL_LIST::equals)).isTrue();
  }

  @Test
  @SnapshotName("genericClass")
  void setMethodGenerator_when_genericClass_then_correctPublicMethodGenerated() {
    final Generator<Pojo, PojoSettings> generator =
        SetMethodGenerator.setMethodGenerator((p, s) -> "Builder", ModifierOption.REQUIRED_PRIVATE);

    final Writer writer =
        generator.generate(
            Pojos.genericSample().withFields(PList.single(PojoFields.optionalId())),
            PojoSettings.defaultSettings(),
            javaWriter());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }
}
