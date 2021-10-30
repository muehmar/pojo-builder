[![Build Status](https://github.com/muehmar/pojo-extension/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/muehmar/pojo-extenstion/actions/workflows/gradle.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/muehmar/pojo-extenstion/blob/master/LICENSE)

# Pojo Extension

Generate useful boilerplate code for immutable pojos. This annotation processor creates an extension class with some
useful features to extend the pojos like the SafeBuilder.

This annotation processor does intentionally not modify the AST of the pojo like Lombok. The features are automatically
added to the pojo by extending the generated extension class.

This annotation processor is designed for simple immutable pojos, whose attributes are either required or optional, but
the processor may also be used for more complex data structures.

Currently the extension class contains the following features:

* SafeBuilder - Special builder class which enforces setting all required attributes
* More to follow...

## Usage

Add the dependency and register it as annotation processor. In gradle this would look like the following:

```
dependencies {
    implementation "io.github.muehmar:pojo-extension:0.2.4"
    annotationProcessor "io.github.muehmar:pojo-extension:0.2.4"
}
```

Annotate a simple pojo class:

```
@PojoExtension
public class Customer extends CustomerExtension {
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

This will create the class `CustomerExtension` which provides the utilities for the `Customer` class. In case of the
SafeBuilder one could now use the static method `Customer.newBuilder()` which is provided by the extension class.

In case one could not extend the extension class, some of the features like the SafeBuilder could be used by delegating
the call of the `newBuilder` method:

```
@PojoExtension
public class Customer {
private final String name;
private final String email;
private final Optional<String> nickname;

    public Customer(String name, String email, String nickname) {
        this.name = name;
        this.email = email;
        this.nickname = Optional.ofNullable(nickname);
    }
    
    // Delegate the creation of the SafeBuilder
    public static Builder0 newBuilder() {
        return CustomerExtension.newBuilder();
    }
    
    // Remaing part omitted...
}

```

## Pojo Requirements

There are some requirements for the pojo depending on the used feature.

### Safe Builder

The safe builder needs to create instances of the pojo. Therefore, a constructor is required which accepts all fields in
the same order of declaration. The arguments for optional fields can either be wrapped into an Optional or allow to pass
`null`, the extension will create a proper constructor call according to the types of the arguments. As the extension
class is generated in the same package, the constructor can be package private:

```
@PojoExtension
public class Customer extends CustomerExtension {
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

## Annotation Parameters

### Optional detection

There are multiple ways to tell the processor which attributes of a pojo are required and which are not. The annotation
has the parameter `optionalDetection` which is an array of `OptionalDetection` and allows customisation of each pojo if
necessary:

| OptionalDetection | Description |
| --- | --- |
| OptionalDetection.OPTIONAL_CLASS | In this case every field in the pojo which is wrapped in an Optional is considered as optional. |
| OptionalDetection.NULLABLE_ANNOTATION | With this option a field in the pojo can be annotated with the `Nullable` annotation to mark it as optional. The `Nullable` annotation is delivered within this package, `javax.annotations.Nullable` (JSR305) is not yet supported.

Both options are active as default.

## SafeBuilder

The 'Safe Builder' is an extended builder pattern which enforces one to create valid instances, i.e. every required
property in a pojo will be set.

This is done by creating a single builder class for each required property, with a single method setting the
corresponding property and returning the next builder for the next property. The `build`
method will only be present after each required property is set.

For example, given the following class:

```
@PojoExtension
public class Customer extends CustomerExtension {
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
  Customer.newBuilder()
    .setName("Dexter")
    .setEmail("dexter@miami-metro.us")
    .andAllOptionals()
    .setNickname("Dex")
    .build();
```

This does not seem to be very different from the normal builder pattern at a first glance but calling `newBuilder()`
will return a class which has only a single method `setName()`, i.e. the compiler enforces one to set the name. The
returned class after setting the name has again one single method `setEmail()`. As the property `email` is the last
required property in this example the returned class for `setEmail()` offers three methods:

* `build()` As all required properties are set at that time, building the instance is allowed here.
* `andOptionals()` Returns the normal builder allowing one to set certain optional properties before creating the
  instance. This method returns just the normal builder populated with all required properties.
* `andAllOptionals()` Enforces one to set all optional properties in the same way as it is done for the required
  properties. The `build()` method will only be available after all optional properties have been set. This method is
  used in the example above, i.e. the compiler enforces one to set the `nickname` property too. This is especially
  useful in case of mapping from another data structure.

Setting all required properties in a class could theoretically also be achieved with a constructor with all required
properties as arguments, but the pattern used here is safer in terms of refactoring, i.e. adding or removing properties,
changing the required properties or changing the order of the properties.

When using `andAllOptionals()` or `andOptinoals()` after all required properties are set, the builder provides
overloaded methods to add the optional properties. The property can be set directly or wrapped in an `Optional`. In the
example above, the builder provides methods with the following signature:

```
  public Builder setNickname(String nickname);
  
  public Builder setNickname(Optional<String> nickname);
```

## Known Issues

* Wildcard types are not yet supported

## Change Log

* 0.2.4 - Fix generated package structure for the extension
* 0.2.3 - Make the extension be extendable by the pojo itself
* 0.2.2 - Support constructors with optional fields wrapped into `java.util.Optional`
* 0.2.1 - Add support for primitives and arrays
* 0.2.0 - Add SafeBuilder to the extension class
* 0.1.0 - Initial release, creates empty extension class
