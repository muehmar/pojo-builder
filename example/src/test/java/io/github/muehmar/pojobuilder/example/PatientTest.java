package io.github.muehmar.pojobuilder.example;

import static io.github.muehmar.pojobuilder.example.PatientBuilder.patientBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class PatientTest {
  @Test
  void fullPatientBuilder_when_used_then_firstNameOptional() {
    final Patient patient =
        patientBuilder()
            .lastName("lastName")
            .andAllOptionals()
            .firstName(Optional.of("firstName"))
            .build();

    assertEquals("firstName", patient.getFirstName());
    assertEquals("lastName", patient.getLastName());
  }
}
