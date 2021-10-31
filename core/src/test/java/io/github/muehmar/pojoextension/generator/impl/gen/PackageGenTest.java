package io.github.muehmar.pojoextension.generator.impl.gen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.muehmar.pojoextension.generator.Pojos;
import io.github.muehmar.pojoextension.generator.data.PojoSettings;
import io.github.muehmar.pojoextension.generator.writer.Writer;
import org.junit.jupiter.api.Test;

class PackageGenTest {

  @Test
  void generate_when_called_then_correctPackageStatementCreated() {
    final PackageGen generator = new PackageGen();
    final Writer writer =
        generator.generate(Pojos.sample(), new PojoSettings(false), Writer.createDefault());

    assertEquals("package io.github.muehmar;", writer.asString());
  }
}
