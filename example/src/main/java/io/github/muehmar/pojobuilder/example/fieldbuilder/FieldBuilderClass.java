package io.github.muehmar.pojobuilder.example.fieldbuilder;

import io.github.muehmar.pojobuilder.annotations.FieldBuilder;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.Value;

@Value
@PojoBuilder
public class FieldBuilderClass<T> {
  String prop1;
  String prop2;
  Optional<String> prop3;
  Optional<T> data;

  @FieldBuilder(fieldName = "prop1", disableDefaultMethods = true)
  static class Prop1Builder {
    private Prop1Builder() {}

    static String randomString() {
      return UUID.randomUUID().toString();
    }

    static String fromInt(Integer i) {
      return i.toString();
    }

    static String fromVarargs(String... args) {
      return String.join("-", args);
    }
  }

  @FieldBuilder(fieldName = "prop3")
  static class Prop3Builder {
    private Prop3Builder() {}

    static String constant() {
      return "CONSTANT";
    }
  }

  @FieldBuilder(fieldName = "data")
  static <T> T supplier(Supplier<T> s) {
    return s.get();
  }
}
