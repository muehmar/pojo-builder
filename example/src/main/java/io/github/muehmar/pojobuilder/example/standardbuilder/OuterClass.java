package io.github.muehmar.pojobuilder.example.standardbuilder;

import lombok.Value;

public class OuterClass {
  private OuterClass() {}

  @Value
  public static class InnerClass {
    String value;
  }
}
