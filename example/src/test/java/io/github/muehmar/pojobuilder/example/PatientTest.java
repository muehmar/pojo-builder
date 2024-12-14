package io.github.muehmar.pojobuilder.example;

import static io.github.muehmar.pojobuilder.example.PatientBuilder.patientBuilder;
import static org.assertj.core.api.Assertions.assertThat;

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

    assertThat(patient.getFirstName()).isEqualTo("firstName");
    assertThat(patient.getLastName()).isEqualTo("lastName");
  }
}
