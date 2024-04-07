package io.github.muehmar.pojobuilder.processor;

import static io.github.muehmar.pojobuilder.Booleans.not;
import static io.github.muehmar.pojobuilder.generator.model.PojoBuilder.pojoBuilder;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.Ignore;
import io.github.muehmar.pojobuilder.generator.model.BuildMethod;
import io.github.muehmar.pojobuilder.generator.model.Constructor;
import io.github.muehmar.pojobuilder.generator.model.FieldBuilder;
import io.github.muehmar.pojobuilder.generator.model.Generics;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.ClassnameParser;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import io.github.muehmar.pojobuilder.processor.mapper.PojoFieldMapper;
import io.github.muehmar.pojobuilder.processor.mapper.PojoSettingsMapper;
import io.github.muehmar.pojobuilder.processor.model.DetectionSettings;
import io.github.muehmar.pojobuilder.processor.writer.PojoWriter;
import java.util.Optional;
import java.util.Set;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class AnnotatedClassProcessor {
  private AnnotatedClassProcessor() {}

  public static void processClassOrRecord(
      PojoBuilderProcessor.ElementAndAnnotationPath<TypeElement> elementAndPath,
      PojoWriter writer,
      Filer filer) {
    final PojoSettings pojoSettings =
        PojoSettingsMapper.extractSettingsFromAnnotationPath(elementAndPath.getPath());
    final TypeElement classElement = elementAndPath.getElement();
    final String fullClassName = classElement.toString();

    final QualifiedClassname pojoClassname = ClassnameParser.parseThrowing(fullClassName);

    final Pojo pojo = extractPojo(classElement, pojoSettings, pojoClassname);

    writer.writePojo(pojo, pojoSettings, filer);
  }

  private static Pojo extractPojo(
      TypeElement element, PojoSettings settings, QualifiedClassname pojoClassname) {
    final DetectionSettings detectionSettings =
        new DetectionSettings(settings.getOptionalDetections());

    final PList<Constructor> constructors = ConstructorProcessor.process(element);
    final Generics generics =
        TypeParameterProcessor.processTypeParameters(element.getTypeParameters());
    final PList<FieldBuilder> fieldBuilders = FieldBuilderProcessor.process(element);
    final Optional<BuildMethod> buildMethod = BuildMethodProcessor.process(element);

    final PList<PojoField> fields =
        PList.fromIter(element.getEnclosedElements())
            .filter(e -> e.getKind().equals(ElementKind.FIELD))
            .filter(AnnotatedClassProcessor::isNonConstantField)
            .filter(AnnotatedClassProcessor::isNotIgnoredField)
            .map(e -> PojoFieldMapper.mapToPojoField(e, detectionSettings));

    return pojoBuilder()
        .pojoClassname(pojoClassname)
        .pojoNameWithTypeVariables(
            pojoClassname.getName().append(generics.getTypeVariablesFormatted()))
        .pkg(pojoClassname.getPkg())
        .fields(fields)
        .constructors(constructors)
        .generics(generics)
        .fieldBuilders(fieldBuilders)
        .andAllOptionals()
        .factoryMethod(Optional.empty())
        .buildMethod(buildMethod)
        .build();
  }

  private static boolean isNonConstantField(Element element) {
    final Set<Modifier> modifiers = element.getModifiers();
    return !modifiers.contains(Modifier.STATIC);
  }

  private static boolean isNotIgnoredField(Element element) {
    final Optional<Ignore> annotation = Optional.ofNullable(element.getAnnotation(Ignore.class));
    return not(annotation.isPresent());
  }
}
