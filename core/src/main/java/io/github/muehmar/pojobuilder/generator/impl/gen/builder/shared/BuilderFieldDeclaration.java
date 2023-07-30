package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.generator.model.Pojo;

public class BuilderFieldDeclaration {
  private BuilderFieldDeclaration() {}

  public static <A> Generator<Pojo, A> builderFieldDeclaration() {
    return (p, s, w) -> w.println("private final Builder%s builder;", p.getTypeVariablesSection());
  }
}
