package io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard;

import static io.github.muehmar.pojobuilder.generator.impl.gen.Generators.newLine;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.FieldBuilderClass.fieldBuilderClass;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.FinalOptionalBuilder.finalOptionalBuilder;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.FinalRequiredBuilder.finalRequiredBuilder;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.model.BuilderField;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;

public class StandardBuilderGenerator {
  public static final String BUILDER_ASSIGNMENT = "this.builder = builder;";

  private StandardBuilderGenerator() {}

  public static Generator<Pojo, PojoSettings> standardBuilderGenerator() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .appendList(fieldBuilderClass(), BuilderField::requiredFromPojo, newLine())
        .appendSingleBlankLine()
        .append(finalRequiredBuilder())
        .appendSingleBlankLine()
        .appendList(fieldBuilderClass(), BuilderField::optionalFromPojo, newLine())
        .appendSingleBlankLine()
        .append(finalOptionalBuilder());
  }
}
