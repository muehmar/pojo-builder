package io.github.muehmar.pojoextension.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
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
