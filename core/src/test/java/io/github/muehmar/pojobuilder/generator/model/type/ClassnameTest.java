package io.github.muehmar.pojobuilder.generator.model.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.muehmar.pojobuilder.generator.model.Name;
import org.junit.jupiter.api.Test;

class ClassnameTest {

  @Test
  void getTopLevelClass_when_innerClass_then_outerClassnameReturned() {
    final Classname classname = Classname.fromString("Customer.Address.Street");

    assertEquals(Name.fromString("Customer"), classname.getTopLevelClass());
    assertEquals("Customer.Address.Street", classname.asName().asString());
  }

  @Test
  void getTopLevelClass_when_noInnerClass_then_classnameReturned() {
    final Classname classname = Classname.fromString("Customer");

    assertEquals(Name.fromString("Customer"), classname.getTopLevelClass());
  }

  @Test
  void getSimpleName_when_innerClass_then_onlySimpleNameReturned() {
    final Classname classname = Classname.fromString("Customer.Address.Street");

    assertEquals(Name.fromString("Street"), classname.getSimpleName());
  }

  @Test
  void getSimpleName_when_noInnerClass_then_classnameReturned() {
    final Classname classname = Classname.fromString("Customer");

    assertEquals(Name.fromString("Customer"), classname.getTopLevelClass());
  }
}
