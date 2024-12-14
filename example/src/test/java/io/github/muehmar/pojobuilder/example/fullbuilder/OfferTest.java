package io.github.muehmar.pojobuilder.example.fullbuilder;

import static io.github.muehmar.pojobuilder.example.fullbuilder.OfferBuilder.fullOfferBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class OfferTest {
  @Test
  void fullBuilder_when_used_then_declarationOrderAndAllPropertiesSet() {
    final Offer offer = fullOfferBuilder().prop1("value1").prop2("value2").prop3(3).build();

    assertThat(offer.getProp1()).isEqualTo("value1");
    assertThat(offer.getProp3()).isEqualTo(3);
    assertThat(offer.getProp2()).isEqualTo(Optional.of("value2"));
  }
}
