package io.github.muehmar.pojobuilder.example.safebuilder;

import io.github.muehmar.pojobuilder.annotations.SafeBuilder;
import lombok.Value;

@Value
@SafeBuilder(packagePrivateBuilder = true)
public class PackagePrivateBuilderClass {
  String prop1;
  String prop2;
}
