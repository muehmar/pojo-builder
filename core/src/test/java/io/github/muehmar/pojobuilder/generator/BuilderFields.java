package io.github.muehmar.pojobuilder.generator;

import static java.util.Optional.empty;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.model.BuilderField;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.model.BuilderFieldBuilder;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.model.IndexedField;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;

public class BuilderFields {
  private BuilderFields() {}

  public static BuilderField of(Pojo pojo, PojoField field, int index) {
    if (pojo.getFields().size() < index) {
      throw new IllegalArgumentException(
          "Pojo has not enough fields " + pojo.getFields().size() + " < " + index);
    }
    final PList<PojoField> fields = pojo.getFields().take(index).add(field);
    final IndexedField indexedField = new IndexedField(pojo.withFields(fields), field, index);
    return BuilderFieldBuilder.create()
        .indexedField(indexedField)
        .andAllOptionals()
        .fieldBuilder(empty())
        .build();
  }
}
