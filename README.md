[![Build Status](https://github.com/muehmar/pojo-builder/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/muehmar/pojo-builder/blob/master/.github/workflows/gradle.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/muehmar/pojo-builder/blob/master/LICENSE)

# Pojo Builder

Generates advanced builders for your immutable data classes or Java 16 records.

The processor distinguishes between required and optional properties/fields in a class or record. The generated builder
ensures during compile time, that all required properties are set, so you will no longer forgot to set a
property which is needed. Also in case of refactoring, adding new required properties will fail the build
for every usage of the builder until the new property is set for every object creation. This can also be achieved
with the optional properties, i.e. one can force the initialization of all optional properties.

## Usage

### Dependency

Add the `pojo-builder-annotations` module as compile-time dependency and register the `pojo-builder` module as
annotation processor. In gradle this would look like the following:

```
dependencies {
    compileOnly "io.github.muehmar:pojo-builder-annotations:1.5.0"
    annotationProcessor "io.github.muehmar:pojo-builder:1.5.0"
}
```

### Class / Record annotation

The `@PojoBuilder` annotation is a class level annotation:

```
@PojoBuilder
public class Customer {
private final String name;
private final String email;
private final Optional<String> nickname;

    public Customer(String name, String email, String nickname) {
        this.name = name;
        this.email = email;
        this.nickname = Optional.ofNullable(nickname);
    }
    
    // Remaing part omitted...
}
```

The processor will create the builder `CustomerBuilder`. The name is configurable, see the configuration section.

The annotation works also for with Java records:

```
@PojoBuilder
public records Customer(
    String name, 
    String email, 
    Optional<String> nickname
  ) {

}
```

### Factory method annotation

In case a class is not part of the source code (e.g. library or JDK) or a factory method contains a lot of arguments,
one could also annotate a factory method:

```
public class Factories {

  @PojoBuilder
  public static File file(String pathname) {
    return new File(pathname);
  }
}
```

The generator will create a builder with arguments in the same order of declaration and the argument name.

Currently, the generator does not support static methods throwing checked exceptions.

## Features

### Compile-Time safety

The builder is implemented by creating a single builder class for each property, with a single method setting
the corresponding property and returning the next builder for the next property. The `build`
method will only be present after each required property is set.

For example, given the following class:

```
@PojoBuilder
public class Customer {
private final String name;
private final String email;
private final Optional<String> nickname;

    public Customer(String name, String email, Optional<String> nickname) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
    }
    
    // Remaing part omitted...
}

```

will lead to a builder which can be used like the following:

```
  CustomerBuilder.create()
    .name("Dexter")
    .email("dexter@miami-metro.us")
    .andAllOptionals()
    .nickname("Dex")
    .build();
```

This does not seem to be very different from the normal builder pattern at a first glance but calling `create()`
will return a class which has only a single method `name()`, i.e. the compiler enforces one to set the name. The
returned class after setting the name has again one single method `email()`. As the property `email` is the last
required property in this example the returned class for `email()` offers three methods:

* `build()` As all required properties are set at that time, building the instance is allowed here.
* `andOptionals()` Returns the well-known simple builder allowing one to set certain optional properties before creating
  the instance. The builder is populated with all required properties but without the possibility to change or delete
  them.
* `andAllOptionals()` Enforces one to set all optional properties in the same way as it is done for the required
  properties. The `build()` method will only be available after all optional properties have been set. This method is
  used in the example above, i.e. the compiler enforces one to set the `nickname` property too. This is especially
  useful in case of mapping from another data structure.

Setting all required properties in a class could theoretically also be achieved with a constructor with all required
properties as arguments, but the pattern used here is safer in terms of refactoring, i.e. adding or removing properties,
changing the required properties or changing the order of the properties.

When using `andAllOptionals()` or `andOptionals()` after all required properties are set, the builder provides
overloaded methods to add the optional properties. The property can be set directly or wrapped in an `Optional`. In the
example above, the builder provides methods with the following signature:

```
  public Builder nickname(String nickname);
  
  public Builder nickname(Optional<String> nickname);
```

#### Prefix for the setter method

You could configure a prefix with `builderSetMethodPrefix` for the setter methods like `set` which is used for the
generation
(see [Annotation Parameters](#annotation-parameters)):

```
  CustomerBuilder.create()
    .setName("Dexter")
    .setEmail("dexter@miami-metro.us")
    .andAllOptionals()
    .setNickname("Dex")
    .build();
```

or alternatively using the `customerBuilder()` method in `CustomerBuilder` which might be statically imported:

```
  customerBuilder()
    .setName("Dexter")
    .setEmail("dexter@miami-metro.us")
    .andAllOptionals()
    .setNickname("Dex")
    .build();
```

The first character of the field name is automatically converted to uppercase if a prefix is used.

### Full builder

The generator creates additionally to the described standard builder also a full builder, i.e. a builder which enforces
to set every property of a class. The generated builder class provides static factory methods (prefix with `full`) to
create a full builder, with the `Customer` class this would look like:

```
  fullCustomerBuilder()
    .setName("Dexter")
    .setEmail("dexter@miami-metro.us")
    .setNickname("Dex")
    .build();
```

There is no `andAllOptionals` method call after the last required field `email`.

There are two options to choose the order of the fields used by the builder:

* Declaration order: In this case, the builder uses the properties in the order they are declared in the class
* Required fields first: This is the same order used in the standard builder, where required fields are used first.

### Custom methods for fields in PojoBuilder

It is possible define custom methods for the PojoBuilder for a specific field which can be used to populate the
corresponding field when using the builder. One could define one or more methods which return an instance of the
corresponding field, where the methods must be static and at least package private:

```
@FieldBuilder(fieldName = "name")
static String fromObject(Object o) {
  return o.toString();
}
```

The builder now contains also a method `fromObject` for populating the `name` field which accepts an `Object`.

If more than one custom method is defined for a field, all methods will be present when the corresponding field must be
populated. One could also annotate a static class with `@FieldBuilder` to group all custom methods for one field:

```
@FieldBuilder(fieldName = "name")
static class FieldBuilder {
  private FieldBuilder(){}
  
  static String fromObject(Object o) {
    return o.toString();
  } 
  
  static String unknown() {
    return "unknown";
  }
}
```

This enables one to create convenience methods to reduce code and/or make the call of the builder more readable.

#### Disable default methods in builder

One could disable the default methods which are generated by the processor in the builders when some custom methods are
defined. To achieve this, set `disableDefaultMethods` to true:

```
@FieldBuilder(fieldName = "name", disableDefaultMethods = true)
static class FieldBuilder {
  private FieldBuilder(){}
  
  static String fromObject(Object o) {
    return o.toString();
  } 
  
  static String unknown() {
    return "unknown";
  }
}
```

The method `setName` will now not be available in the builder for the name property, only the custom
methods `fromObject` and `unknown`. Note that this flag must be set consistently in all `@FieldBuilder` annotations for
the same field, therefore it is recommended to use a static class if you have more than one custom method for the same
field.

#### Generic

To create a custom method for a generic field of the class, the method needs to be declared with the same type parameter
name as the type parameter. For example if your class has a type parameter `T` and a field is a `List<T>` you have to
declare the custom method like following:

```
@FieldBuilder(fieldName = "items")
static <T> List<T> singleItem(T item) {
  return Collections.singletonList(item);
}
```

Other generic methods are not yet supported (it will lead currently to a compile error when used).

### Custom build method

A custom build method can be defined, which may return another type than the actual type of the class. A custom build
method maps the actual build instance to another type and is called by the builder automatically. The following example
returns a String instead of the `Customer` instance:

```
@PojoBuilder
public class Customer {
    ...
    
    @BuildMethod
    String customBuildMethod(Customer customer) {
        return customer.toString();
    }
}
```

```
final String customerString = CustomerBuilder.create()
                                .foo(1)
                                .bar("Test")
                                .build();
```

A custom build method must be static and can be package-private.

## Requirements

A data class must provide a constructor with all fields as arguments. The builder is created in the same package as the
class, therefore the constructor can be package private if needed.

A record (Java 16) provides already a constructor and therefore satisfies all requirements
automatically.

### Constructor

The constructor must accept all fields as arguments and in the same order of declaration. Depending
on `constructorMatching`, either the types or types and names must match (
see [Parameter constructoMatching](#parameter-constructormatching). The types of the optional fields can either be
the actual type which may be nullable in case of absence or wrapped into a `java.util.Optional`. The annotation
processor is smart enough to detect which case is used, there can also be a mix for the optional fields in case you
really need it.

```
@PojoBuilder
public class Customer {
private final String name;
private final String email;
private final Optional<String> nickname;

    // Package private constructor, nickname is String instead of Optional and wrapped with Optional.ofNullable()
    Customer(String name, String email, String nickname) {
        this.name = name;
        this.email = email;
        this.nickname = Optional.ofNullable(nickname);
    }
    
    // Remaing part omitted...
}
```

In case there is a field which is instantiated in the constructor and no argument is present, you can use the `@Ignore`
annotation for this field:

```
@PojoBuilder
public class IgnoreFieldClass {
  private final String id;
  private final String name;
  @Ignore private final String deviated;

  public IgnoreFieldClass(String id, String name) {
    this.id = id;
    this.name = name;
    this.deviated = String.format("%s-%s", id, name);
  }
}
```

## Annotations

The following annotations exists:

* `@PojoBuilder` Creates the builder class
* `@FieldBuilder` Used to mark custom methods used in the Builder. `fieldName` is required and defines the field for
  which the custom method should be used.
* `@Ignore` Used to mark a field which should get ignored by the processor. Used particularly for fields which are
  instantiated withing the constructor and not present as argument in the constructor.
* `@Nullable` Used to denote optional fields which can be nullable in case of absence. Can also be used in factory
  methods.
* `@BuildMethod` Used to mark a method which is used to map the actual instance to another object used by the generated
  build method.

### Annotation Parameters

The `@PojoBuilder` annotation contains the following parameters.

| Parameter                | Default value                         | Description                                                                                                                                                                                          |
|--------------------------|---------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `optionalDetection`      | [OPTIONAL_CLASS, NULLABLE_ANNOTATION] | Defines how optional fields in data class are detected by the processor. See the next sections for details.                                                                                          |
| `constructorMatching`    | TYPE                                  | Defines how a constructor must match the declared fields to be used to create instances. See next sections for details.                                                                              |
| `builderName`            | "{CLASSNAME}Builder"                  | Allows to override the default name of the discrete builder. `{CLASSNAME}` gets replaced by the name of the data class. Ignored if `discreteBuilder` is false.                                       |
| `builderSetMethodPrefix` | ""                                    | Prefix which is used for the setter methods of the builder.                                                                                                                                          |
| `packagePrivateBuilder`  | false                                 | Generates a package-private builder which is only accessible from within the same package.                                                                                                           |
| `enableStandardBuilder`  | true                                  | Allows to disable the generation of the standard builder.                                                                                                                                            |
| `enableFullBuilder`      | true                                  | Allows to disable the generation of the full builder.                                                                                                                                                |
| `fullBuilderFieldOrder`  | REQUIRED_FIELDS_FIRST                 | Defines the order of the fields in the full builder. See next sections for details.                                                                                                                  |
| `includeOuterClassName`  | true                                  | Uses the outer class name to create the builder name. E.g. a class `OuterClass.InnerClass` would create a builder `OuterClassInnerClassBuilder`. If disabled this would only be `InnerClassBuilder`. |

#### Parameter `optionalDetection`

There are multiple ways to tell the processor which attributes of a pojo are required and which are not. The annotation
has the parameter `optionalDetection` which is an array of `OptionalDetection` and allows customisation of each pojo if
necessary:

| OptionalDetection                     | Description                                                                                                                                                                                                                     |
|---------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| OptionalDetection.OPTIONAL_CLASS      | In this case every field in the pojo which is wrapped in an Optional is considered as optional.                                                                                                                                 |
| OptionalDetection.NULLABLE_ANNOTATION | With this option a field in the pojo can be annotated with the `Nullable` annotation to mark it as optional. The `Nullable` annotation is delivered within this package, `javax.annotation.Nullable` (JSR305) is supported too. |
| OptionalDetection.NONE                | All fields are treated as required. This setting gets ignored in case it is used in combination with one of the others.                                                                                                         |

Both options are active as default.

#### Parameter `constructorMatching`

Defines how the arguments of a constructor must match the declared fields. Currently, the arguments need to have the
same order as the order of the declared fields.

As mentioned, the actual types of the arguments of optional fields, may be wrapped into `java.util.Optional`.

| ConstructorMatching               | Description                                                                                        |
|-----------------------------------|----------------------------------------------------------------------------------------------------|
| ConstructorMatching.TYPE          | Only the types of the arguments of the constructor need to match the types of the declared fields. |
| ConstructorMatching.TYPE_AND_NAME | The types and names of the arguments of the constructor need to match the declared fields.         |

With version 1.x, the default ìs `TYPE` but may change with a next major release.

#### Parameter `fullBuilderFieldOrder`

The following orders exist:

| FullBuilderFieldOrder                       | Description                                                                                                                        |
|---------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| FullBuilderFieldOrder.REQUIRED_FIELDS_FIRST | Required fields are used before the optional fields. This corresponds to the order of the standard builder and is used as default. |
| FullBuilderFieldOrder.DECLARATION_ORDER     | Fields are used in the same order they are declared in the pojo                                                                    |

## Custom Annotation / Meta Annotation

The `@PojoBuilder` annotation can be used as meta annotation to create your own annotation with predefined behaviour.

For example if you want to treat every field in an annotated class as required, you could create your own
annotation `@AllRequiredPojoBuilder` which is annotated with `@PojoBuilder` with disabled optional detection.

```
@PojoBuilder(optionalDetection = OptionalDetection.NONE)
public @interface AllRequiredPojoBuilder {}
```

If one wants to create a custom annotation, with default values but allow overriding for certain classes, simply create
the corresponding method in the annotation with the same name providing the default value, i.e.:

```
@PojoBuilder
public @interface AllRequiredPojoBuilder {

  OptionalDetection[] optionalDetection() default {OptionalDetection.NONE};
  
}
```

## Known Issues

* Fields in superclasses of data classes are currently ignored.

## Change Log

* 1.6.0
    * Improve compile time safety by adding the possibility to check the argument names for used constructor (
      issue `#32`)
    * Improve error message in case no suitable constructor is found (issue `#29`)
    * Add possibility to annotate the constructor of a pojo (issue `#26`)
* 1.5.0
    * Support `javax.annotation.Nullable` (issue `#27`)
    * Support checked exceptions for factory methods (issue `#16`)
* 1.4.2
    * Fix arguments order for field builders (issue `#22`)
    * Remove redundant explicit `Object` type upper bound of generic parameters (issue `#9`)
* 1.4.1 - Fix missing type variables in build method (issue `#17`)
* 1.4.0 - Support annotation of factory methods (issue `#4`)
* 1.3.0 - Add annotation element to use only the inner class name for the builder (issue `#12`)
* 1.2.0 - Add full builder (issue `#2`)
* 1.1.0 - Add second factory method with the pojo name for static imports (issue `#7`)
* 1.0.0 - Fork and Release of PojoBuilder
    * Remove the pojo extension generation
* 0.15.1 - Fix import for nested classes (issue `#15`)
* 0.15.0
    * Add support for wildcards (issue `#13`)
* 0.14.0
    * Add custom build method (issue `#6`)
    * Remove obsolete base class settings (issue `#8`)
    * Add option for package-private builder class (issue `#2`)
* 0.13.0 - Add `@Ignore` annotation
* 0.12.0
    * Drop support for `equals/hashCode` and `toString` method
    * Allow to disable the default methods in PojoBuilder when defining custom methods
    * Support varargs in custom PojoBuilder methods
* 0.11.0 - Add `@FieldBuilder` annotation to create custom methods for the PojoBuilder
* 0.10.1 - Make the base class and the extension interface package private
* 0.10.0
    * Add configurable prefix for the builder set methods
    * Generate convenience getters for optional fields
* 0.9.0
    * Support Java 16 records
    * Use an interface instead of an abstract class
* 0.8.0
    * Support generic data classes
    * Support newer Java versions
* 0.7.2 - Fix type conversion for annotated getter method for optional fields
* 0.7.1 - Fix possible stackoverflow caused by circular annotation paths
* 0.7.0
    * PojoBuilder can be created as discrete class
    * Improve meta annotation processing
    * Classname of the data class can be used to create custom builder or extension class names
* 0.6.0
    * Fix `toString` method
    * Add possibility to disable specific features
    * Add separate module for annotations to be used as compile time dependency
    * Make static methods private if data class inherits extension class
* 0.5.0
    * Add `mapXX` methods
    * Fix import for generic fields
* 0.4.0
    * Add `toString` method
    * Support inner classes
    * Support custom extension name
    * Support meta annotation
    * Support arbitrary getter names with `@Getter` annotation
* 0.3.1 - Ignore constants in data classes
* 0.3.0 - Add `equals`, `hashCode` and `withXX` methods
* 0.2.5 - Remove newline character from writer output
* 0.2.4 - Fix generated package structure for the extension
* 0.2.3 - Make the extension be extendable by the pojo itself
* 0.2.2 - Support constructors with optional fields wrapped into `java.util.Optional`
* 0.2.1 - Add support for primitives and arrays
* 0.2.0 - Add PojoBuilder to the extension class
* 0.1.0 - Initial release, creates empty extension class
