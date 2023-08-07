package io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe;

import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.pojobuilder.generator.impl.gen.instantiation.ConstructorCallGens;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import java.util.function.Function;

class BuildMethod {
  private BuildMethod() {}

  public static Generator<Pojo, PojoSettings> buildMethod() {
    final Function<Pojo, Object> createReturnType =
        p ->
            p.getBuildMethod()
                .map(io.github.muehmar.pojobuilder.generator.model.BuildMethod::getReturnType)
                .map(Type::getTypeDeclaration)
                .orElseGet(p::getNameWithTypeVariables);
    return JavaGenerators.<Pojo, PojoSettings>methodGen()
        .modifiers(PUBLIC)
        .noGenericTypes()
        .returnTypeName(createReturnType)
        .methodName("build")
        .noArguments()
        .content(buildMethodContent())
        .build();
  }

  private static Generator<Pojo, PojoSettings> buildMethodContent() {
    final Generator<Pojo, PojoSettings> returnGenerator =
        (p, s, w) ->
            p.getBuildMethod()
                .map(bm -> w.println("return %s.%s(instance);", p.getPojoName(), bm.getName()))
                .orElse(w.println("return instance;"));
    return Generator.<Pojo, PojoSettings>emptyGen()
        .append((p, s, w) -> w.println("final %s instance =", p.getNameWithTypeVariables()))
        .append(ConstructorCallGens.callWithAllLocalVariables(""), 2)
        .append(returnGenerator);
  }
}
