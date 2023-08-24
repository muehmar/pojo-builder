package io.github.muehmar.pojobuilder.generator.model;

import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_STRING;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_LIST;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.REQUIRED;
import static io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation.SAME_TYPE;
import static io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation.WRAP_INTO_OPTIONAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.Names;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PojoTest {

  @Test
  void findMatchingConstructor_when_samplePojo_then_matchForSingleConstructorAndFields() {
    final Pojo pojo = Pojos.sample();

    final PList<FieldArgument> fieldArguments =
        pojo.getFields()
            .zip(pojo.getConstructors().head().getArguments())
            .map(p -> new FieldArgument(p.first(), p.second(), SAME_TYPE));
    final MatchingConstructor expected =
        MatchingConstructorBuilder.create()
            .constructor(pojo.getConstructors().head())
            .fieldArguments(fieldArguments)
            .build();

    final Optional<MatchingConstructor> matchingConstructor = pojo.findMatchingConstructor();

    assertTrue(matchingConstructor.isPresent());
    assertEquals(expected, matchingConstructor.get());
  }

  @Test
  void findMatchingConstructor_when_samplePojoWithoutConstructor_then_noMatchingConstructorFound() {
    final Pojo pojo = Pojos.sample().withConstructors(PList.empty());
    final Optional<MatchingConstructor> matchingConstructor = pojo.findMatchingConstructor();

    assertFalse(matchingConstructor.isPresent());
  }

  @Test
  void findMatchingConstructor_when_samplePojoAndOneRemovedField_then_noMatchingConstructorFound() {
    final Pojo p = Pojos.sample();
    final Pojo pojo = p.withFields(p.getFields().drop(1));
    final Optional<MatchingConstructor> matchingConstructor = pojo.findMatchingConstructor();

    assertFalse(matchingConstructor.isPresent());
  }

  @Test
  void
      findMatchingConstructor_when_samplePojoAndReversedFieldOrder_then_noMatchingConstructorFound() {
    final Pojo p = Pojos.sample();
    final Pojo pojo = p.withFields(p.getFields().reverse());
    final Optional<MatchingConstructor> matchingConstructor = pojo.findMatchingConstructor();

    assertFalse(matchingConstructor.isPresent());
  }

  @Test
  void getDiamond_when_calledForNonGenericSample_then_empty() {
    assertEquals("", Pojos.sample().getDiamond());
  }

  @Test
  void getDiamond_when_calledForGenericSample_then_empty() {
    assertEquals("<>", Pojos.genericSample().getDiamond());
  }

  @Test
  void getTypeVariablesSection_when_calledForNonGenericSample_then_empty() {
    assertEquals("", Pojos.sample().getTypeVariablesFormatted());
  }

  @Test
  void getTypeVariablesSection_when_calledForGenericSample_then_empty() {
    assertEquals("<T, S>", Pojos.genericSample().getTypeVariablesFormatted());
  }

  @Test
  void getNameWithTypeVariables_when_calledForNonGenericSample_then_onlyName() {
    assertEquals("Customer", Pojos.sample().getPojoNameWithTypeVariables().asString());
  }

  @Test
  void getNameWithTypeVariables_when_calledForGenericSample_then_nameWithTypeVariables() {
    assertEquals("Customer<T, S>", Pojos.genericSample().getPojoNameWithTypeVariables().asString());
  }

  @Test
  void getGenericTypeDeclarations_when_calledForNonGenericSample_then_empty() {
    Pojo pojo = Pojos.sample();
    assertEquals(
        PList.empty(),
        pojo.getGenerics().asList().map(Generic::getTypeDeclaration).map(Name::asString));
  }

  @Test
  void getGenericTypeDeclarations_when_calledForGenericSample_then_empty() {
    Pojo pojo = Pojos.genericSample();
    assertEquals(
        PList.of("T extends List<String>", "S"),
        pojo.getGenerics().asList().map(Generic::getTypeDeclaration).map(Name::asString));
  }

  @Test
  void getBoundedTypeVariablesSection_when_calledForNonGenericSample_then_empty() {
    assertEquals("", Pojos.sample().getBoundedTypeVariablesFormatted());
  }

  @Test
  void getBoundedTypeVariablesSection_when_calledForGenericSample_then_empty() {
    assertEquals(
        "<T extends List<String>, S>", Pojos.genericSample().getBoundedTypeVariablesFormatted());
  }

  @Test
  void getGenericImports_when_calledForNonGenericSample_then_empty() {
    assertEquals(PList.empty(), Pojos.sample().getGenericImports());
  }

  @Test
  void getGenericImports_when_calledForGenericSample_then_empty() {
    final Set<Name> actual = Pojos.genericSample().getGenericImports().toHashSet();
    final Set<Name> expected =
        PList.of(Name.fromString(JAVA_UTIL_LIST), Name.fromString(JAVA_LANG_STRING)).toHashSet();
    assertEquals(expected, actual);
  }

  @Test
  void matchFields_when_sameOrderAndSameTypes_then_doesMatchAndReturnSameFields() {
    final PList<Argument> arguments =
        PList.of(
            new Argument(Names.id(), Types.string()), new Argument(Names.zip(), Types.integer()));

    final PList<PojoField> fields =
        PList.of(
            new PojoField(Names.id(), Types.string(), REQUIRED),
            new PojoField(Names.zip(), Types.integer(), OPTIONAL));

    final Pojo pojo = Pojos.sample().withFields(fields);

    // method call
    final Optional<PList<FieldArgument>> result = pojo.matchArguments(arguments);

    final PList<FieldArgument> expected =
        fields.zip(arguments).map(p -> new FieldArgument(p.first(), p.second(), SAME_TYPE));

    assertEquals(Optional.of(expected), result);
  }

  @Test
  void matchFields_when_sameTypesInWrongOrder_then_doesNotMatch() {
    final PList<Argument> arguments =
        PList.of(
            new Argument(Names.id(), Types.string()), new Argument(Names.zip(), Types.integer()));

    final PList<PojoField> fields =
        PList.of(
            new PojoField(Names.zip(), Types.integer(), REQUIRED),
            new PojoField(Names.id(), Types.string(), OPTIONAL));

    final Pojo pojo = Pojos.sample().withFields(fields);

    // method call
    final Optional<PList<FieldArgument>> result = pojo.matchArguments(arguments);

    assertEquals(Optional.empty(), result);
  }

  @Test
  void
      matchFields_when_sameTypesButOptionalFieldIsWrappedInOptional_then_doesMatchAndReturnSameFields() {
    final PList<Argument> arguments =
        PList.of(
            new Argument(Names.id(), Types.string()),
            new Argument(Names.zip(), Types.optional(Types.integer())));

    final PList<PojoField> fields =
        PList.of(
            new PojoField(Names.id(), Types.string(), REQUIRED),
            new PojoField(Names.zip(), Types.integer(), OPTIONAL));

    final Pojo pojo = Pojos.sample().withFields(fields);

    // method call
    final Optional<PList<FieldArgument>> result = pojo.matchArguments(arguments);

    final PList<FieldArgument> expected =
        fields
            .zip(arguments)
            .map(
                p ->
                    new FieldArgument(
                        p.first(),
                        p.second(),
                        p.first().isRequired() ? SAME_TYPE : WRAP_INTO_OPTIONAL));

    assertEquals(Optional.of(expected), result);
  }

  @Test
  void matchFields_when_sameTypesButRequiredFieldIsWrappedInOptional_then_doesNotMatch() {
    final PList<Argument> arguments =
        PList.of(
            new Argument(Names.id(), Types.optional(Types.string())),
            new Argument(Names.zip(), Types.integer()));

    final PList<PojoField> fields =
        PList.of(
            new PojoField(Names.id(), Types.string(), REQUIRED),
            new PojoField(Names.zip(), Types.integer(), OPTIONAL));

    final Pojo pojo = Pojos.sample().withFields(fields);

    // method call
    final Optional<PList<FieldArgument>> result = pojo.matchArguments(arguments);

    assertEquals(Optional.empty(), result);
  }

  @Test
  void matchFields_when_fieldsAndArgumentsHaveNotTheSameSize_then_doesNotMatch() {
    final PList<Argument> arguments =
        PList.of(
            new Argument(Names.id(), Types.string()),
            new Argument(Names.zip(), Types.optional(Types.integer())));

    final PList<PojoField> fields = PList.of(new PojoField(Names.id(), Types.string(), REQUIRED));

    final Pojo pojo = Pojos.sample().withFields(fields);

    // method call
    final Optional<PList<FieldArgument>> result = pojo.matchArguments(arguments);

    assertEquals(Optional.empty(), result);
  }
}
