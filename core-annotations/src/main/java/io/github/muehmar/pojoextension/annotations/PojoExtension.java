package io.github.muehmar.pojoextension.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Creates an extension class for the annotated class. */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface PojoExtension {

  /** Defines how optional fields in a pojo are detected. */
  OptionalDetection[] optionalDetection() default {
    OptionalDetection.OPTIONAL_CLASS, OptionalDetection.NULLABLE_ANNOTATION
  };

  /** Override the default name which is created for the extension. */
  String extensionName() default "";

  /** Enables or disables the generation of the SafeBuilder. */
  boolean enableSafeBuilder() default true;

  /** Enables or disables the generation of the equals and hashCode methods. */
  boolean enableEqualsAndHashCode() default true;

  /** Enables or disables the generation of the toString method. */
  boolean enableToString() default true;

  /** Enables or disables the generation of the with methods. */
  boolean enableWithers() default true;

  /** Enables or disables the generation of the map methods. */
  boolean enableMappers() default true;
}