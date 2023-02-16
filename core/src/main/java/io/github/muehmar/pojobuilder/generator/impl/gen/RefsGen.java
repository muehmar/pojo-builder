package io.github.muehmar.pojobuilder.generator.impl.gen;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.FieldBuilderMethod;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Type;

public class RefsGen {
  private RefsGen() {}

  public static Generator<PojoField, PojoSettings> fieldRefs() {
    return (f, s, w) -> addRefs(w, f.getType().getImports());
  }

  public static Generator<Pojo, PojoSettings> genericRefs() {
    return (pojo, s, w) -> addRefs(w, pojo.getGenericImports());
  }

  public static Generator<FieldBuilderMethod, PojoSettings> fieldBuilderMethodRefs() {
    return (field, s, w) -> {
      final PList<Name> argumentImports =
          field.getArguments().map(Argument::getType).flatMap(Type::getImports);
      return addRefs(w, argumentImports);
    };
  }

  private static Writer addRefs(Writer writer, PList<Name> imports) {
    return imports.map(Name::asString).foldLeft(writer, Writer::ref);
  }
}
