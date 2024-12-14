package io.github.muehmar.pojobuilder.example.fullbuilder;

import static io.github.muehmar.pojobuilder.example.fullbuilder.InvoiceBuilder.fullInvoiceBuilder;
import static io.github.muehmar.pojobuilder.example.fullbuilder.InvoiceBuilder.invoiceBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.muehmar.pojobuilder.example.MethodHelper;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class InvoiceTest {
  @Test
  void fullBuilder_when_used_then_allPropertiesSet() {
    final Invoice invoice = fullInvoiceBuilder().prop1("value1").prop3(3).prop2("value2").build();

    assertThat(invoice.getProp1()).isEqualTo("value1");
    assertThat(invoice.getProp3()).isEqualTo(3);
    assertThat(invoice.getProp2()).isEqualTo(Optional.of("value2"));
  }

  @Test
  void fullBuilder_when_lastRequiredBuilder_then_noOptionalsMethods() {
    final InvoiceBuilder.FullBuilder2 builder = fullInvoiceBuilder().prop1("value1").prop3(3);

    assertThat(MethodHelper.hasMethod(builder.getClass(), "andOptionals")).isFalse();
    assertThat(MethodHelper.hasMethod(builder.getClass(), "andAllOptionals")).isFalse();
  }

  @Test
  void standardBuilder_when_lastRequiredBuilder_then_hasOptionalsMethods() {
    final InvoiceBuilder.Builder2 builder = invoiceBuilder().prop1("value1").prop3(3);

    assertThat(MethodHelper.hasMethod(builder.getClass(), "andOptionals")).isTrue();
    assertThat(MethodHelper.hasMethod(builder.getClass(), "andAllOptionals")).isTrue();
  }
}
