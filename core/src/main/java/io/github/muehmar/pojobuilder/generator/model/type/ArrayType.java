package io.github.muehmar.pojobuilder.generator.model.type;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.Name;
import lombok.Value;

@Value
@PojoBuilder
public class ArrayType implements SpecificType {
  Type itemType;
  boolean isVarargs;

  public static ArrayType fromItemType(Type itemType) {
    return new ArrayType(itemType, false);
  }

  public static ArrayType varargs(Type itemType) {
    return new ArrayType(itemType, true);
  }

  @Override
  public Name getName() {
    return getTypeDeclaration();
  }

  @Override
  public TypeKind getKind() {
    return TypeKind.ARRAY;
  }

  @Override
  public Name getTypeDeclaration() {
    final String suffix = isVarargs ? "..." : "[]";
    return itemType.getTypeDeclaration().append(suffix);
  }

  @Override
  public PList<Name> getImports() {
    return itemType.getImports();
  }
}
