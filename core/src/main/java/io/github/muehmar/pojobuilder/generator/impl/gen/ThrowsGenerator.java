package io.github.muehmar.pojobuilder.generator.impl.gen;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Classname;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;

public class ThrowsGenerator {
  private ThrowsGenerator() {}

  public static Generator<PList<QualifiedClassname>, PojoSettings> throwsGenerator() {
    return Generator.<PList<QualifiedClassname>, PojoSettings>emptyGen()
        .append(
            (exceptions, s, w) ->
                w.println(
                    "%s",
                    exceptions
                        .map(QualifiedClassname::getClassname)
                        .map(Classname::asString)
                        .mkString(", ")))
        .append((exceptions, s, w) -> w.refs(exceptions.map(QualifiedClassname::getImport)));
  }
}
