package io.github.muehmar.pojobuilder.example;

import static io.github.muehmar.pojobuilder.example.GenericClassBuilder.create;
import static io.github.muehmar.pojobuilder.example.GenericClassBuilder.createFull;
import static io.github.muehmar.pojobuilder.example.GenericClassBuilder.fullGenericClassBuilder;
import static io.github.muehmar.pojobuilder.example.GenericClassBuilder.genericClassBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class GenericClassTest {
  @Test
  void createInGenericClassBuilder_when_calledAndBuilderUsed_then_correctInstanceCreated() {
    final Data data = new Data();
    data.add("Hello World!");

    final GenericClass<Data, String> genericClass =
        GenericClassBuilder.<Data, String>create().prop1("prop1").data(data).code(Code.A1).build();

    assertEquals("Hello World!", genericClass.getData().get(0));
    assertEquals("prop1", genericClass.getProp1());
    assertEquals(Code.A1, genericClass.getCode());
  }

  @Test
  void create_when_used_then_compilesAndInstanceCreated() {
    final Data data = new Data();
    data.add("Hello World!");

    final GenericClass<Data, String> genericClass =
        create(Data.class, String.class).prop1("prop1").data(data).code(Code.A1).build();

    assertNotNull(genericClass);
  }

  @Test
  void genericClassBuilder_when_used_then_compilesAndInstanceCreated() {
    final Data data = new Data();
    data.add("Hello World!");

    final GenericClass<Data, String> genericClass =
        genericClassBuilder(Data.class, String.class)
            .prop1("prop1")
            .data(data)
            .code(Code.A1)
            .build();

    assertNotNull(genericClass);
  }

  @Test
  void createFull_when_used_then_compilesAndInstanceCreated() {
    final Data data = new Data();
    data.add("Hello World!");

    final GenericClass<Data, String> genericClass =
        createFull(Data.class, String.class)
            .prop1("prop1")
            .data(data)
            .code(Code.A1)
            .additionalData(Optional.empty())
            .build();

    assertNotNull(genericClass);
  }

  @Test
  void fullGenericClassBuilder_when_used_then_compilesAndInstanceCreated() {
    final Data data = new Data();
    data.add("Hello World!");

    final GenericClass<Data, String> genericClass =
        fullGenericClassBuilder(Data.class, String.class)
            .prop1("prop1")
            .data(data)
            .code(Code.A1)
            .additionalData(Optional.empty())
            .build();

    assertNotNull(genericClass);
  }

  public static class Data extends ArrayList<String> implements Comparable<Data> {
    @Override
    public int compareTo(Data o) {
      return 0;
    }

    @Override
    public String toString() {
      return "Data{}";
    }
  }

  private enum Code {
    A1,
    A2
  }
}
