package io.github.muehmar.pojoextension.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@PojoExtension(enableToString = false, enableEqualsAndHashCode = false)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface RecordExtension {
  /** Defines how optional fields in a pojo are detected. */
  OptionalDetection[] optionalDetection() default {OptionalDetection.OPTIONAL_CLASS};

  /**
   * Override the default name which is used for the discrete builder class. `{CLASSNAME}` gets by
   * the classname of the annotated class.
   */
  String builderName() default "{CLASSNAME}Builder";

  /**
   * Override the default name which is used for the extension interface. `{CLASSNAME}` gets
   * replaced by the classname of the annotated class.
   */
  String extensionName() default "{CLASSNAME}Extension";

  /**
   * Creates a discrete builder class. If set to false, the builder is part of the extension class.
   */
  boolean discreteBuilder() default true;

  /** Enables or disables the generation of the with methods. */
  boolean enableWithers() default true;

  /** Enables or disables the generation of the map methods. */
  boolean enableMappers() default true;
}
