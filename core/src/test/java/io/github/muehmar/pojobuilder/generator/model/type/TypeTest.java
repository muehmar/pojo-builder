package io.github.muehmar.pojobuilder.generator.model.type;

import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_INTEGER;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_STRING;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_MAP;
import static org.assertj.core.api.Assertions.assertThat;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class TypeTest {

  @Test
  void getImports_when_javaMap_then_correctQualifiedNames() {
    final Type type = Types.map(Types.string(), Types.integer());
    final PList<String> allQualifiedNames = type.getImports().map(Name::asString);
    assertThat(allQualifiedNames.size()).isEqualTo(3);
    assertThat(allQualifiedNames.exists(JAVA_UTIL_MAP::equals)).isTrue();
    assertThat(allQualifiedNames.exists(JAVA_LANG_STRING::equals)).isTrue();
    assertThat(allQualifiedNames.exists(JAVA_LANG_INTEGER::equals)).isTrue();
  }

  @ParameterizedTest
  @EnumSource(PrimitiveType.class)
  void isPrimitive_when_calledForEachPrimitive_then_trueForAll(PrimitiveType primitiveType) {
    final Type type = Type.fromSpecificType(primitiveType);
    assertThat(type.isPrimitive()).isTrue();
  }

  @Test
  void isPrimitive_when_calledForNonPrimitives_then_falseForAll() {
    assertThat(Types.string().isPrimitive()).isFalse();
    assertThat(Types.integer().isPrimitive()).isFalse();
  }

  @Test
  void isVoid_when_calledForVoidType_then_true() {
    assertThat(Types.voidType().isVoid()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonArrayTypes")
  void isArray_when_calledForArray_then_true(Type type) {
    assertThat(Types.array(type).isArray()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonArrayTypes")
  void isArray_when_calledNonArrayTypes_then_false(Type type) {
    assertThat(type.isArray()).isFalse();
  }

  static Stream<Arguments> nonArrayTypes() {
    return Stream.of(
        Arguments.of(Types.booleanClass()),
        Arguments.of(Types.voidType()),
        Arguments.of(Types.integer()),
        Arguments.of(Types.primitiveChar()));
  }

  @Test
  void getRelation_when_unrelatedTypes_then_noRelationReturned() {
    final Optional<OptionalFieldRelation> relation = Types.string().getRelation(Types.integer());
    assertThat(relation.isPresent()).isFalse();
  }

  @Test
  void getRelation_when_stringAndOptionalString_then_relationIsWrapIntoOptional() {
    final Optional<OptionalFieldRelation> relation =
        Types.string().getRelation(Types.optional(Types.string()));
    assertThat(relation).isEqualTo(Optional.of(OptionalFieldRelation.WRAP_INTO_OPTIONAL));
  }

  @Test
  void getRelation_when_bothAreStrings_then_relationIsSameType() {
    final Optional<OptionalFieldRelation> relation = Types.string().getRelation(Types.string());
    assertThat(relation).isEqualTo(Optional.of(OptionalFieldRelation.SAME_TYPE));
  }

  @Test
  void fold_when_declaredType_then_correspondingFunctionExecutedWithDeclaredType() {
    final Type type = Types.optional(Types.string());

    final Function<DeclaredType, String> getTypeDeclaration =
        declaredType -> declaredType.getTypeDeclaration().asString();

    assertThat(type.fold(getTypeDeclaration, ignore -> "", ignore -> "", ignore -> ""))
        .isEqualTo("Optional<String>");
  }

  @Test
  void fold_when_arrayType_then_correspondingFunctionExecutedWithArrayType() {
    final Type type = Types.array(Types.string());

    final Function<ArrayType, String> getItemTypeName =
        arrayType -> arrayType.getItemType().getName().asString();
    assertThat(type.fold(ignore -> "", getItemTypeName, ignore -> "", ignore -> ""))
        .isEqualTo("String");
  }

  @Test
  void fold_when_primitiveType_then_correspondingFunctionExecutedWithPrimitiveType() {
    final Type type = Types.primitiveBoolean();

    final Function<PrimitiveType, String> getPrimitiveName =
        primitiveType -> primitiveType.getName().asString();
    assertThat(type.fold(ignore -> "", ignore -> "", getPrimitiveName, ignore -> ""))
        .isEqualTo("boolean");
  }

  @Test
  void fold_when_typeVariable_then_correspondingFunctionExecutedWithTypeVariableType() {
    final Type type = Types.typeVariable(Name.fromString("T"));

    final Function<TypeVariableType, Boolean> compareTypeVariableName =
        typeVariableType -> typeVariableType.getName().equals(Name.fromString("T"));

    assertThat(
            type.fold(ignore -> false, ignore -> false, ignore -> false, compareTypeVariableName))
        .isTrue();
  }

  @Test
  void onDeclaredType_when_declaredType_then_functionExecuted() {
    final Type type = Types.optional(Types.string());

    final Function<DeclaredType, String> getTypeDeclaration =
        declaredType -> declaredType.getTypeDeclaration().asString();

    final Optional<String> result = type.onDeclaredType(getTypeDeclaration);
    assertThat(result).isEqualTo(Optional.of("Optional<String>"));
  }

  @Test
  void onDeclaredType_when_nonDeclaredType_then_returnsEmptyOptional() {
    final Function<DeclaredType, String> getTypeDeclaration =
        declaredType -> declaredType.getTypeDeclaration().asString();

    assertThat(Types.array(Types.string()).onDeclaredType(getTypeDeclaration).isPresent())
        .isFalse();
    assertThat(
            Types.typeVariable(Name.fromString("T")).onDeclaredType(getTypeDeclaration).isPresent())
        .isFalse();
    assertThat(Types.primitiveBoolean().onDeclaredType(getTypeDeclaration).isPresent()).isFalse();
  }

  @Test
  void onArrayType_when_arrayType_then_functionExecuted() {
    final Type type = Types.array(Types.string());

    final Function<ArrayType, String> getItemType =
        arrayType -> arrayType.getItemType().getTypeDeclaration().asString();

    final Optional<String> result = type.onArrayType(getItemType);
    assertThat(result).isEqualTo(Optional.of("String"));
  }

  @Test
  void onArrayType_when_nonArrayType_then_returnsEmptyOptional() {
    final Function<ArrayType, String> getItemType =
        arrayType -> arrayType.getItemType().getTypeDeclaration().asString();

    assertThat(Types.string().onArrayType(getItemType).isPresent()).isFalse();
    assertThat(Types.typeVariable(Name.fromString("T")).onArrayType(getItemType).isPresent())
        .isFalse();
    assertThat(Types.primitiveBoolean().onArrayType(getItemType).isPresent()).isFalse();
  }
}
