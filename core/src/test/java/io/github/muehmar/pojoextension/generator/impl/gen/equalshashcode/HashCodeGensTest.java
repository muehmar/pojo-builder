package io.github.muehmar.pojoextension.generator.impl.gen.equalshashcode;

import static io.github.muehmar.pojoextension.generator.data.Necessity.REQUIRED;
import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_UTIL_ARRAYS;
import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_UTIL_OBJECTS;
import static org.junit.jupiter.api.Assertions.*;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojoextension.generator.Generator;
import io.github.muehmar.pojoextension.generator.PojoFields;
import io.github.muehmar.pojoextension.generator.Pojos;
import io.github.muehmar.pojoextension.generator.data.Name;
import io.github.muehmar.pojoextension.generator.data.Pojo;
import io.github.muehmar.pojoextension.generator.data.PojoField;
import io.github.muehmar.pojoextension.generator.data.PojoSettings;
import io.github.muehmar.pojoextension.generator.data.Type;
import io.github.muehmar.pojoextension.generator.writer.Writer;
import org.junit.jupiter.api.Test;

class HashCodeGensTest {

  @Test
  void staticHashCodeMethod_when_generatorUsedWithSamplePojoAndArrayField_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = HashCodeGens.staticHashCodeMethod();

    final PList<PojoField> fields =
        Pojos.sample()
            .getFields()
            .cons(
                new PojoField(
                    Name.fromString("byteArray"),
                    Type.primitive("byte").withIsArray(true),
                    REQUIRED));

    final Writer writer =
        generator.generate(
            Pojos.sample().withFields(fields).withGetters(fields.map(PojoFields::toGetter)),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    assertEquals(
        "public static int hashCode(Customer o) {\n"
            + "  int result = Objects.hash(o.getId(), o.getUsername(), o.getNickname());\n"
            + "  result = 31 * result + Arrays.hashCode(o.getByteArray());\n"
            + "  return result;\n"
            + "}",
        writer.asString());
    assertTrue(writer.getRefs().exists(JAVA_UTIL_OBJECTS::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_ARRAYS::equals));
  }

  @Test
  void staticHashCodeMethod_when_generatorUsedWithBooleanField_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = HashCodeGens.staticHashCodeMethod();

    final PList<PojoField> fields =
        PList.of(new PojoField(Name.fromString("flag"), Type.primitiveBoolean(), REQUIRED));

    final Writer writer =
        generator.generate(
            Pojos.sample().withFields(fields).withGetters(fields.map(PojoFields::toGetter)),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    assertEquals(
        "public static int hashCode(Customer o) {\n"
            + "  int result = Objects.hash(o.isFlag());\n"
            + "  return result;\n"
            + "}",
        writer.asString());
    assertTrue(writer.getRefs().exists(JAVA_UTIL_OBJECTS::equals));
    assertFalse(writer.getRefs().exists(JAVA_UTIL_ARRAYS::equals));
  }

  @Test
  void staticHashCodeMethod_when_generatorUsedWithTwoByteArrays_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = HashCodeGens.staticHashCodeMethod();

    final PList<PojoField> fields =
        PList.of(
            new PojoField(
                Name.fromString("byteArray"), Type.primitive("byte").withIsArray(true), REQUIRED),
            new PojoField(
                Name.fromString("byteArray2"), Type.primitive("byte").withIsArray(true), REQUIRED));

    final Writer writer =
        generator.generate(
            Pojos.sample().withFields(fields).withGetters(fields.map(PojoFields::toGetter)),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    assertEquals(
        "public static int hashCode(Customer o) {\n"
            + "  int result = Arrays.hashCode(o.getByteArray());\n"
            + "  result = 31 * result + Arrays.hashCode(o.getByteArray2());\n"
            + "  return result;\n"
            + "}",
        writer.asString());
    assertTrue(writer.getRefs().exists(JAVA_UTIL_ARRAYS::equals));
  }

  @Test
  void hashCodeMethod_when_generatorUsedWithSamplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = HashCodeGens.hashCodeMethod();
    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault());
    assertEquals(
        "@Override\n" + "public int hashCode() {\n" + "  return hashCode(self());\n" + "}",
        writer.asString());
  }
}