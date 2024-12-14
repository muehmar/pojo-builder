package io.github.muehmar.pojobuilder.generator.model.settings;

import static org.assertj.core.api.Assertions.assertThat;

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
    assertThat(name.asString()).isEqualTo("CustomerBuilder");
  }

  @Test
  void builderName_when_calledWithInnerClassName_then_correctBuilderName() {
    final Name name =
        PojoSettings.defaultSettings()
            .builderName(Pojos.sample().withPojoClassname(CUSTOMER_ADDRESS_CLASSNAME));
    assertThat(name.asString()).isEqualTo("CustomerAddressBuilder");
  }

  @Test
  void
      builderName_when_calledWithInnerClassNameButDoesNotIncludeOuterClassName_then_correctBuilderName() {
    final PojoSettings pojoSettings =
        PojoSettings.defaultSettings().withIncludeOuterClassName(false);
    final Name name =
        pojoSettings.builderName(Pojos.sample().withPojoClassname(CUSTOMER_ADDRESS_CLASSNAME));
    assertThat(name.asString()).isEqualTo("AddressBuilder");
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
    assertThat(name.asString()).isEqualTo("AddressSafeBuilder");
  }

  @Test
  void builderName_when_overriddenBuilderName_then_useCustomBuilderName() {
    final Name name =
        PojoSettings.defaultSettings()
            .withBuilderNameOpt(Name.fromString("MyBuilder"))
            .builderName(Pojos.sample());
    assertThat(name.asString()).isEqualTo("MyBuilder");
  }

  @Test
  void builderName_when_overriddenBuilderNameWithClassname_then_customBuilderNameCreated() {
    final Name name =
        PojoSettings.defaultSettings()
            .withBuilderNameOpt(Name.fromString("{CLASSNAME}SafeBuilder"))
            .builderName(Pojos.sample());
    assertThat(name.asString()).isEqualTo("CustomerSafeBuilder");
  }

  @Test
  void qualifiedBuilderName_when_calledWithSamplePojo_then_correctBuilderName() {
    final Name name = PojoSettings.defaultSettings().qualifiedBuilderName(Pojos.sample());
    assertThat(name.asString()).isEqualTo("io.github.muehmar.CustomerBuilder");
  }

  @Test
  void qualifiedBuilderName_when_calledWithInnerClassName_then_correctBuilderName() {
    final Name name =
        PojoSettings.defaultSettings()
            .qualifiedBuilderName(Pojos.sample().withPojoClassname(CUSTOMER_ADDRESS_CLASSNAME));
    assertThat(name.asString()).isEqualTo("io.github.muehmar.CustomerAddressBuilder");
  }
}
