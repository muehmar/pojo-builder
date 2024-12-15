package io.github.muehmar.pojobuilder.generator.model;

import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_STRING;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_LIST;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.REQUIRED;
import static io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation.SAME_TYPE;
import static io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation.WRAP_INTO_OPTIONAL;
import static org.assertj.core.api.Assertions.assertThat;

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
    assertThat(matchingConstructor).isEqualTo(Optional.of(expected));
  }

  @Test
  void findMatchingConstructor_when_samplePojoWithoutConstructor_then_noMatchingConstructorFound() {
    final Pojo pojo = Pojos.sample().withConstructors(PList.empty());

    final ConstructorMatchingResults constructorMatchingResults =
        pojo.findMatchingConstructor(FieldMatching.TYPE);

    assertThat(constructorMatchingResults.getFirstMatchingConstructor()).isEmpty();
  }

  @Test
  void findMatchingConstructor_when_samplePojoAndOneRemovedField_then_noMatchingConstructorFound() {
    final Pojo p = Pojos.sample();
    final Pojo pojo = p.withFields(p.getFields().drop(1));

    final ConstructorMatchingResults constructorMatchingResults =
        pojo.findMatchingConstructor(FieldMatching.TYPE);

    assertThat(constructorMatchingResults.getFirstMatchingConstructor()).isEmpty();
  }

  @Test
  void
      findMatchingConstructor_when_samplePojoAndReversedFieldOrder_then_noMatchingConstructorFound() {
    final Pojo p = Pojos.sample();
    final Pojo pojo = p.withFields(p.getFields().reverse());

    final ConstructorMatchingResults constructorMatchingResults =
        pojo.findMatchingConstructor(FieldMatching.TYPE);

    assertThat(constructorMatchingResults.getFirstMatchingConstructor()).isEmpty();
  }

  @Test
  void getDiamond_when_calledForNonGenericSample_then_empty() {
    assertThat(Pojos.sample().getDiamond()).isEqualTo("");
  }

  @Test
  void getDiamond_when_calledForGenericSample_then_empty() {
    assertThat(Pojos.genericSample().getDiamond()).isEqualTo("<>");
  }

  @Test
  void getTypeVariablesSection_when_calledForNonGenericSample_then_empty() {
    assertThat(Pojos.sample().getTypeVariablesFormatted()).isEqualTo("");
  }

  @Test
  void getTypeVariablesSection_when_calledForGenericSample_then_empty() {
    assertThat(Pojos.genericSample().getTypeVariablesFormatted()).isEqualTo("<T, S>");
  }

  @Test
  void getNameWithTypeVariables_when_calledForNonGenericSample_then_onlyName() {
    assertThat(Pojos.sample().getPojoNameWithTypeVariables().asString()).isEqualTo("Customer");
  }

  @Test
  void getNameWithTypeVariables_when_calledForGenericSample_then_nameWithTypeVariables() {
    assertThat(Pojos.genericSample().getPojoNameWithTypeVariables().asString())
        .isEqualTo("Customer<T, S>");
  }

  @Test
  void getGenericTypeDeclarations_when_calledForNonGenericSample_then_empty() {
    Pojo pojo = Pojos.sample();
    assertThat(pojo.getGenerics().asList().map(Generic::getTypeDeclaration).map(Name::asString))
        .isEqualTo(PList.empty());
  }

  @Test
  void getGenericTypeDeclarations_when_calledForGenericSample_then_empty() {
    Pojo pojo = Pojos.genericSample();
    assertThat(pojo.getGenerics().asList().map(Generic::getTypeDeclaration).map(Name::asString))
        .isEqualTo(PList.of("T extends List<String>", "S"));
  }

  @Test
  void getBoundedTypeVariablesSection_when_calledForNonGenericSample_then_empty() {
    assertThat(Pojos.sample().getBoundedTypeVariablesFormatted()).isEqualTo("");
  }

  @Test
  void getBoundedTypeVariablesSection_when_calledForGenericSample_then_empty() {
    assertThat(Pojos.genericSample().getBoundedTypeVariablesFormatted())
        .isEqualTo("<T extends List<String>, S>");
  }

  @Test
  void getGenericImports_when_calledForNonGenericSample_then_empty() {
    assertThat(Pojos.sample().getGenericImports()).isEqualTo(PList.empty());
  }

  @Test
  void getGenericImports_when_calledForGenericSample_then_empty() {
    final Set<Name> actual = Pojos.genericSample().getGenericImports().toHashSet();
    final Set<Name> expected =
        PList.of(Name.fromString(JAVA_UTIL_LIST), Name.fromString(JAVA_LANG_STRING)).toHashSet();
    assertThat(actual).isEqualTo(expected);
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

    assertThat(result).isEqualTo(new ArgumentsMatchingResult(expectedResults));
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

    assertThat(result.getFieldArguments()).isEmpty();
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

    assertThat(result.getFieldArguments()).isEmpty();
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

    assertThat(result.getFieldArguments()).isEqualTo(Optional.of(expected));
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

    assertThat(result.getFieldArguments()).isEmpty();
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

    assertThat(result.getFieldArguments()).isEmpty();
  }
}
