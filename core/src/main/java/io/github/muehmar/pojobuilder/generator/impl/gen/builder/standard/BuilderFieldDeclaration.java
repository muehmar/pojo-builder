package io.github.muehmar.pojobuilder.generator.impl.gen.builder.standard;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.generator.model.Pojo;

class BuilderFieldDeclaration {
  private BuilderFieldDeclaration() {}

  public static <A> Generator<Pojo, A> builderFieldDeclaration() {
    return (p, s, w) -> w.println("private final Builder%s builder;", p.getTypeVariablesSection());
  }
}
