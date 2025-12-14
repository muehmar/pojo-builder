package io.github.muehmar.pojobuilder.generator.model.type;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Name;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class WildcardType implements SpecificType {
  private final Optional<Type> upperBound;
  private final Optional<Type> lowerBound;

  private WildcardType(Optional<Type> upperBound, Optional<Type> lowerBound) {
    this.upperBound = upperBound;
    this.lowerBound = lowerBound;
  }

  public static WildcardType create() {
    return new WildcardType(Optional.empty(), Optional.empty());
  }

  public static WildcardType upperBound(Type bound) {
    return new WildcardType(Optional.of(bound), Optional.empty());
  }

  public static WildcardType lowerBound(Type bound) {
    return new WildcardType(Optional.empty(), Optional.of(bound));
  }

  @Override
  public Name getName() {
    return Name.fromString("?");
  }

  @Override
  public TypeKind getKind() {
    return TypeKind.WILDCARD;
  }

  @Override
  public Name getTypeDeclaration() {
    if (upperBound.isPresent()) {
      return Name.fromString("? extends " + upperBound.get().getTypeDeclaration().asString());
    } else if (lowerBound.isPresent()) {
      return Name.fromString("? super " + lowerBound.get().getTypeDeclaration().asString());
    }
    return Name.fromString("?");
  }

  @Override
  public PList<Name> getImports() {
    return upperBound
        .map(Type::getImports)
        .orElseGet(() -> lowerBound.map(Type::getImports).orElse(PList.empty()));
  }
}
