package io.github.muehmar.pojobuilder.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.CLASS)
public @interface PojoBuilder {
  /** Defines how optional fields in a pojo are detected. */
  OptionalDetection[] optionalDetection() default {
    OptionalDetection.OPTIONAL_CLASS, OptionalDetection.NULLABLE_ANNOTATION
  };

  /** Defines how a constructor is selected and used by the builder to instantiate a pojo. */
  ConstructorMatching constructorMatching() default ConstructorMatching.TYPE;

  /**
   * Override the default name which is used for the discrete builder class. `{CLASSNAME}` gets
   * replaced by the classname of the annotated class.
   */
  String builderName() default "{CLASSNAME}Builder";

  /** Prefix which is used for the setter methods of the builder. */
  String builderSetMethodPrefix() default "";

  /** Generates a package-private builder which is only accessible from within the same package. */
  boolean packagePrivateBuilder() default false;

  /** Enables the generation of the standard builder pattern. */
  boolean enableStandardBuilder() default true;

  /** Enables the generation of the full builder pattern, where every field must be set. */
  boolean enableFullBuilder() default true;

  /** Defines the order of fields used in the full builder. */
  FullBuilderFieldOrder fullBuilderFieldOrder() default FullBuilderFieldOrder.REQUIRED_FIELDS_FIRST;

  /**
   * Use outer class names for inner classes to create the builder name. If disabled, only the inner
   * class name is used as name, i.e. it behaves the same as a top level class.
   */
  boolean includeOuterClassName() default true;
}
