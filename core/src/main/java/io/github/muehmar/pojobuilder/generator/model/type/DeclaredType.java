package io.github.muehmar.pojobuilder.generator.model.type;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import java.util.Optional;
import lombok.Value;

@Value
@PojoBuilder
public class DeclaredType implements SpecificType {
  Classname classname;
  PackageName pkg;
  PList<Type> typeParameters;

  public static DeclaredType fromNameAndPackage(Classname name, PackageName packageName) {
    return new DeclaredType(name, packageName, PList.empty());
  }

  public static DeclaredType of(Classname name, PackageName packageName, Type singeTypeParameter) {
    return new DeclaredType(name, packageName, PList.single(singeTypeParameter));
  }

  public static DeclaredType of(
      Classname name, PackageName packageName, PList<Type> typeParameters) {
    return new DeclaredType(name, packageName, typeParameters);
  }

  public static DeclaredType optional(Type value) {
    return of(Classname.fromString("Optional"), PackageName.javaUtil(), value);
  }

  @Override
  public TypeKind getKind() {
    return TypeKind.DECLARED;
  }

  @Override
  public Name getTypeDeclaration() {
    final Optional<Name> formattedTypeParameters =
        typeParameters
            .map(Type::getTypeDeclaration)
            .reduce((s1, s2) -> s1.append(",").append(s2))
            .map(s -> s.prefix("<").append(">"));
    return classname.asName().append(formattedTypeParameters.map(Name::asString).orElse(""));
  }

  @Override
  public PList<Name> getImports() {
    return PList.single(classname.getTopLevelClass().prefix(pkg + "."))
        .concat(typeParameters.flatMap(Type::getImports));
  }

  public Name getName() {
    return classname.asName();
  }

  public boolean isOptional() {
    return typeParameters.size() == 1 && this.equals(optional(typeParameters.head()));
  }
}
