package io.github.muehmar.pojobuilder.example.factorymethod;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.muehmar.pojobuilder.example.factorymethod.subpackage.Customer;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CustomerOptionalBuilderTest {
  @Test
  void customerOptionalBuilder_when_optionalOrNullableParameters_then_expectedInstance() {
    final Customer customer =
        CustomerOptionalBuilder.fullCustomerOptionalBuilder()
            .prop1(Optional.empty())
            .prop2(Optional.empty())
            .build();

    final Customer expectedCustomer = new Customer("defaultProp1", (byte) 0x12);
    assertEquals(expectedCustomer, customer);
  }
}
