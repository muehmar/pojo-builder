package io.github.muehmar.pojobuilder.generator.model.settings;

import static java.util.Optional.empty;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.FullBuilderFieldOrder;
import io.github.muehmar.pojobuilder.annotations.OptionalDetection;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.generator.model.ClassAccessLevelModifier;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import java.util.Optional;
import lombok.Value;
import lombok.With;

@Value
@With
@PojoBuilder
public class PojoSettings {
  private static final Name CLASS_NAME_PLACEHOLDER = Name.fromString("{CLASSNAME}");
  public static final Name BUILDER_CLASS_POSTFIX = Name.fromString("Builder");
  PList<OptionalDetection> optionalDetections;
  Optional<Name> builderName;
  Optional<Name> builderSetMethodPrefix;
  ClassAccessLevelModifier builderAccessLevel;
  boolean standardBuilderEnabled;
  boolean fullBuilderEnabled;
  FullBuilderFieldOrder fullBuilderFieldOrder;

  public static PojoSettings defaultSettings() {
    return PojoSettingsBuilder.create()
        .optionalDetections(
            PList.of(OptionalDetection.OPTIONAL_CLASS, OptionalDetection.NULLABLE_ANNOTATION))
        .builderAccessLevel(ClassAccessLevelModifier.PUBLIC)
        .standardBuilderEnabled(true)
        .fullBuilderEnabled(true)
        .fullBuilderFieldOrder(FullBuilderFieldOrder.REQUIRED_FIELDS_FIRST)
        .andAllOptionals()
        .builderName(Optional.of(CLASS_NAME_PLACEHOLDER.append(BUILDER_CLASS_POSTFIX)))
        .builderSetMethodPrefix(empty())
        .build();
  }

  public Name qualifiedBuilderName(Pojo pojo) {
    return pojo.getPackage().qualifiedName(builderName(pojo));
  }

  public Name builderName(Pojo pojo) {
    return getNameOrAppend(builderName, BUILDER_CLASS_POSTFIX, pojo);
  }

  private Name getNameOrAppend(Optional<Name> name, Name postfix, Pojo pojo) {
    return name.map(n -> n.replace(CLASS_NAME_PLACEHOLDER, getClassName(pojo)))
        .orElseGet(() -> getClassName(pojo).append(postfix));
  }

  private Name getClassName(Pojo pojo) {
    return pojo.getName().map(n -> n.replace(".", ""));
  }

  public PojoSettings withBuilderNameOpt(Name builderName) {
    return this.withBuilderName(Optional.of(builderName));
  }

  public PojoSettings withBuilderSetMethodPrefixOpt(Name builderSetMethodPrefix) {
    return this.withBuilderSetMethodPrefix(Optional.of(builderSetMethodPrefix));
  }

  public PojoSettings overrideOptionalDetection(
      Optional<PList<OptionalDetection>> optionalDetections) {
    return optionalDetections.map(this::withOptionalDetections).orElse(this);
  }

  public PojoSettings overrideBuilderName(Optional<Name> builderName) {
    return builderName.map(this::withBuilderNameOpt).orElse(this);
  }

  public PojoSettings overrideBuilderSetMethodPrefix(Optional<Name> builderSetMethodPrefix) {
    return builderSetMethodPrefix
        .map(ignore -> this.withBuilderSetMethodPrefix(builderSetMethodPrefix))
        .orElse(this);
  }

  public PojoSettings overrideBuilderAccessLevel(
      Optional<ClassAccessLevelModifier> classAccessLevelModifier) {
    return classAccessLevelModifier.map(this::withBuilderAccessLevel).orElse(this);
  }
}
