package io.github.muehmar.pojobuilder.generator.model.matching;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.exception.PojoBuilderException;
import io.github.muehmar.pojobuilder.generator.Names;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.Constructor;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.FieldMatching;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import io.github.muehmar.pojobuilder.snapshottesting.SnapshotTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SnapshotTest
class ConstructorMatchingResultsTest {
  private Expect expect;

  @ParameterizedTest
  @EnumSource(FieldMatching.class)
  @SnapshotName("nonMatchingForSamplePojo")
  void
      getFirstMatchingConstructorOrThrow_when_hasNoMatchingConstructors_then_correctExceptionMessage(
          FieldMatching fieldMatching) {
    final Pojo samplePojo = Pojos.sample();

    final Constructor samplePojoConstructor = samplePojo.getConstructors().head();

    final SingleConstructorMatchingResult singleConstructorMatchingResult =
        SingleConstructorMatchingResult.ofMismatchReasons(
            new Constructor(
                samplePojoConstructor.getName(),
                samplePojoConstructor
                    .getArguments()
                    .add(new Argument(Names.id(), Types.optional(Types.integer()))),
                PList.empty()),
            PList.of(
                new MismatchReason("Field does not match name"),
                new MismatchReason("Field does not match type")));
    final ConstructorMatchingResults constructorMatchingResults =
        new ConstructorMatchingResults(PList.single(singleConstructorMatchingResult));

    final Throwable throwable =
        catchThrowable(
            () ->
                constructorMatchingResults.getFirstMatchingConstructorOrThrow(
                    samplePojo, fieldMatching));

    assertThat(throwable).isInstanceOf(PojoBuilderException.class);

    expect.scenario(fieldMatching.name()).toMatchSnapshot(throwable.getMessage());
  }
}
