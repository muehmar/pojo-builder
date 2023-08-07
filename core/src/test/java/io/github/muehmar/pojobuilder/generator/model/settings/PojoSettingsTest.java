package io.github.muehmar.pojobuilder.generator.model.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PojoName;
import org.junit.jupiter.api.Test;

class PojoSettingsTest {
  @Test
  void builderName_when_calledWithSamplePojo_then_correctBuilderName() {
    final Name name = PojoSettings.defaultSettings().builderName(Pojos.sample());
    assertEquals("CustomerBuilder", name.asString());
  }

  @Test
  void builderName_when_calledWithInnerClassName_then_correctBuilderName() {
    final Name name =
        PojoSettings.defaultSettings()
            .builderName(Pojos.sample().withPojoName(PojoName.fromString("Customer.Address")));
    assertEquals("CustomerAddressBuilder", name.asString());
  }

  @Test
  void
      builderName_when_calledWithInnerClassNameButDoesNotIncludeOuterClassName_then_correctBuilderName() {
    final PojoSettings pojoSettings =
        PojoSettings.defaultSettings().withIncludeOuterClassName(false);
    final Name name =
        pojoSettings.builderName(
            Pojos.sample().withPojoName(PojoName.fromString("Customer.Address")));
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
        pojoSettings.builderName(
            Pojos.sample().withPojoName(PojoName.fromString("Customer.Address")));
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
            .qualifiedBuilderName(
                Pojos.sample().withPojoName(PojoName.fromString("Customer.Address")));
    assertEquals("io.github.muehmar.CustomerAddressBuilder", name.asString());
  }
}
