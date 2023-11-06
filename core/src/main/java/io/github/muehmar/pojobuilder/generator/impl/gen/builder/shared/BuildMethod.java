package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import java.util.function.Function;

public class BuildMethod {
  private BuildMethod() {}

  public static Generator<Pojo, PojoSettings> buildMethod() {
    final Function<Pojo, Object> createReturnType =
        p ->
            p.getBuildMethod()
                .map(io.github.muehmar.pojobuilder.generator.model.BuildMethod::getReturnType)
                .map(Type::getTypeDeclaration)
                .map(Name::asString)
                .orElse(
                    String.format(
                        "%s%s",
                        p.getPkg().equals(p.getPojoClassname().getPkg())
                            ? ""
                            : p.getPojoClassname().getPkg() + ".",
                        p.getPojoNameWithTypeVariables()));
    return JavaGenerators.<Pojo, PojoSettings>methodGen()
        .modifiers(PUBLIC)
        .noGenericTypes()
        .returnType(createReturnType)
        .methodName("build")
        .noArguments()
        .doesNotThrow()
        .content("return builder.build();")
        .build();
  }
}
