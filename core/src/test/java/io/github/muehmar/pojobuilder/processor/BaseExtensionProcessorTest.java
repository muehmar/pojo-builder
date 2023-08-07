package io.github.muehmar.pojobuilder.processor;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoName;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Classname;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.joor.CompileOptions;
import org.joor.Reflect;
import org.junit.jupiter.api.Assertions;

public abstract class BaseExtensionProcessorTest {
  protected static final PackageName PACKAGE = PackageName.fromString("io.github.muehmar");

  protected static PojoName randomPojoName() {
    return PojoName.fromClassname(
        Classname.fromString(
            Name.fromString("Customer")
                .append(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10))
                .asString()));
  }

  protected static Name qualifiedPojoName(PojoName pojoName) {
    return pojoName.getName().prefix(".").prefix(PACKAGE.asString());
  }

  protected static PojoAndSettings runAnnotationProcessor(Name name, String content) {
    final AtomicReference<PojoAndSettings> ref = new AtomicReference<>();
    final PojoBuilderProcessor pojoBuilderProcessor =
        new PojoBuilderProcessor(
            ((pojo, settings) -> ref.set(new PojoAndSettings(pojo, settings))));

    try {
      Reflect.compile(
          name.asString(), content, new CompileOptions().processors(pojoBuilderProcessor));
    } catch (Exception e) {
      Assertions.fail("Compilation failed: " + e.getMessage());
    }
    final PojoAndSettings pojoAndSettings = ref.get();
    assertNotNull(pojoAndSettings, "Output not redirected");

    return pojoAndSettings;
  }

  protected static class PojoAndSettings {
    private final Pojo pojo;
    private final PojoSettings settings;

    public PojoAndSettings(Pojo pojo, PojoSettings settings) {
      this.pojo = pojo;
      this.settings = settings;
    }

    public Pojo getPojo() {
      return pojo;
    }

    public PojoSettings getSettings() {
      return settings;
    }
  }
}
