package io.github.muehmar.pojobuilder.generator.impl.gen.builder.full;

import static io.github.muehmar.pojobuilder.generator.impl.gen.Filters.isFullBuilderEnabled;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Generators.newLine;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.FieldBuilderClass.fieldBuilderClass;
import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.FinalBuilderClass.finalBuilderClass;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.model.BuilderField;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared.RawClassNameGenerator;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;

public class FullBuilderGenerator {

  public static final RawClassNameGenerator CLASS_NAME_FOR_FULL_BUILDER =
      fieldIndex -> String.format("FullBuilder%d", fieldIndex);

  private FullBuilderGenerator() {}

  public static Generator<Pojo, PojoSettings> fullBuilderGenerator() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .append(fieldBuilderClasses())
        .appendSingleBlankLine()
        .append(finalBuilderClass(CLASS_NAME_FOR_FULL_BUILDER, pojo -> pojo.getFields().size()))
        .filter(isFullBuilderEnabled());
  }

  private static Generator<Pojo, PojoSettings> fieldBuilderClasses() {
    return (pojo, s, w) -> {
      final PList<BuilderField> orderedFields =
          BuilderField.allFromPojo(pojo, s.getFullBuilderFieldOrder());
      return Generator.<PList<BuilderField>, PojoSettings>emptyGen()
          .appendList(fieldBuilderClass(CLASS_NAME_FOR_FULL_BUILDER), p -> p, newLine())
          .generate(orderedFields, s, w);
    };
  }
}
