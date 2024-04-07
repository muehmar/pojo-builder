package io.github.muehmar.pojobuilder.processor;

import static io.github.muehmar.pojobuilder.generator.model.FactoryMethodBuilder.factoryMethodBuilder;
import static io.github.muehmar.pojobuilder.generator.model.PojoBuilder.pojoBuilder;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.FactoryMethod;
import io.github.muehmar.pojobuilder.generator.model.Generics;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.ClassnameParser;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import io.github.muehmar.pojobuilder.processor.mapper.ArgumentMapper;
import io.github.muehmar.pojobuilder.processor.mapper.PojoFieldMapper;
import io.github.muehmar.pojobuilder.processor.mapper.PojoSettingsMapper;
import io.github.muehmar.pojobuilder.processor.mapper.TypeMirrorMapper;
import io.github.muehmar.pojobuilder.processor.model.DetectionSettings;
import io.github.muehmar.pojobuilder.processor.writer.PojoWriter;
import java.util.Optional;
import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;

public class AnnotatedFactoryMethodProcessor {
  private AnnotatedFactoryMethodProcessor() {}

  public static void processFactoryMethod(
      PojoBuilderProcessor.ElementAndAnnotationPath<ExecutableElement> elementAndPath,
      PojoWriter writer,
      Filer filer) {
    final PojoSettings pojoSettings =
        PojoSettingsMapper.extractSettingsFromAnnotationPath(elementAndPath.getPath());
    final ExecutableElement executableElement = elementAndPath.getElement();

    final QualifiedClassname factoryMethodOwner =
        ClassnameParser.parseThrowing(executableElement.getEnclosingElement().toString());

    final DetectionSettings detectionSettings =
        new DetectionSettings(pojoSettings.getOptionalDetections());

    final Type factoryMethodReturnType = TypeMirrorMapper.map(executableElement.getReturnType());
    final QualifiedClassname pojoClassName =
        ClassnameParser.parseThrowing(executableElement.getReturnType().toString());

    final PackageName pojoPackage = factoryMethodOwner.getPkg();

    final Pojo pojo =
        extractPojoFromFactoryMethod(
            pojoClassName,
            executableElement,
            detectionSettings,
            factoryMethodOwner,
            pojoPackage,
            factoryMethodReturnType);

    writer.writePojo(pojo, pojoSettings, filer);
  }

  private static Pojo extractPojoFromFactoryMethod(
      QualifiedClassname pojoClassName,
      ExecutableElement executableElement,
      DetectionSettings detectionSettings,
      QualifiedClassname factoryMethodOwner,
      PackageName pojoPackage,
      Type returnType) {
    final PList<PojoField> fields =
        PList.fromIter(executableElement.getParameters())
            .map(e -> PojoFieldMapper.mapToPojoField(e, detectionSettings));

    final PList<Argument> factoryMethodArguments =
        PList.fromIter(executableElement.getParameters()).map(ArgumentMapper::toArgument);

    final Generics buildGenerics =
        TypeParameterProcessor.processTypeParameters(executableElement.getTypeParameters());

    final FactoryMethod factoryMethod =
        factoryMethodBuilder()
            .ownerClassname(factoryMethodOwner.getClassname())
            .pkg(pojoPackage)
            .methodName(Name.fromString(executableElement.getSimpleName().toString()))
            .arguments(factoryMethodArguments)
            .exceptions(ExceptionProcessor.process(executableElement))
            .build();

    return pojoBuilder()
        .pojoClassname(pojoClassName)
        .pojoNameWithTypeVariables(returnType.getTypeDeclaration())
        .pkg(pojoPackage)
        .fields(fields)
        .constructors(PList.empty())
        .generics(buildGenerics)
        .fieldBuilders(PList.empty())
        .andAllOptionals()
        .factoryMethod(factoryMethod)
        .buildMethod(Optional.empty())
        .build();
  }
}
