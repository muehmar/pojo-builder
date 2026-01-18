package io.github.muehmar.pojobuilder.example.issues.issue49;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;

public class Sample2 {
  private final String hello;

  public Sample2(String hello) {
    this.hello = hello;
  }

  @PojoBuilder
  public static Sample2 sample(String hello) {
    return new Sample2(hello);
  }
}
