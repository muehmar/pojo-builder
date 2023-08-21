package io.github.muehmar.pojobuilder.generator.impl.gen.builder.model;

import ch.bluecare.commons.data.NonEmptyList;
import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.FullBuilderFieldOrder;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.FieldBuilder;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.Value;
import lombok.With;

@Value
@With
@PojoBuilder
public class BuilderField {
  IndexedField indexedField;
  Optional<FieldBuilder> fieldBuilder;

  public Pojo getPojo() {
    return indexedField.getPojo();
  }

  public PojoField getField() {
    return indexedField.getField();
  }

  public int getIndex() {
    return indexedField.getIndex();
  }

  public boolean hasFieldBuilder() {
    return fieldBuilder.isPresent();
  }

  public boolean isDisableDefaultMethods() {
    return fieldBuilder.map(FieldBuilder::isDisableDefaultMethods).orElse(false);
  }

  public boolean isEnableDefaultMethods() {
    return !isDisableDefaultMethods();
  }

  public boolean isFieldOptional() {
    return indexedField.getField().isOptional();
  }

  public static PList<BuilderField> allFromPojo(Pojo pojo, FullBuilderFieldOrder fieldOrder) {
    switch (fieldOrder) {
      case REQUIRED_FIELDS_FIRST:
        {
          final PList<PojoField> fields = pojo.getFields();
          final PList<PojoField> requiredFields = fields.filter(PojoField::isRequired);
          final PList<PojoField> optionalFields = fields.filter(PojoField::isOptional);
          return fromPojoAndFields(pojo, requiredFields.concat(optionalFields), ignore -> true);
        }
      case DECLARATION_ORDER:
      default:
        return fromPojo(pojo, ignore -> true);
    }
  }

  public static PList<BuilderField> requiredFromPojo(Pojo pojo) {
    return fromPojo(pojo, PojoField::isRequired);
  }

  public static PList<BuilderField> optionalFromPojo(Pojo pojo) {
    return fromPojo(pojo, PojoField::isOptional);
  }

  private static PList<BuilderField> fromPojo(Pojo pojo, Predicate<PojoField> filter) {
    return fromPojoAndFields(pojo, pojo.getFields(), filter);
  }

  private static PList<BuilderField> fromPojoAndFields(
      Pojo pojo, PList<PojoField> fields, Predicate<PojoField> filter) {
    return fields
        .filter(filter)
        .zipWithIndex()
        .map(
            p -> {
              final PojoField field = p.first();
              final Integer index = p.second();
              final IndexedField indexedField = new IndexedField(pojo, field, index);
              final Optional<FieldBuilder> fieldBuilder =
                  pojo.getFieldBuilders()
                      .find(
                          builder ->
                              field.isFieldBuilder(pojo.getPojoClassname().getName(), builder));
              return new BuilderField(indexedField, fieldBuilder);
            });
  }

  public PList<BuilderFieldWithMethod> getBuilderFieldsWithMethod() {
    return fieldBuilder
        .map(FieldBuilder::getMethods)
        .map(NonEmptyList::toPList)
        .orElse(PList.empty())
        .map(fieldBuilderMethod -> new BuilderFieldWithMethod(this, fieldBuilderMethod));
  }
}
