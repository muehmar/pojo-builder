package io.github.muehmar.pojobuilder.example.factorymethod;

import io.github.muehmar.pojobuilder.annotations.Nullable;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.example.factorymethod.subpackage.Customer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class Factories {
  private Factories() {}

  @PojoBuilder
  static URL url(String spec) {
    try {
      return new URL(spec);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @PojoBuilder
  static Customer customer(Byte value) {
    return new Customer("PROP1", value);
  }

  @PojoBuilder(builderName = "CustomerOptionalBuilder")
  static Customer customer(Optional<String> prop1, @Nullable Byte prop2) {
    return new Customer(prop1.orElse("defaultProp1"), prop2 != null ? prop2 : (byte) 0x12);
  }
}
