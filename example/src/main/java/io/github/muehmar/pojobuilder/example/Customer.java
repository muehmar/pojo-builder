package io.github.muehmar.pojobuilder.example;

import io.github.muehmar.pojobuilder.annotations.Nullable;
import io.github.muehmar.pojobuilder.annotations.SafeBuilder;
import java.util.Optional;
import lombok.Value;

@SafeBuilder(builderSetMethodPrefix = "set")
@Value
public class Customer {
  String id;
  String name;
  Optional<String> nickname;
  @Nullable Integer age;
  double random;
  byte[] key;
  boolean flag;

  public Optional<Integer> getAge() {
    return Optional.ofNullable(age);
  }

  @Value
  @SafeBuilder(builderName = "CustomerAddressBuilder")
  public static class Address {
    String street;
    String city;
  }
}
