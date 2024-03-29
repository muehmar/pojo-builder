package io.github.muehmar.pojobuilder.generator.model;

import java.util.function.UnaryOperator;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Name {
  private final String value;

  private Name(String val) {
    if (val == null || val.trim().isEmpty()) {
      throw new IllegalArgumentException("A name must not be null or empty");
    }

    this.value = val;
  }

  public static Name fromString(String val) {
    return new Name(val);
  }

  public static Name concat(Name n1, Name n2) {
    return n1.append(n2);
  }

  public String asString() {
    return value;
  }

  public Name map(UnaryOperator<String> f) {
    return Name.fromString(f.apply(value));
  }

  public Name append(String append) {
    return new Name(value + append);
  }

  public Name append(Name append) {
    return new Name(value + append.value);
  }

  public Name prefix(String prefix) {
    return new Name(prefix + value);
  }

  public boolean equalsIgnoreCase(Name other) {
    return value.equalsIgnoreCase(other.value);
  }

  public Name toPascalCase() {
    return map(n -> n.substring(0, 1).toUpperCase() + n.substring(1));
  }

  public Name startLowerCase() {
    return map(n -> n.substring(0, 1).toLowerCase() + n.substring(1));
  }

  public Name startUpperCase() {
    return map(n -> n.substring(0, 1).toUpperCase() + n.substring(1));
  }

  public Name javaBeansName() {
    if (value.length() >= 2 && value.substring(1, 2).toUpperCase().equals(value.substring(1, 2))) {
      return this;
    } else {
      return toPascalCase();
    }
  }

  public boolean startsWith(String str) {
    return value.startsWith(str);
  }

  public Name replace(Name oldName, Name newName) {
    return map(s -> s.replace(oldName.asString(), newName.asString()));
  }

  public int length() {
    return value.length();
  }

  @Override
  public String toString() {
    return asString();
  }
}
