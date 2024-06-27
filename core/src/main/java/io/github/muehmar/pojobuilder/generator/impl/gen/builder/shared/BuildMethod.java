package io.github.muehmar.pojobuilder.generator.impl.gen.builder.shared;

import static io.github.muehmar.codegenerator.Generator.constant;
import static io.github.muehmar.codegenerator.java.JavaModifier.PUBLIC;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.java.JavaGenerators;
import io.github.muehmar.pojobuilder.generator.impl.gen.ThrowsGenerator;
import io.github.muehmar.pojobuilder.generator.model.Constructor;
import io.github.muehmar.pojobuilder.generator.model.FactoryMethod;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.matching.MatchingConstructor;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import java.util.function.Function;

public class BuildMethod {
  private BuildMethod() {}

  public static Generator<Pojo, PojoSettings> standardBuilderBuildMethod() {
    return buildMethod(constant("return builder.build();"));
  }

  public static Generator<Pojo, PojoSettings> buildMethod(
      Generator<Pojo, PojoSettings> buildMethodContent) {
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
        .throwsExceptions(throwsGenerator())
        .content(buildMethodContent)
        .build();
  }

  private static Generator<Pojo, PojoSettings> throwsGenerator() {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .append(ThrowsGenerator.throwsGenerator(), BuildMethod::getAllBuildExceptionsFromPojo);
  }

  private static PList<QualifiedClassname> getAllBuildExceptionsFromPojo(
      Pojo pojo, PojoSettings settings) {
    return getExceptionsFromFactoryMethod(pojo)
        .concat(getConstructorExceptionsFromPojo(pojo, settings))
        .concat(getBuildMethodExceptionsFromPojo(pojo))
        .distinct(Function.identity());
  }

  private static PList<QualifiedClassname> getExceptionsFromFactoryMethod(Pojo pojo) {
    return pojo.getFactoryMethod().map(FactoryMethod::getExceptions).orElseGet(PList::empty);
  }

  private static PList<QualifiedClassname> getConstructorExceptionsFromPojo(
      Pojo pojo, PojoSettings settings) {
    if (pojo.getFactoryMethod().isPresent()) {
      return PList.empty();
    }
    return pojo.findMatchingConstructor(settings.getFieldMatching())
        .getFirstMatchingConstructor()
        .map(MatchingConstructor::getConstructor)
        .map(Constructor::getExceptions)
        .orElseGet(PList::empty);
  }

  private static PList<QualifiedClassname> getBuildMethodExceptionsFromPojo(Pojo pojo) {
    return pojo.getBuildMethod()
        .map(io.github.muehmar.pojobuilder.generator.model.BuildMethod::getExceptions)
        .orElseGet(PList::empty);
  }
}
