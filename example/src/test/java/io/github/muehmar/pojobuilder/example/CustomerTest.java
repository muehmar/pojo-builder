package io.github.muehmar.pojobuilder.example;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    assertEquals(SAMPLE_ID, customer.getId());
    assertEquals("Dexter", customer.getName());
    assertEquals(12.5d, customer.getRandom());
    assertArrayEquals(new byte[] {0x15}, customer.getKey());
    assertEquals(Optional.of("Dex"), customer.getNickname());
    assertEquals(empty(), customer.getAge());
  }

  @Test
  void newBuilder_when_calledForAddress_then_correctInstanceCreated() {
    final Customer.Address address =
        CustomerAddressBuilder.create()
            .street("Waldweg 10")
            .city("Winterthur")
            .andAllOptionals()
            .build();

    assertEquals("Waldweg 10", address.getStreet());
    assertEquals("Winterthur", address.getCity());
  }
}
