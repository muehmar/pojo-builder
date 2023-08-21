package io.github.muehmar.pojobuilder.processor;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Classname;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Value;
import org.joor.CompileOptions;
import org.joor.Reflect;
import org.junit.jupiter.api.Assertions;

public abstract class BaseExtensionProcessorTest {
  protected static final PackageName PACKAGE = PackageName.fromString("io.github.muehmar");

  protected static QualifiedClassname randomPojoClassname() {
    return new QualifiedClassname(
        Classname.fromString(
            Name.fromString("Customer")
                .append(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10))
                .asString()),
        PACKAGE);
  }

  protected static PojoAndSettings runAnnotationProcessor(
      QualifiedClassname classname, String content) {
    final AtomicReference<PojoAndSettings> ref = new AtomicReference<>();
    final PojoBuilderProcessor pojoBuilderProcessor =
        new PojoBuilderProcessor(
            ((pojo, settings) -> ref.set(new PojoAndSettings(pojo, settings))));

    try {
      Reflect.compile(
          classname.asString(), content, new CompileOptions().processors(pojoBuilderProcessor));
    } catch (Exception e) {
      Assertions.fail("Compilation failed: " + e.getMessage());
    }
    final PojoAndSettings pojoAndSettings = ref.get();
    assertNotNull(pojoAndSettings, "Output not redirected");

    return pojoAndSettings;
  }

  @Value
  protected static class PojoAndSettings {
    Pojo pojo;
    PojoSettings settings;
  }
}
