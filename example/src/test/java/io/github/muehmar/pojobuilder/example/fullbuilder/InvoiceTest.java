package io.github.muehmar.pojobuilder.example.fullbuilder;

import static io.github.muehmar.pojobuilder.example.fullbuilder.InvoiceBuilder.fullInvoiceBuilder;
import static io.github.muehmar.pojobuilder.example.fullbuilder.InvoiceBuilder.invoiceBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.muehmar.pojobuilder.example.MethodHelper;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class InvoiceTest {
  @Test
  void fullBuilder_when_used_then_allPropertiesSet() {
    final Invoice invoice = fullInvoiceBuilder().prop1("value1").prop3(3).prop2("value2").build();

    assertEquals("value1", invoice.getProp1());
    assertEquals(3, invoice.getProp3());
    assertEquals(Optional.of("value2"), invoice.getProp2());
  }

  @Test
  void fullBuilder_when_lastRequiredBuilder_then_noOptionalsMethods() {
    final InvoiceBuilder.FullBuilder2 builder = fullInvoiceBuilder().prop1("value1").prop3(3);

    assertFalse(MethodHelper.hasMethod(builder.getClass(), "andOptionals"));
    assertFalse(MethodHelper.hasMethod(builder.getClass(), "andAllOptionals"));
  }

  @Test
  void standardBuilder_when_lastRequiredBuilder_then_hasOptionalsMethods() {
    final InvoiceBuilder.Builder2 builder = invoiceBuilder().prop1("value1").prop3(3);

    assertTrue(MethodHelper.hasMethod(builder.getClass(), "andOptionals"));
    assertTrue(MethodHelper.hasMethod(builder.getClass(), "andAllOptionals"));
  }
}
