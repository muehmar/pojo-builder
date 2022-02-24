package io.github.muehmar.pojoextension.generator.impl.gen.equalshashcode;

import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_UTIL_ARRAYS;
import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_UTIL_OBJECTS;
import static io.github.muehmar.pojoextension.generator.model.Necessity.REQUIRED;
import static io.github.muehmar.pojoextension.generator.model.settings.Ability.DISABLED;
import static org.junit.jupiter.api.Assertions.*;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojoextension.generator.Generator;
import io.github.muehmar.pojoextension.generator.PojoFields;
import io.github.muehmar.pojoextension.generator.Pojos;
import io.github.muehmar.pojoextension.generator.model.Name;
import io.github.muehmar.pojoextension.generator.model.Pojo;
import io.github.muehmar.pojoextension.generator.model.PojoField;
import io.github.muehmar.pojoextension.generator.model.settings.PojoSettings;
import io.github.muehmar.pojoextension.generator.model.type.Types;
import io.github.muehmar.pojoextension.generator.writer.Writer;
import org.junit.jupiter.api.Test;

class HashCodeGensTest {

  @Test
  void genHashCodeMethod_when_generatorUsedWithSamplePojoAndArrayField_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = HashCodeGens.genHashCodeMethod();

    final PList<PojoField> fields =
        Pojos.sample()
            .getFields()
            .cons(
                new PojoField(
                    Name.fromString("byteArray"), Types.array(Types.primitiveByte()), REQUIRED));

    final Writer writer =
        generator.generate(
            Pojos.sample().withFields(fields).withGetters(fields.map(PojoFields::toGetter)),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    assertEquals(
        "default int genHashCode() {\n"
            + "  int result = Objects.hash(getId(), getUsername(), getNickname());\n"
            + "  result = 31 * result + Arrays.hashCode(getByteArray());\n"
            + "  return result;\n"
            + "}",
        writer.asString());
    assertTrue(writer.getRefs().exists(JAVA_UTIL_OBJECTS::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_ARRAYS::equals));
  }

  @Test
  void genHashCodeMethod_when_generatorUsedWithBooleanField_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = HashCodeGens.genHashCodeMethod();

    final PList<PojoField> fields =
        PList.of(new PojoField(Name.fromString("flag"), Types.primitiveBoolean(), REQUIRED));

    final Writer writer =
        generator.generate(
            Pojos.sample().withFields(fields).withGetters(fields.map(PojoFields::toGetter)),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    assertEquals(
        "default int genHashCode() {\n"
            + "  int result = Objects.hash(isFlag());\n"
            + "  return result;\n"
            + "}",
        writer.asString());
    assertTrue(writer.getRefs().exists(JAVA_UTIL_OBJECTS::equals));
    assertFalse(writer.getRefs().exists(JAVA_UTIL_ARRAYS::equals));
  }

  @Test
  void genHashCodeMethod_when_generatorUsedWithTwoByteArrays_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = HashCodeGens.genHashCodeMethod();

    final PList<PojoField> fields =
        PList.of(
            new PojoField(
                Name.fromString("byteArray"), Types.array(Types.primitiveByte()), REQUIRED),
            new PojoField(
                Name.fromString("byteArray2"), Types.array(Types.primitiveByte()), REQUIRED));

    final Writer writer =
        generator.generate(
            Pojos.sample().withFields(fields).withGetters(fields.map(PojoFields::toGetter)),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    assertEquals(
        "default int genHashCode() {\n"
            + "  int result = Arrays.hashCode(getByteArray());\n"
            + "  result = 31 * result + Arrays.hashCode(getByteArray2());\n"
            + "  return result;\n"
            + "}",
        writer.asString());
    assertTrue(writer.getRefs().exists(JAVA_UTIL_ARRAYS::equals));
  }

  @Test
  void genHashCodeMethod_when_genericSample_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = HashCodeGens.genHashCodeMethod();

    final PList<PojoField> fields =
        PList.of(new PojoField(Name.fromString("flag"), Types.primitiveBoolean(), REQUIRED));

    final Writer writer =
        generator.generate(
            Pojos.genericSample().withFields(fields).withGetters(fields.map(PojoFields::toGetter)),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    assertEquals(
        "default int genHashCode() {\n"
            + "  int result = Objects.hash(isFlag());\n"
            + "  return result;\n"
            + "}",
        writer.asString());
    assertTrue(writer.getRefs().exists(JAVA_UTIL_OBJECTS::equals));
    assertFalse(writer.getRefs().exists(JAVA_UTIL_ARRAYS::equals));
  }

  @Test
  void genHashCodeMethod_when_disabled_then_noOutput() {
    final Generator<Pojo, PojoSettings> generator = HashCodeGens.genHashCodeMethod();

    final Writer writer =
        generator.generate(
            Pojos.sample(),
            PojoSettings.defaultSettings().withEqualsHashCodeAbility(DISABLED),
            Writer.createDefault());
    assertEquals("", writer.asString());
    assertTrue(writer.getRefs().isEmpty());
  }
}
