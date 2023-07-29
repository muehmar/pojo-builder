package io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard;

import static io.github.muehmar.pojobuilder.generator.impl.gen.Filters.isStandardBuilderEnabled;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Generators.newLine;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.FieldBuilderClass.fieldBuilderClass;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard.FinalRequiredBuilder.finalRequiredBuilder;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.model.BuilderField;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.FinalBuilderClass;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.RawClassNameGenerator;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;

public class StandardBuilderGenerator {
  public static final RawClassNameGenerator CLASS_NAME_FOR_REQUIRED =
      fieldIndex -> String.format("Builder%d", fieldIndex);
  public static final RawClassNameGenerator CLASS_NAME_FOR_OPTIONAL =
      fieldIndex -> String.format("OptBuilder%d", fieldIndex);

  private StandardBuilderGenerator() {}

  public static Generator<Pojo, PojoSettings> standardBuilderGenerator() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .appendList(
            fieldBuilderClass(CLASS_NAME_FOR_REQUIRED), BuilderField::requiredFromPojo, newLine())
        .appendSingleBlankLine()
        .append(finalRequiredBuilder())
        .appendSingleBlankLine()
        .appendList(
            fieldBuilderClass(CLASS_NAME_FOR_OPTIONAL), BuilderField::optionalFromPojo, newLine())
        .appendSingleBlankLine()
        .append(
            FinalBuilderClass.finalBuilderClass(
                CLASS_NAME_FOR_OPTIONAL,
                pojo -> pojo.getFields().filter(PojoField::isOptional).size()))
        .filter(isStandardBuilderEnabled());
  }
}
