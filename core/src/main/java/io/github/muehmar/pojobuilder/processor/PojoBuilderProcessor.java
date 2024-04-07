package io.github.muehmar.pojobuilder.processor;

import static io.github.muehmar.pojobuilder.Booleans.not;

import ch.bluecare.commons.data.NonEmptyList;
import ch.bluecare.commons.data.PList;
import com.google.auto.service.AutoService;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.processor.writer.PojoWriter;
import java.util.Optional;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import lombok.Value;

@SupportedAnnotationTypes("*")
@AutoService(Processor.class)
public class PojoBuilderProcessor extends AbstractProcessor {
  private static final int MAX_ANNOTATION_PATH_DEPTH = 50;

  private final PojoWriter pojoWriter;

  public PojoBuilderProcessor(PojoWriter pojoWriter) {
    this.pojoWriter = pojoWriter;
  }

  // Used by the compiler during annotation processing
  public PojoBuilderProcessor() {
    this(PojoWriter.defaultWriter());
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    final PList<? extends Element> annotatedElements =
        PList.fromIter(annotations).flatMap(roundEnv::getElementsAnnotatedWith);

    processClassOrRecord(annotatedElements);
    processStaticMethod(annotatedElements);
    processConstructor(annotatedElements);

    return false;
  }

  private void processClassOrRecord(PList<? extends Element> annotatedElements) {
    annotatedElements
        .filter(this::isClassOrRecord)
        .filter(TypeElement.class::isInstance)
        .map(TypeElement.class::cast)
        .distinct(Object::toString)
        .flatMapOptional(this::findAnnotationPath)
        .forEach(
            elementAndPath ->
                AnnotatedClassProcessor.processClassOrRecord(
                    elementAndPath, pojoWriter, processingEnv.getFiler()));
  }

  private void processStaticMethod(PList<? extends Element> annotatedElements) {
    annotatedElements
        .filter(this::isMethod)
        .filter(ExecutableElement.class::isInstance)
        .map(ExecutableElement.class::cast)
        .filter(this::isStaticMethod)
        .distinct(Object::toString)
        .flatMapOptional(this::findAnnotationPath)
        .forEach(
            elementAndPath ->
                AnnotatedFactoryMethodProcessor.processFactoryMethod(
                    elementAndPath, pojoWriter, processingEnv.getFiler()));
  }

  private void processConstructor(PList<? extends Element> annotatedElements) {
    annotatedElements
        .filter(this::isConstructor)
        .filter(ExecutableElement.class::isInstance)
        .map(ExecutableElement.class::cast)
        .distinct(Object::toString)
        .flatMapOptional(this::findAnnotationPath)
        .forEach(
            elementAndPath ->
                AnnotatedConstructorProcessor.processConstructor(
                    elementAndPath, pojoWriter, processingEnv.getFiler()));
  }

  private boolean isClassOrRecord(Element e) {
    return e.getKind().equals(ElementKind.CLASS) || e.getKind().name().equalsIgnoreCase("Record");
  }

  private boolean isMethod(Element element) {
    return element.getKind().equals(ElementKind.METHOD);
  }

  private boolean isConstructor(Element element) {
    return element.getKind().equals(ElementKind.CONSTRUCTOR);
  }

  private boolean isStaticMethod(ExecutableElement executableElement) {
    return executableElement.getModifiers().contains(Modifier.STATIC);
  }

  private <T extends Element> Optional<ElementAndAnnotationPath<T>> findAnnotationPath(T element) {
    return NonEmptyList.fromIter(findAnnotationPath(element, PList.empty()))
        .map(path -> new ElementAndAnnotationPath<>(element, path));
  }

  private <T extends Element> PList<AnnotationMirror> findAnnotationPath(
      T currentElement, PList<AnnotationMirror> currentPath) {
    if (currentPath.size() >= MAX_ANNOTATION_PATH_DEPTH) {
      return PList.empty();
    }

    final PList<AnnotationMirror> annotationMirrors =
        PList.fromIter(currentElement.getAnnotationMirrors()).map(a -> a);
    final Optional<AnnotationMirror> safeBuilder =
        annotationMirrors.find(
            a ->
                a.getAnnotationType()
                    .asElement()
                    .asType()
                    .toString()
                    .equals(PojoBuilder.class.getName()));

    return safeBuilder
        .map(currentPath::cons)
        .orElseGet(
            () ->
                annotationMirrors
                    .filter(a -> not(currentPath.exists(a::equals)))
                    .map(
                        a ->
                            findAnnotationPath(
                                a.getAnnotationType().asElement(), currentPath.cons(a)))
                    .find(PList::nonEmpty)
                    .orElse(PList.empty()));
  }

  @Value
  public static class ElementAndAnnotationPath<T extends Element> {
    T element;
    NonEmptyList<AnnotationMirror> path;
  }
}
