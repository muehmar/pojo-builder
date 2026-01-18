package io.github.muehmar.pojobuilder.example.issues.issue49;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;

public class Sample1 {
  private final String hello;

  public Sample1(String hello) {
    this.hello = hello;
  }

  @PojoBuilder
  public static Sample1 sample(String hello) {
    return new Sample1(hello);
  }
}
