package io.github.muehmar.pojobuilder.example;

import static io.github.muehmar.pojobuilder.example.GenericClassBuilder.create;
import static io.github.muehmar.pojobuilder.example.GenericClassBuilder.createFull;
import static io.github.muehmar.pojobuilder.example.GenericClassBuilder.fullGenericClassBuilder;
import static io.github.muehmar.pojobuilder.example.GenericClassBuilder.genericClassBuilder;
import static org.assertj.core.api.Assertions.assertThat;

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

    assertThat(genericClass.getData().get(0)).isEqualTo("Hello World!");
    assertThat(genericClass.getProp1()).isEqualTo("prop1");
    assertThat(genericClass.getCode()).isEqualTo(Code.A1);
  }

  @Test
  void create_when_used_then_compilesAndInstanceCreated() {
    final Data data = new Data();
    data.add("Hello World!");

    final GenericClass<Data, String> genericClass =
        create(Data.class, String.class).prop1("prop1").data(data).code(Code.A1).build();

    assertThat(genericClass).isNotNull();
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

    assertThat(genericClass).isNotNull();
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

    assertThat(genericClass).isNotNull();
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

    assertThat(genericClass).isNotNull();
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
