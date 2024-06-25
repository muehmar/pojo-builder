package io.github.muehmar.pojobuilder.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.Constructor;
import io.github.muehmar.pojobuilder.generator.model.Generic;
import io.github.muehmar.pojobuilder.generator.model.Generics;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Necessity;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.PojoFieldBuilder;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class AnnotatedConstructorProcessorTest extends BaseExtensionProcessorTest {
  @Test
  void run_when_simplePojoWithAnnotatedConstructor_then_correctPojo() {
    final QualifiedClassname pojoClassname = randomPojoClassname();

    final String classString =
        "package "
            + PACKAGE
            + ";\n"
            + "\n"
            + "import io.github.muehmar.pojobuilder.annotations.Nullable;\n"
            + "import io.github.muehmar.pojobuilder.annotations.PojoBuilder;\n"
            + "import java.util.Optional;\n"
            + "\n"
            + "public class "
            + pojoClassname.getSimpleName()
            + "<T> {\n"
            + "  private final String name;\n"
            + "  private final Optional<Integer> age;\n"
            + "  private final T data;\n"
            + "\n"
            + "  @PojoBuilder\n"
            + "  public "
            + pojoClassname.getSimpleName()
            + "(String name, @Nullable Integer age, T data) {\n"
            + "    this.name = name;\n"
            + "    this.age = Optional.ofNullable(age);\n"
            + "    this.data = data;\n"
            + "  }\n"
            + "}";

    final BaseExtensionProcessorTest.PojoAndSettings pojoAndSettings =
        runAnnotationProcessor(pojoClassname, classString);

    final Pojo pojo = pojoAndSettings.getPojo();

    final PojoField nameField =
        PojoFieldBuilder.pojoFieldBuilder()
            .name(Name.fromString("name"))
            .type(Types.string())
            .necessity(Necessity.REQUIRED)
            .build();

    final PojoField ageField =
        PojoFieldBuilder.pojoFieldBuilder()
            .name(Name.fromString("age"))
            .type(Types.integer())
            .necessity(Necessity.OPTIONAL)
            .build();

    final PojoField dataField =
        PojoFieldBuilder.pojoFieldBuilder()
            .name(Name.fromString("data"))
            .type(Types.typeVariable(Name.fromString("T")))
            .necessity(Necessity.REQUIRED)
            .build();

    final PList<PojoField> fields = PList.of(nameField, ageField, dataField);
    final Pojo expectedPojo =
        PojoBuilder.fullPojoBuilder()
            .pojoClassname(pojoClassname)
            .pojoNameWithTypeVariables(pojoClassname.getSimpleName().append("<T>"))
            .pkg(PACKAGE)
            .fields(fields)
            .constructors(
                PList.single(
                    new Constructor(
                        pojoClassname.getSimpleName(),
                        fields.map(f -> new Argument(f.getName(), f.getType())),
                        PList.empty())))
            .generics(Generics.of(new Generic(Name.fromString("T"), PList.empty())))
            .fieldBuilders(PList.empty())
            .factoryMethod(Optional.empty())
            .buildMethod(Optional.empty())
            .build();

    assertEquals(expectedPojo, pojo);
  }
}
