package io.github.muehmar.pojoextension.generator.impl.gen.tostring;

import static io.github.muehmar.pojoextension.generator.data.OptionalFieldRelation.SAME_TYPE;
import static io.github.muehmar.pojoextension.generator.impl.JavaModifier.STATIC;
import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_UTIL_ARRAYS;

import ch.bluecare.commons.data.PList;
import ch.bluecare.commons.data.Pair;
import io.github.muehmar.pojoextension.Mapper;
import io.github.muehmar.pojoextension.generator.Generator;
import io.github.muehmar.pojoextension.generator.data.FieldGetter;
import io.github.muehmar.pojoextension.generator.data.Pojo;
import io.github.muehmar.pojoextension.generator.data.Type;
import io.github.muehmar.pojoextension.generator.data.settings.PojoSettings;
import io.github.muehmar.pojoextension.generator.impl.JavaModifiers;
import io.github.muehmar.pojoextension.generator.impl.gen.MethodGen;
import io.github.muehmar.pojoextension.generator.impl.gen.RefsGen;
import java.util.function.Function;

public class ToStringGens {
  private ToStringGens() {}

  public static Generator<Pojo, PojoSettings> genToStringMethod() {
    final Generator<Pojo, PojoSettings> method =
        MethodGen.<Pojo, PojoSettings>modifiers()
            .noGenericTypes()
            .returnType("String")
            .methodName("genToString")
            .noArguments()
            .content(genToStringContent());
    return method.filter((p, s) -> s.getToStringAbility().isEnabled());
  }

  public static Generator<Pojo, PojoSettings> staticToStringMethod() {
    final Function<Pojo, String> argument =
        p -> String.format("%s self", p.getNameWithTypeVariables());
    return MethodGen.<Pojo, PojoSettings>modifiers(
            (p, s) -> JavaModifiers.of(s.getStaticMethodAccessModifier(), STATIC))
        .genericTypes(Pojo::getGenericTypeDeclarations)
        .returnType("String")
        .methodName("toString")
        .singleArgument(argument)
        .content(genToStringContent())
        .append(RefsGen.genericRefs())
        .filter((p, s) -> s.getToStringAbility().isEnabled());
  }

  private static Generator<Pojo, PojoSettings> genToStringContent() {
    final Generator<Pojo, PojoSettings> content =
        Generator.of(
            (p, s, w) -> {
              final PList<FieldGetter> getters = p.getAllGettersOrThrow();
              return getters
                  .headOption()
                  .map(
                      head ->
                          getters
                              .tail()
                              .foldLeft(
                                  toStringFieldLine("").generate(head, s, w),
                                  (wr, g) -> toStringFieldLine(", ").generate(g, s, wr)))
                  .orElse(w);
            });
    return Generator.<Pojo, PojoSettings>of((p, s, w) -> w.println("return \"%s{\"", p.getName()))
        .append(content)
        .append(w -> w.tab(2).println("+ '}';"));
  }

  private static Generator<FieldGetter, PojoSettings> toStringFieldLine(String separator) {
    return (fg, s, w) -> {
      final Pair<String, String> wrapper = getWrapper(fg);
      final boolean isArray = fg.getField().getType().isArray();
      final String format =
          isArray ? "+ \"%s%s=%s\" + Arrays.toString(%s())%s" : "+ \"%s%s=%s\" + %s()%s";

      return Mapper.initial(w)
          .mapConditionally(isArray, wr -> wr.ref(JAVA_UTIL_ARRAYS))
          .apply()
          .tab(2)
          .println(
              format,
              separator,
              fg.getField().getName(),
              wrapper.first(),
              fg.getGetter().getName(),
              wrapper.second());
    };
  }

  private static Pair<String, String> getWrapper(FieldGetter fg) {
    return fg.getField().getType().equals(Type.string()) && fg.getRelation().equals(SAME_TYPE)
        ? Pair.of("'", " + '\\''")
        : Pair.of("", "");
  }
}
