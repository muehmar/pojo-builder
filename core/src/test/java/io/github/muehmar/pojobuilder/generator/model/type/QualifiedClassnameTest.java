package io.github.muehmar.pojobuilder.generator.model.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.muehmar.pojobuilder.generator.model.PackageName;
import org.junit.jupiter.api.Test;

class QualifiedClassnameTest {

  @Test
  void getImport_when_innerClass_then_importForOuterClass() {
    final QualifiedClassname qualifiedClassname =
        new QualifiedClassname(
            Classname.fromString("OuterClass.InnerClass"),
            PackageName.fromString("io.github.muehmar"));

    assertEquals("io.github.muehmar.OuterClass", qualifiedClassname.getImport());
  }
}
