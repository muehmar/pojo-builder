package io.github.muehmar.pojobuilder.example.fullbuilder;

import io.github.muehmar.pojobuilder.annotations.FullBuilderFieldOrder;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.util.Optional;
import lombok.Value;

@PojoBuilder(fullBuilderFieldOrder = FullBuilderFieldOrder.DECLARATION_ORDER)
@Value
public class Offer {
  String prop1;
  Optional<String> prop2;
  int prop3;
}
