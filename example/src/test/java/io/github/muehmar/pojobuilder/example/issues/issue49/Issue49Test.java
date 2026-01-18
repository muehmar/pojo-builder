package io.github.muehmar.pojobuilder.example.issues.issue49;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Issue49Test {

  @Test
  void sample1Builder_when_usedWithFactoryMethod_then_correctInstanceCreated() {
    final Sample1 sample1 = Sample1Builder.fullSample1Builder().hello("world").build();

    assertThat(sample1).isNotNull();
  }

  @Test
  void sample2Builder_when_usedWithFactoryMethod_then_correctInstanceCreated() {
    final Sample2 sample2 = Sample2Builder.fullSample2Builder().hello("world").build();

    assertThat(sample2).isNotNull();
  }

  @Test
  void outerClass1InnerPojoBuilder_when_usedWithInnerClass_then_correctInstanceCreated() {
    final OuterClass1.InnerPojo inner =
        OuterClass1InnerPojoBuilder.fullOuterClass1InnerPojoBuilder().value("test").build();

    assertThat(inner).isNotNull();
  }

  @Test
  void outerClass2InnerPojoBuilder_when_usedWithInnerClass_then_correctInstanceCreated() {
    final OuterClass2.InnerPojo inner =
        OuterClass2InnerPojoBuilder.fullOuterClass2InnerPojoBuilder().value("test").build();

    assertThat(inner).isNotNull();
  }
}
