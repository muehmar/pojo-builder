package io.github.muehmar.pojobuilder.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Classname;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import io.github.muehmar.pojobuilder.processor.writer.PojoWriter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Value;
import org.joor.CompileOptions;
import org.joor.Reflect;

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
    final PojoWriter pojoWriter =
        PojoWriter.redirectWriter((pojo, settings) -> ref.set(new PojoAndSettings(pojo, settings)));
    final PojoBuilderProcessor pojoBuilderProcessor = new PojoBuilderProcessor(pojoWriter);

    assertThatNoException()
        .as("Compilation failed")
        .isThrownBy(
            () -> {
              Reflect.compile(
                  classname.asString(),
                  content,
                  new CompileOptions().processors(pojoBuilderProcessor));
            });
    final PojoAndSettings pojoAndSettings = ref.get();
    assertThat(pojoAndSettings).as("Output not redirected").isNotNull();

    return pojoAndSettings;
  }

  @Value
  protected static class PojoAndSettings {
    Pojo pojo;
    PojoSettings settings;
  }
}
