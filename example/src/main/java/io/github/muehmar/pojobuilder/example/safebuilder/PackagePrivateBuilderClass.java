package io.github.muehmar.pojobuilder.example.safebuilder;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import lombok.Value;

@Value
@PojoBuilder(packagePrivateBuilder = true)
public class PackagePrivateBuilderClass {
  String prop1;
  String prop2;
}
