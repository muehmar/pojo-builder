package io.github.muehmar.pojobuilder;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class OptionalsTest {
  @Test
  void ifPresentOrElse_when_empty_then_orElseRunnableCalled() {
    final AtomicBoolean runnableCalled = new AtomicBoolean(false);
    final AtomicBoolean consumerCalled = new AtomicBoolean(false);
    final Optional<Integer> value = Optional.empty();
    Optionals.ifPresentOrElse(
        value, ignore -> consumerCalled.set(true), () -> runnableCalled.set(true));

    assertThat(runnableCalled.get()).isTrue();
    assertThat(consumerCalled.get()).isFalse();
  }

  @Test
  void ifPresentOrElse_when_valuePresent_then_consumerWithValueCalled() {
    final AtomicBoolean runnableCalled = new AtomicBoolean(false);
    final AtomicInteger consumerValue = new AtomicInteger(0);
    final Optional<Integer> value = Optional.of(10);
    Optionals.ifPresentOrElse(value, consumerValue::set, () -> runnableCalled.set(true));

    assertThat(runnableCalled.get()).isFalse();
    assertThat(consumerValue.get()).isEqualTo(10);
  }
}
