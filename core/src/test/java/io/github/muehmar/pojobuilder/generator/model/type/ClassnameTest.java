package io.github.muehmar.pojobuilder.generator.model.type;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.muehmar.pojobuilder.generator.model.Name;
import org.junit.jupiter.api.Test;

class ClassnameTest {

  @Test
  void getTopLevelClass_when_innerClass_then_outerClassnameReturned() {
    final Classname classname = Classname.fromString("Customer.Address.Street");

    assertThat(classname.getTopLevelClass()).isEqualTo(Name.fromString("Customer"));
    assertThat(classname.asName().asString()).isEqualTo("Customer.Address.Street");
  }

  @Test
  void getTopLevelClass_when_noInnerClass_then_classnameReturned() {
    final Classname classname = Classname.fromString("Customer");

    assertThat(classname.getTopLevelClass()).isEqualTo(Name.fromString("Customer"));
  }

  @Test
  void getSimpleName_when_innerClass_then_onlySimpleNameReturned() {
    final Classname classname = Classname.fromString("Customer.Address.Street");

    assertThat(classname.getSimpleName()).isEqualTo(Name.fromString("Street"));
  }

  @Test
  void getSimpleName_when_noInnerClass_then_classnameReturned() {
    final Classname classname = Classname.fromString("Customer");

    assertThat(classname.getTopLevelClass()).isEqualTo(Name.fromString("Customer"));
  }
}
