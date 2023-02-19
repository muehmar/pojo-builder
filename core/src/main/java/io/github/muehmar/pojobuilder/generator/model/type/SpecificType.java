package io.github.muehmar.pojobuilder.generator.model.type;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Name;

public interface SpecificType {

  Name getName();

  TypeKind getKind();

  Name getTypeDeclaration();

  PList<Name> getImports();
}
