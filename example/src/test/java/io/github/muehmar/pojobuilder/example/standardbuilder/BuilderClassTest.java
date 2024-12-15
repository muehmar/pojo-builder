package io.github.muehmar.pojobuilder.example.standardbuilder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BuilderClassTest {

  @Test
  void builderWithCustomNameCreated() {
    final BuilderClass builderClass =
        SafeBuilder.create()
            .prop1("prop1")
            .prop2("prop2")
            .prop3(new OuterClass.InnerClass("prop3"))
            .build();

    assertThat(builderClass.getProp1()).isEqualTo("prop1");
    assertThat(builderClass.getProp2()).isEqualTo("prop2");
    assertThat(builderClass.getProp3().getValue()).isEqualTo("prop3");
  }
}
