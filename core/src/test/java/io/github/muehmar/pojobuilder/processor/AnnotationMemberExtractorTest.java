package io.github.muehmar.pojobuilder.processor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class AnnotationMemberExtractorTest {
  @Test
  void SafeBuilderContainsAllMethods() {
    final Class<?> clazz = PojoBuilder.class;

    assertTrue(hasMethod(clazz, AnnotationMemberExtractor.OPTIONAL_DETECTION));
    assertTrue(hasMethod(clazz, AnnotationMemberExtractor.BUILDER_NAME));
    assertTrue(hasMethod(clazz, AnnotationMemberExtractor.BUILDER_SET_METHOD_PREFIX));
    assertTrue(hasMethod(clazz, AnnotationMemberExtractor.PACKAGE_PRIVATE_BUILDER));
    assertTrue(hasMethod(clazz, AnnotationMemberExtractor.ENABLE_STANDARD_BUILDER));
    assertTrue(hasMethod(clazz, AnnotationMemberExtractor.ENABLE_FULL_BUILDER));
    assertTrue(hasMethod(clazz, AnnotationMemberExtractor.FULL_BUILDER_FIELD_ORDER));
    assertTrue(hasMethod(clazz, AnnotationMemberExtractor.INCLUDE_OUTER_CLASS_NAME));
  }

  private static boolean hasMethod(Class<?> clazz, String methodName) {
    final Method[] declaredMethods = clazz.getDeclaredMethods();
    return Stream.of(declaredMethods).anyMatch(m -> m.getName().equals(methodName));
  }
}
