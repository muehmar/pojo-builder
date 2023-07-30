package io.github.muehmar.pojobuilder.example.fullbuilder;

import static io.github.muehmar.pojobuilder.example.fullbuilder.OfferBuilder.fullOfferBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class OfferTest {
  @Test
  void fullBuilder_when_used_then_declarationOrderAndAllPropertiesSet() {
    final Offer offer = fullOfferBuilder().prop1("value1").prop2("value2").prop3(3).build();

    assertEquals("value1", offer.getProp1());
    assertEquals(3, offer.getProp3());
    assertEquals(Optional.of("value2"), offer.getProp2());
  }
}
