package io.github.muehmar.pojobuilder.example.factorymethod;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class GenericClassTest {

  @Test
  void
      genericClassBuilder_when_additionalGenericTypeParameterInFactoryMethod_then_expectedInstance() {
    final GenericClass<Data, Byte> genericClass =
        GenericClassBuilder.<Data, Byte, Integer>fullGenericClassBuilder()
            .creator(() -> 55)
            .prop1Mapper(i -> Integer.toString(i))
            .data(new Data())
            .code(Code.A1)
            .additionalData((byte) 0x55)
            .build();

    final GenericClass<Data, Byte> expectedGenericClass =
        new GenericClass<>("55", new Data(), Optional.of((byte) 0x55), Code.A1);

    assertThat(genericClass).isEqualTo(expectedGenericClass);
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
    A1
  }
}
