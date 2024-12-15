package io.github.muehmar.pojobuilder.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class UserTest {
  @Test
  void newBuilder_when_usedToCreateInstanceEmptyOptional_then_allAttributesSetAccordingly() {
    final User user =
        UserBuilder.create().name("Joe").andAllOptionals().age(Optional.empty()).build();

    assertThat(user.getName()).isEqualTo("Joe");
    assertThat(user.getAge()).isEmpty();
  }

  @Test
  void newBuilder_when_usedToCreateInstanceSetOptional_then_allAttributesSetAccordingly() {
    final User user = UserBuilder.create().name("Joe").andAllOptionals().age(50).build();

    assertThat(user.getName()).isEqualTo("Joe");
    assertThat(user.getAge()).contains(50);
  }
}
