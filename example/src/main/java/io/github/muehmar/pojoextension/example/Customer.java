package io.github.muehmar.pojoextension.example;

import io.github.muehmar.pojoextension.annotations.Getter;
import io.github.muehmar.pojoextension.annotations.Nullable;
import io.github.muehmar.pojoextension.annotations.SafeBuilder;
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

  @Getter("id")
  public String getIdentification() {
    return id;
  }

  @Getter("nickname")
  public Optional<String> getNick() {
    return nickname;
  }

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
