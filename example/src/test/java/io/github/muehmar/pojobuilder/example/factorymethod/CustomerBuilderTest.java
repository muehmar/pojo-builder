package io.github.muehmar.pojobuilder.example.factorymethod;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.muehmar.pojobuilder.example.factorymethod.subpackage.Customer;
import org.junit.jupiter.api.Test;

class CustomerBuilderTest {
  @Test
  void customerBuilder_when_builderClassIsInDifferentPackage_then_correctInstance() {
    final Customer customer = CustomerBuilder.fullCustomerBuilder().value((byte) 0x84).build();

    final Customer expectedCustomer = new Customer("PROP1", (byte) 0x84);

    assertThat(customer).isEqualTo(expectedCustomer);
  }
}
