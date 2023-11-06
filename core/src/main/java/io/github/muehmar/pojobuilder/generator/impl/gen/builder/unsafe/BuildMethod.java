package io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe;

import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;
import static io.github.muehmar.pojobuilder.Booleans.not;
import static io.github.muehmar.pojobuilder.generator.impl.gen.instantiation.ConstructorCallGenerator.constructorCallGenerator;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.pojobuilder.generator.impl.gen.instantiation.FactoryMethodCallGenerator;
import io.github.muehmar.pojobuilder.generator.model.Name;
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
        .content(buildMethodContent())
        .build();
  }

  private static Generator<Pojo, PojoSettings> buildMethodContent() {
    return constructorCall().append(factoryMethodCall());
  }

  private static Generator<Pojo, PojoSettings> factoryMethodCall() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .append((p, s, w) -> w.print("return "))
        .append(FactoryMethodCallGenerator.factoryMethodCallGenerator())
        .filter(pojo -> pojo.getFactoryMethod().isPresent());
  }

  private static Generator<Pojo, PojoSettings> constructorCall() {
    final Generator<Pojo, PojoSettings> returnGenerator =
        (p, s, w) ->
            p.getBuildMethod()
                .map(
                    bm ->
                        w.println(
                            "return %s.%s(instance);",
                            p.getPojoClassname().getName(), bm.getName()))
                .orElse(w.println("return instance;"));
    return Generator.<Pojo, PojoSettings>emptyGen()
        .append((p, s, w) -> w.println("final %s instance =", p.getPojoNameWithTypeVariables()))
        .append(constructorCallGenerator(), 2)
        .append(returnGenerator)
        .filter(pojo -> not(pojo.getFactoryMethod().isPresent()));
  }
}
