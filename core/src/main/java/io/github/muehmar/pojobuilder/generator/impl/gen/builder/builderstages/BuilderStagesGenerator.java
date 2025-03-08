package io.github.muehmar.pojobuilder.generator.impl.gen.builder.builderstages;

import static io.github.muehmar.codegenerator.java.JavaModifier.*;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;

public class BuilderStagesGenerator {
  public static final String BUILDER_STAGES_CLASS_NAME = "BuilderStages";

  private BuilderStagesGenerator() {}

  public static Generator<Pojo, PojoSettings> builderStagesGenerator(
      Generator<Pojo, PojoSettings> content) {
    return JavaGenerators.<Pojo, PojoSettings>classGen()
        .clazz()
        .nested()
        .packageGen(Generator.emptyGen())
        .noJavaDoc()
        .noAnnotations()
        .modifiers(PUBLIC, STATIC, FINAL)
        .className(BUILDER_STAGES_CLASS_NAME)
        .noSuperClass()
        .noInterfaces()
        .content(content)
        .build();
  }
}
