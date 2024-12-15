package io.github.muehmar.pojobuilder.generator.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PackageNameTest {
  @Test
  void qualifiedName_when_called_then_correctQualifiedName() {
    final Name customer = Name.fromString("Customer");
    final PackageName packageName = PackageName.fromString("io.github.muehmar");

    final Name qualifiedName = packageName.qualifiedName(customer);
    assertThat(qualifiedName.asString()).isEqualTo("io.github.muehmar.Customer");
  }
}
