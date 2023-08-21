package io.github.muehmar.pojobuilder.generator.model.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import io.github.muehmar.pojobuilder.generator.model.type.Classname;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import org.junit.jupiter.api.Test;

class PojoSettingsTest {
  private static final QualifiedClassname CUSTOMER_ADDRESS_CLASSNAME =
      new QualifiedClassname(
          Classname.fromString("Customer.Address"), PackageName.fromString("io.github.muehmar"));

  @Test
  void builderName_when_calledWithSamplePojo_then_correctBuilderName() {
    final Name name = PojoSettings.defaultSettings().builderName(Pojos.sample());
    assertEquals("CustomerBuilder", name.asString());
  }

  @Test
  void builderName_when_calledWithInnerClassName_then_correctBuilderName() {
    final Name name =
        PojoSettings.defaultSettings()
            .builderName(Pojos.sample().withPojoClassname(CUSTOMER_ADDRESS_CLASSNAME));
    assertEquals("CustomerAddressBuilder", name.asString());
  }

  @Test
  void
      builderName_when_calledWithInnerClassNameButDoesNotIncludeOuterClassName_then_correctBuilderName() {
    final PojoSettings pojoSettings =
        PojoSettings.defaultSettings().withIncludeOuterClassName(false);
    final Name name =
        pojoSettings.builderName(Pojos.sample().withPojoClassname(CUSTOMER_ADDRESS_CLASSNAME));
    assertEquals("AddressBuilder", name.asString());
  }

  @Test
  void
      builderName_when_doesNotIncludeOuterClassNameAndCustomPatternForInnerClass_then_correctBuilderName() {
    final PojoSettings pojoSettings =
        PojoSettings.defaultSettings()
            .withIncludeOuterClassName(false)
            .withBuilderNameOpt(Name.fromString("{CLASSNAME}SafeBuilder"));
    final Name name =
        pojoSettings.builderName(Pojos.sample().withPojoClassname(CUSTOMER_ADDRESS_CLASSNAME));
    assertEquals("AddressSafeBuilder", name.asString());
  }

  @Test
  void builderName_when_overriddenBuilderName_then_useCustomBuilderName() {
    final Name name =
        PojoSettings.defaultSettings()
            .withBuilderNameOpt(Name.fromString("MyBuilder"))
            .builderName(Pojos.sample());
    assertEquals("MyBuilder", name.asString());
  }

  @Test
  void builderName_when_overriddenBuilderNameWithClassname_then_customBuilderNameCreated() {
    final Name name =
        PojoSettings.defaultSettings()
            .withBuilderNameOpt(Name.fromString("{CLASSNAME}SafeBuilder"))
            .builderName(Pojos.sample());
    assertEquals("CustomerSafeBuilder", name.asString());
  }

  @Test
  void qualifiedBuilderName_when_calledWithSamplePojo_then_correctBuilderName() {
    final Name name = PojoSettings.defaultSettings().qualifiedBuilderName(Pojos.sample());
    assertEquals("io.github.muehmar.CustomerBuilder", name.asString());
  }

  @Test
  void qualifiedBuilderName_when_calledWithInnerClassName_then_correctBuilderName() {
    final Name name =
        PojoSettings.defaultSettings()
            .qualifiedBuilderName(Pojos.sample().withPojoClassname(CUSTOMER_ADDRESS_CLASSNAME));
    assertEquals("io.github.muehmar.CustomerAddressBuilder", name.asString());
  }
}
