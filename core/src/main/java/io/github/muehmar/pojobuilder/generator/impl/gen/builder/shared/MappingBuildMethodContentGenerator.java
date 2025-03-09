package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.pojobuilder.Booleans.not;
import static io.github.muehmar.pojobuilder.generator.impl.gen.instantiation.ConstructorCallGenerator.constructorCallGenerator;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.generator.impl.gen.instantiation.FactoryMethodCallGenerator;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;

/**
 * Creates the build method content mapping the resulting instance to the desired class if
 * necessary.
 */
public class MappingBuildMethodContentGenerator {
  private MappingBuildMethodContentGenerator() {}

  public static Generator<Pojo, PojoSettings> mappingBuildMethodContentGenerator() {
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
