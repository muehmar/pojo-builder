package io.github.muehmar.pojobuilder.processor.writer;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.Optionals;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.PojoBuilderGenerator;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.function.BiConsumer;
import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

public class PojoWriter {
  private final Optional<BiConsumer<Pojo, PojoSettings>> redirectPojo;

  private PojoWriter(Optional<BiConsumer<Pojo, PojoSettings>> redirectPojo) {
    this.redirectPojo = redirectPojo;
  }

  public static PojoWriter defaultWriter() {
    return new PojoWriter(Optional.empty());
  }

  public static PojoWriter redirectWriter(BiConsumer<Pojo, PojoSettings> redirectPojo) {
    return new PojoWriter(Optional.of(redirectPojo));
  }

  public void writePojo(Pojo pojo, PojoSettings pojoSettings, Filer filer) {
    Optionals.ifPresentOrElse(
        redirectPojo,
        output -> output.accept(pojo, pojoSettings),
        () -> writeBuilder(pojo, pojoSettings, filer));
  }

  private void writeBuilder(Pojo pojo, PojoSettings settings, Filer filer) {
    writeJavaFile(
        settings.qualifiedBuilderName(pojo),
        PojoBuilderGenerator.pojoBuilderGenerator(),
        pojo,
        settings,
        filer);
  }

  private void writeJavaFile(
      Name qualifiedClassName,
      Generator<Pojo, PojoSettings> gen,
      Pojo pojo,
      PojoSettings pojoSettings,
      Filer filer) {
    final String javaContent = gen.generate(pojo, pojoSettings, javaWriter()).asString();
    try {
      final JavaFileObject builderFile = filer.createSourceFile(qualifiedClassName.asString());
      try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
        out.println(javaContent);
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
