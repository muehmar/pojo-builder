package io.github.muehmar.pojoextension.processor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.muehmar.pojoextension.annotations.SafeBuilder;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class AnnotationMemberExtractorTest {
  @Test
  void SafeBuilderContainsAllMethods() {
    final Class<?> clazz = SafeBuilder.class;

    assertTrue(hasMethod(clazz, AnnotationMemberExtractor.OPTIONAL_DETECTION));
    assertTrue(hasMethod(clazz, AnnotationMemberExtractor.BUILDER_NAME));
    assertTrue(hasMethod(clazz, AnnotationMemberExtractor.BUILDER_SET_METHOD_PREFIX));
  }

  private static boolean hasMethod(Class<?> clazz, String methodName) {
    final Method[] declaredMethods = clazz.getDeclaredMethods();
    return Stream.of(declaredMethods).anyMatch(m -> m.getName().equals(methodName));
  }
}
