package io.github.muehmar.pojobuilder.generator.model;

import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_STRING;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_LIST;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.REQUIRED;
import static io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation.SAME_TYPE;
import static io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation.WRAP_INTO_OPTIONAL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.Names;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.matching.ArgumentsMatchingResult;
import io.github.muehmar.pojobuilder.generator.model.matching.ConstructorMatchingResults;
import io.github.muehmar.pojobuilder.generator.model.matching.FieldArgument;
import io.github.muehmar.pojobuilder.generator.model.matching.MatchingConstructor;
import io.github.muehmar.pojobuilder.generator.model.matching.MatchingConstructorBuilder;
import io.github.muehmar.pojobuilder.generator.model.matching.SingleArgumentMatchingResult;
import io.github.muehmar.pojobuilder.generator.model.settings.FieldMatching;
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

    final ConstructorMatchingResults constructorMatchingResults =
        pojo.findMatchingConstructor(FieldMatching.TYPE);

    final Optional<MatchingConstructor> matchingConstructor =
        constructorMatchingResults.getFirstMatchingConstructor();
    assertEquals(Optional.of(expected), matchingConstructor);
  }

  @Test
  void findMatchingConstructor_when_samplePojoWithoutConstructor_then_noMatchingConstructorFound() {
    final Pojo pojo = Pojos.sample().withConstructors(PList.empty());

    final ConstructorMatchingResults constructorMatchingResults =
        pojo.findMatchingConstructor(FieldMatching.TYPE);

    assertEquals(Optional.empty(), constructorMatchingResults.getFirstMatchingConstructor());
  }

  @Test
  void findMatchingConstructor_when_samplePojoAndOneRemovedField_then_noMatchingConstructorFound() {
    final Pojo p = Pojos.sample();
    final Pojo pojo = p.withFields(p.getFields().drop(1));

    final ConstructorMatchingResults constructorMatchingResults =
        pojo.findMatchingConstructor(FieldMatching.TYPE);

    assertEquals(Optional.empty(), constructorMatchingResults.getFirstMatchingConstructor());
  }

  @Test
  void
      findMatchingConstructor_when_samplePojoAndReversedFieldOrder_then_noMatchingConstructorFound() {
    final Pojo p = Pojos.sample();
    final Pojo pojo = p.withFields(p.getFields().reverse());

    final ConstructorMatchingResults constructorMatchingResults =
        pojo.findMatchingConstructor(FieldMatching.TYPE);

    assertEquals(Optional.empty(), constructorMatchingResults.getFirstMatchingConstructor());
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
  void matchArguments_when_orderAndTypesMatch_then_doesMatchAndReturnSameFields() {
    final PList<Argument> arguments =
        PList.of(
            new Argument(Names.id(), Types.string()), new Argument(Names.zip(), Types.integer()));

    final PList<PojoField> fields =
        PList.of(
            new PojoField(Names.id().append("_"), Types.string(), REQUIRED),
            new PojoField(Names.zip().append("_"), Types.integer(), OPTIONAL));

    final Pojo pojo = Pojos.sample().withFields(fields);

    // method call
    final ArgumentsMatchingResult result = pojo.matchArguments(arguments, FieldMatching.TYPE);

    final PList<SingleArgumentMatchingResult> expectedResults =
        fields
            .zip(arguments)
            .map(p -> new FieldArgument(p.first(), p.second(), SAME_TYPE))
            .map(SingleArgumentMatchingResult::fromFieldArgument);

    assertEquals(new ArgumentsMatchingResult(expectedResults), result);
  }

  @Test
  void matchArguments_when_orderAndTypesMatchButDifferentNameWhenMustMatch_then_doesNotMatch() {
    final PList<Argument> arguments =
        PList.of(
            new Argument(Names.id(), Types.string()), new Argument(Names.zip(), Types.integer()));

    final PList<PojoField> fields =
        PList.of(
            new PojoField(Names.id().append("_"), Types.string(), REQUIRED),
            new PojoField(Names.zip().append("_"), Types.integer(), OPTIONAL));

    final Pojo pojo = Pojos.sample().withFields(fields);

    // method call
    final ArgumentsMatchingResult result =
        pojo.matchArguments(arguments, FieldMatching.TYPE_AND_NAME);

    assertEquals(Optional.empty(), result.getFieldArguments());
  }

  @Test
  void matchArguments_when_sameTypesInWrongOrder_then_doesNotMatch() {
    final PList<Argument> arguments =
        PList.of(
            new Argument(Names.id(), Types.string()), new Argument(Names.zip(), Types.integer()));

    final PList<PojoField> fields =
        PList.of(
            new PojoField(Names.zip(), Types.integer(), REQUIRED),
            new PojoField(Names.id(), Types.string(), OPTIONAL));

    final Pojo pojo = Pojos.sample().withFields(fields);

    // method call
    final ArgumentsMatchingResult result = pojo.matchArguments(arguments, FieldMatching.TYPE);

    assertEquals(Optional.empty(), result.getFieldArguments());
  }

  @Test
  void
      matchArguments_when_sameTypesButOptionalFieldIsWrappedInOptional_then_doesMatchAndReturnSameFields() {
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
    final ArgumentsMatchingResult result = pojo.matchArguments(arguments, FieldMatching.TYPE);

    final PList<FieldArgument> expected =
        fields
            .zip(arguments)
            .map(
                p ->
                    new FieldArgument(
                        p.first(),
                        p.second(),
                        p.first().isRequired() ? SAME_TYPE : WRAP_INTO_OPTIONAL));

    assertEquals(Optional.of(expected), result.getFieldArguments());
  }

  @Test
  void matchArguments_when_sameTypesButRequiredFieldIsWrappedInOptional_then_doesNotMatch() {
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
    final ArgumentsMatchingResult result = pojo.matchArguments(arguments, FieldMatching.TYPE);

    assertEquals(Optional.empty(), result.getFieldArguments());
  }

  @Test
  void matchArguments_when_fieldsAndArgumentsHaveNotTheSameSize_then_doesNotMatch() {
    final PList<Argument> arguments =
        PList.of(
            new Argument(Names.id(), Types.string()),
            new Argument(Names.zip(), Types.optional(Types.integer())));

    final PList<PojoField> fields = PList.of(new PojoField(Names.id(), Types.string(), REQUIRED));

    final Pojo pojo = Pojos.sample().withFields(fields);

    // method call
    final ArgumentsMatchingResult result = pojo.matchArguments(arguments, FieldMatching.TYPE);

    assertEquals(Optional.empty(), result.getFieldArguments());
  }
}
