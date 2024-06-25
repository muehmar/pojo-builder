package io.github.muehmar.pojobuilder.example.throwingconstructor;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.net.MalformedURLException;
import java.net.URL;

@PojoBuilder
public class ThrowingConstructorClass {
  private final String prop1;

  public ThrowingConstructorClass(String prop1) throws MalformedURLException {
    new URL(prop1);
    this.prop1 = prop1;
  }
}
