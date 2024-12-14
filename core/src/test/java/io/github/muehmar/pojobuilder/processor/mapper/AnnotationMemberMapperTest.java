package io.github.muehmar.pojobuilder.processor.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import org.junit.jupiter.api.Test;

class AnnotationMemberMapperTest {
  @Test
  void SafeBuilderContainsAllMethods() {
    final Class<?> clazz = PojoBuilder.class;

    assertThat(clazz)
        .hasDeclaredMethods(
            AnnotationMemberMapper.OPTIONAL_DETECTION,
            AnnotationMemberMapper.BUILDER_NAME,
            AnnotationMemberMapper.BUILDER_SET_METHOD_PREFIX,
            AnnotationMemberMapper.PACKAGE_PRIVATE_BUILDER,
            AnnotationMemberMapper.ENABLE_STANDARD_BUILDER,
            AnnotationMemberMapper.ENABLE_FULL_BUILDER,
            AnnotationMemberMapper.FULL_BUILDER_FIELD_ORDER,
            AnnotationMemberMapper.INCLUDE_OUTER_CLASS_NAME);
  }
}
