package io.github.muehmar.pojobuilder.generator.model;

import static io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation.SAME_TYPE;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import java.util.Optional;
import lombok.Value;
import lombok.With;

@Value
@With
@PojoBuilder
public class Argument {
  Name name;
  Type type;

  public static Argument of(Name name, Type type) {
    return new Argument(name, type);
  }

  public String formatted() {
    return String.format("%s %s", type.getTypeDeclaration(), name);
  }

  /** Returns the relation from the field to the argument */
  public Optional<OptionalFieldRelation> getRelationFromField(PojoField field) {
    if (field.isRequired()) {
      return type.equals(field.getType()) ? Optional.of(SAME_TYPE) : Optional.empty();
    }

    return field.getType().getRelation(this.getType());
  }
}
