package io.github.muehmar.pojobuilder.processor;

import static io.github.muehmar.pojobuilder.generator.model.PojoBuilder.pojoBuilder;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.exception.PojoBuilderException;
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
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

public class AnnotatedConstructorProcessor {
  private AnnotatedConstructorProcessor() {}

  public static void processConstructor(
      PojoBuilderProcessor.ElementAndAnnotationPath<ExecutableElement> elementAndPath,
      PojoWriter pojoWriter,
      Filer filer) {
    final PojoSettings pojoSettings =
        PojoSettingsMapper.extractSettingsFromAnnotationPath(elementAndPath.getPath());
    final ExecutableElement constructorElement = elementAndPath.getElement();

    final Element maybeClassElement = constructorElement.getEnclosingElement();
    if (!(maybeClassElement instanceof TypeElement)) {
      throw new PojoBuilderException(
          String.format(
              "Cannot process constructor %s because enclosing element is not a class or record!",
              constructorElement.getSimpleName()));
    }
    final TypeElement classElement = (TypeElement) maybeClassElement;

    final Pojo pojo = extractPojo(pojoSettings, classElement, constructorElement);

    pojoWriter.writePojo(pojo, pojoSettings, filer);
  }

  private static Pojo extractPojo(
      PojoSettings pojoSettings, TypeElement classElement, ExecutableElement constructorElement) {
    final DetectionSettings detectionSettings =
        new DetectionSettings(pojoSettings.getOptionalDetections());

    final String fullClassName = classElement.toString();
    final QualifiedClassname pojoClassname = ClassnameParser.parseThrowing(fullClassName);

    final Constructor constructor =
        ConstructorProcessor.processConstructor(pojoClassname.getSimpleName(), constructorElement);
    final Generics generics =
        TypeParameterProcessor.processTypeParameters(classElement.getTypeParameters());
    final PList<FieldBuilder> fieldBuilders = FieldBuilderProcessor.process(classElement);
    final Optional<BuildMethod> buildMethod = BuildMethodProcessor.process(classElement);

    final PList<PojoField> fields =
        PList.fromIter(constructorElement.getParameters())
            .map(e -> PojoFieldMapper.mapToPojoField(e, detectionSettings));

    return pojoBuilder()
        .pojoClassname(pojoClassname)
        .pojoNameWithTypeVariables(
            pojoClassname.getName().append(generics.getTypeVariablesFormatted()))
        .pkg(pojoClassname.getPkg())
        .fields(fields)
        .constructors(PList.single(constructor))
        .generics(generics)
        .fieldBuilders(fieldBuilders)
        .andAllOptionals()
        .factoryMethod(Optional.empty())
        .buildMethod(buildMethod)
        .build();
  }
}
