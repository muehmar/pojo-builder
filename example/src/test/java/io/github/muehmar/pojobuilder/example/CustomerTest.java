package io.github.muehmar.pojobuilder.example;

import static io.github.muehmar.pojobuilder.example.ProfessionBuilder.fullProfessionBuilder;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class CustomerTest {

  public static final String SAMPLE_ID = "123456";

  @Test
  void newBuilder_when_usedToCreateInstance_then_allAttributesSetAccordingly() {
    final Customer customer =
        CustomerBuilder.create()
            .setId(SAMPLE_ID)
            .setName("Dexter")
            .setRandom(12.5d)
            .setKey(new byte[] {0x15})
            .setFlag(true)
            .andAllOptionals()
            .setNickname("Dex")
            .setAge(empty())
            .build();

    assertThat(customer.getId()).isEqualTo(SAMPLE_ID);
    assertThat(customer.getName()).isEqualTo("Dexter");
    assertThat(customer.getRandom()).isEqualTo(12.5d);
    assertThat(customer.getKey()).containsExactly(new byte[] {0x15});
    assertThat(customer.getNickname()).isEqualTo(Optional.of("Dex"));
    assertThat(customer.getAge()).isEqualTo(empty());
  }

  @Test
  void newBuilder_when_calledForAddress_then_correctInstanceCreated() {
    final Customer.Address address =
        CustomerAddressBuilder.create()
            .street("Waldweg 10")
            .city("Winterthur")
            .andAllOptionals()
            .build();

    assertThat(address.getStreet()).isEqualTo("Waldweg 10");
    assertThat(address.getCity()).isEqualTo("Winterthur");
  }

  @Test
  void newBuilder_when_professionClass_then_noOuterClassIncluded() {
    final Customer.Profession profession =
        fullProfessionBuilder().name("Software developer").build();

    assertThat(profession.getName()).isEqualTo("Software developer");
  }
}
