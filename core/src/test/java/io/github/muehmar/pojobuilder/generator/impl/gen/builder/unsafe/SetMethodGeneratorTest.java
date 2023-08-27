package io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_INTEGER;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_STRING;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_LIST;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_MAP;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;
import static org.junit.jupiter.api.Assertions.assertTrue;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.PojoFields;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.snapshottesting.IntellijDiffSnapshotTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SnapshotExtension.class, IntellijDiffSnapshotTestExtension.class})
class SetMethodGeneratorTest {
  private Expect expect;

  @Test
  @SnapshotName("requiredField")
  void setMethodGenerator_when_requiredField_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = SetMethodGenerator.setMethodGenerator();

    final Writer writer =
        generator.generate(
            Pojos.fromFields(PojoFields.requiredId()),
            PojoSettings.defaultSettings(),
            javaWriter());
    final String output = writer.asString();

    assertTrue(writer.getRefs().exists(JAVA_LANG_INTEGER::equals));
    expect.toMatchSnapshot(output);
  }

  @Test
  @SnapshotName("optionalField")
  void setMethodGenerator_when_optionalField_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = SetMethodGenerator.setMethodGenerator();

    final Writer writer =
        generator.generate(
            Pojos.fromFields(PojoFields.optionalId()),
            PojoSettings.defaultSettings(),
            javaWriter());

    final String output = writer.asString();

    assertTrue(writer.getRefs().exists(JAVA_UTIL_OPTIONAL::equals));
    assertTrue(writer.getRefs().exists(JAVA_LANG_INTEGER::equals));
    expect.toMatchSnapshot(output);
  }

  @Test
  void setMethodGenerator_when_generatorUsedWithGenericType_then_correctRefs() {
    final Generator<Pojo, PojoSettings> generator = SetMethodGenerator.setMethodGenerator();

    final Writer writer =
        generator.generate(
            Pojos.fromFields(PojoFields.requiredMap()),
            PojoSettings.defaultSettings(),
            javaWriter());

    assertTrue(writer.getRefs().exists(JAVA_UTIL_LIST::equals));
    assertTrue(writer.getRefs().exists(JAVA_LANG_STRING::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_MAP::equals));
  }

  @Test
  @SnapshotName("fieldOfGenericClass")
  void setMethodGenerator_when_fieldOfGenericClass_then_correctParameterizableBuilder() {
    final Generator<Pojo, PojoSettings> generator = SetMethodGenerator.setMethodGenerator();

    final Writer writer =
        generator.generate(
            Pojos.genericSample().withFields(PList.single(PojoFields.optionalId())),
            PojoSettings.defaultSettings(),
            javaWriter());
    final String output = writer.asString();

    assertTrue(writer.getRefs().exists(JAVA_LANG_INTEGER::equals));
    expect.toMatchSnapshot(output);
  }

  @Test
  @SnapshotName("builderSetMethodePrefix")
  void setMethodGenerator_when_builderSetMethodePrefix_then_correctPublicMethodGenerated() {
    final Generator<Pojo, PojoSettings> generator = SetMethodGenerator.setMethodGenerator();

    final PojoSettings settings =
        PojoSettings.defaultSettings().withBuilderSetMethodPrefixOpt(Name.fromString("set"));

    final Writer writer =
        generator.generate(Pojos.fromFields(PojoFields.optionalId()), settings, javaWriter());

    final String output = writer.asString();
    expect.toMatchSnapshot(output);

    assertTrue(writer.getRefs().exists(JAVA_UTIL_OPTIONAL::equals));
    assertTrue(writer.getRefs().exists(JAVA_LANG_INTEGER::equals));
  }

  @Test
  @SnapshotName("optionalFieldWithGenericType")
  void setMethodGenerator_when_optionalFieldWithGenericType_then_correctRefs() {
    final Generator<Pojo, PojoSettings> generator = SetMethodGenerator.setMethodGenerator();

    final Writer writer =
        generator.generate(
            Pojos.genericSample().withFields(PList.single(PojoFields.optionalMap())),
            PojoSettings.defaultSettings(),
            javaWriter());

    assertTrue(writer.getRefs().exists(JAVA_UTIL_OPTIONAL::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_MAP::equals));
    assertTrue(writer.getRefs().exists(JAVA_LANG_STRING::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_LIST::equals));
  }

  @Test
  @SnapshotName("genericClass")
  void setMethodGenerator_when_genericClass_then_correctPublicMethodGenerated() {
    final Generator<Pojo, PojoSettings> generator = SetMethodGenerator.setMethodGenerator();

    final Writer writer =
        generator.generate(
            Pojos.genericSample().withFields(PList.single(PojoFields.optionalId())),
            PojoSettings.defaultSettings(),
            javaWriter());

    final String output = writer.asString();
    expect.toMatchSnapshot(output);

    assertTrue(writer.getRefs().exists(JAVA_UTIL_OPTIONAL::equals));
    assertTrue(writer.getRefs().exists(JAVA_LANG_INTEGER::equals));
  }
}
