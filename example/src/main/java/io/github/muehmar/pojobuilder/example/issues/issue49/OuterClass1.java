package io.github.muehmar.pojobuilder.example.issues.issue49;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;

public class OuterClass1 {
  @PojoBuilder
  public static class InnerPojo {
    private final String value;

    public InnerPojo(String value) {
      this.value = value;
    }
  }
}
