package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.codegenerator.java.MethodGen;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe.ToPrepopulatedBuilderMethodGenerator;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;

public class ToBuilderMethod {
  private ToBuilderMethod() {}

  public static Generator<Pojo, PojoSettings> toBuilderMethod() {
    MethodGen<Pojo, PojoSettings> method =
        JavaGenerators.<Pojo, PojoSettings>methodGen()
            .modifiers(PUBLIC)
            .noGenericTypes()
            .returnType(
                (p, s, w) -> w.println("%s%s", s.builderName(p), p.getTypeVariablesFormatted()))
            .methodName("toBuilder")
            .noArguments()
            .doesNotThrow()
            .content("return builder.toBuilder();")
            .build();
    return ToPrepopulatedBuilderMethodGenerator.javaDoc().append(method);
  }
}
