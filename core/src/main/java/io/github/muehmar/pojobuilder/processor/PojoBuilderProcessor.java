package io.github.muehmar.pojobuilder.processor;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.Booleans.not;
import static io.github.muehmar.pojobuilder.generator.model.FactoryMethodBuilder.factoryMethodBuilder;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.OPTIONAL;
import static io.github.muehmar.pojobuilder.generator.model.Necessity.REQUIRED;
import static io.github.muehmar.pojobuilder.generator.model.PojoBuilder.pojoBuilder;
import static io.github.muehmar.pojobuilder.processor.AnnotationMemberExtractor.getBuilderName;
import static io.github.muehmar.pojobuilder.processor.AnnotationMemberExtractor.getBuilderSetMethodPrefix;
import static io.github.muehmar.pojobuilder.processor.AnnotationMemberExtractor.getEnableFullBuilder;
import static io.github.muehmar.pojobuilder.processor.AnnotationMemberExtractor.getEnableStandardBuilder;
import static io.github.muehmar.pojobuilder.processor.AnnotationMemberExtractor.getFullBuilderFieldOrder;
import static io.github.muehmar.pojobuilder.processor.AnnotationMemberExtractor.getIncludeOuterClassName;
import static io.github.muehmar.pojobuilder.processor.AnnotationMemberExtractor.getOptionalDetection;
import static io.github.muehmar.pojobuilder.processor.AnnotationMemberExtractor.getPackagePrivateBuilder;

import ch.bluecare.commons.data.NonEmptyList;
import ch.bluecare.commons.data.PList;
import com.google.auto.service.AutoService;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.Optionals;
import io.github.muehmar.pojobuilder.Strings;
import io.github.muehmar.pojobuilder.annotations.Ignore;
import io.github.muehmar.pojobuilder.annotations.Nullable;
import io.github.muehmar.pojobuilder.annotations.OptionalDetection;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import io.github.muehmar.pojobuilder.exception.PojoBuilderException;
import io.github.muehmar.pojobuilder.generator.impl.gen.builder.PojoBuilderGenerator;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.BuildMethod;
import io.github.muehmar.pojobuilder.generator.model.ClassAccessLevelModifier;
import io.github.muehmar.pojobuilder.generator.model.Constructor;
import io.github.muehmar.pojobuilder.generator.model.FactoryMethod;
import io.github.muehmar.pojobuilder.generator.model.FieldBuilder;
import io.github.muehmar.pojobuilder.generator.model.Generics;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.ClassnameParser;
import io.github.muehmar.pojobuilder.generator.model.type.DeclaredType;
import io.github.muehmar.pojobuilder.generator.model.type.QualifiedClassname;
import io.github.muehmar.pojobuilder.generator.model.type.Type;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
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
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import lombok.Value;

@SupportedAnnotationTypes("*")
@AutoService(Processor.class)
public class PojoBuilderProcessor extends AbstractProcessor {
  private static final int MAX_ANNOTATION_PATH_DEPTH = 50;

  private final Optional<BiConsumer<Pojo, PojoSettings>> redirectPojo;

  private PojoBuilderProcessor(Optional<BiConsumer<Pojo, PojoSettings>> redirectPojo) {
    this.redirectPojo = redirectPojo;
  }

  public PojoBuilderProcessor() {
    this(Optional.empty());
  }

  /**
   * Creates an annotation processor which does not actually produce output but redirects the
   * created {@link Pojo} and {@link PojoSettings} instances to the given consumer.
   */
  public PojoBuilderProcessor(BiConsumer<Pojo, PojoSettings> redirectPojo) {
    this(Optional.of(redirectPojo));
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
        .forEach(this::processTypeElementAndPath);
  }

  private void processStaticMethod(PList<? extends Element> annotatedElements) {
    annotatedElements
        .filter(this::isMethod)
        .filter(ExecutableElement.class::isInstance)
        .map(ExecutableElement.class::cast)
        .filter(this::isStaticMethod)
        .distinct(Object::toString)
        .flatMapOptional(this::findAnnotationPath)
        .forEach(this::processExecutableElementAndPath);
  }

  private void processConstructor(PList<? extends Element> annotatedElements) {
    annotatedElements
        .filter(this::isConstructor)
        .filter(ExecutableElement.class::isInstance)
        .map(ExecutableElement.class::cast)
        .distinct(Object::toString)
        .flatMapOptional(this::findAnnotationPath)
        .forEach(this::processConstructorAndPath);
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

  private void processConstructorAndPath(
      ElementAndAnnotationPath<ExecutableElement> elementAndPath) {
    final PojoSettings pojoSettings = extractSettingsFromAnnotationPath(elementAndPath.getPath());
    final ExecutableElement constructorElement = elementAndPath.getElement();

    final Element maybeClassElement = constructorElement.getEnclosingElement();
    if (!(maybeClassElement instanceof TypeElement)) {
      throw new PojoBuilderException(
          String.format(
              "Cannot process constructor %s because enclosing element is not a class or record!",
              constructorElement.getSimpleName()));
    }
    final TypeElement classElement = (TypeElement) maybeClassElement;

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
            .map(e -> convertToPojoField(e, detectionSettings));

    final Pojo pojo =
        pojoBuilder()
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

    outputPojo(pojo, pojoSettings);
  }

  private boolean isStaticMethod(ExecutableElement executableElement) {
    return executableElement.getModifiers().contains(Modifier.STATIC);
  }

  private void processExecutableElementAndPath(
      ElementAndAnnotationPath<ExecutableElement> elementAndPath) {
    final PojoSettings pojoSettings = extractSettingsFromAnnotationPath(elementAndPath.getPath());
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

    outputPojo(pojo, pojoSettings);
  }

  private Pojo extractPojoFromFactoryMethod(
      QualifiedClassname pojoClassName,
      ExecutableElement executableElement,
      DetectionSettings detectionSettings,
      QualifiedClassname factoryMethodOwner,
      PackageName pojoPackage,
      Type returnType) {
    final PList<PojoField> fields =
        PList.fromIter(executableElement.getParameters())
            .map(e -> convertToPojoField(e, detectionSettings));

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

  private void processTypeElementAndPath(ElementAndAnnotationPath<TypeElement> elementAndPath) {
    final PojoSettings pojoSettings = extractSettingsFromAnnotationPath(elementAndPath.getPath());
    final TypeElement classElement = elementAndPath.getElement();
    final String fullClassName = classElement.toString();

    final QualifiedClassname pojoClassname = ClassnameParser.parseThrowing(fullClassName);

    final Pojo pojo = extractPojo(classElement, pojoSettings, pojoClassname);

    outputPojo(pojo, pojoSettings);
  }

  private Pojo extractPojo(
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
            .filter(this::isNonConstantField)
            .filter(this::isNotIgnoredField)
            .map(e -> convertToPojoField(e, detectionSettings));

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

  private PojoSettings extractSettingsFromAnnotationPath(
      NonEmptyList<AnnotationMirror> annotations) {
    return extractSettingsFromAnnotationPath(annotations.toPList(), PojoSettings.defaultSettings());
  }

  private PojoSettings extractSettingsFromAnnotationPath(
      PList<AnnotationMirror> annotations, PojoSettings currentSettings) {
    return annotations
        .headOption()
        .map(
            a ->
                extractSettingsFromAnnotationPath(
                    annotations.tail(), overrideWithAnnotationValues(a, currentSettings)))
        .orElse(currentSettings);
  }

  private PojoSettings overrideWithAnnotationValues(
      AnnotationMirror annotation, PojoSettings currentSettings) {
    return currentSettings
        .overrideOptionalDetection(getOptionalDetection(annotation))
        .overrideBuilderName(
            getBuilderName(annotation)
                .map(String::trim)
                .filter(Strings::nonEmpty)
                .map(Name::fromString))
        .overrideBuilderSetMethodPrefix(
            getBuilderSetMethodPrefix(annotation)
                .map(String::trim)
                .filter(Strings::nonEmpty)
                .map(Name::fromString))
        .overrideBuilderAccessLevel(
            getPackagePrivateBuilder(annotation)
                .map(this::classAccessLevelModifierFromIsPackagePrivateFlag))
        .overrideEnableStandardBuilder(getEnableStandardBuilder(annotation))
        .overrideEnableFullBuilder(getEnableFullBuilder(annotation))
        .overrideFullBuilderFieldOrder(getFullBuilderFieldOrder(annotation))
        .overrideIncludeOuterClassName(getIncludeOuterClassName(annotation));
  }

  private ClassAccessLevelModifier classAccessLevelModifierFromIsPackagePrivateFlag(
      boolean isPackagePrivate) {
    return isPackagePrivate
        ? ClassAccessLevelModifier.PACKAGE_PRIVATE
        : ClassAccessLevelModifier.PUBLIC;
  }

  private void outputPojo(Pojo pojo, PojoSettings pojoSettings) {
    Optionals.ifPresentOrElse(
        redirectPojo,
        output -> output.accept(pojo, pojoSettings),
        () -> writeBuilder(pojo, pojoSettings));
  }

  private void writeBuilder(Pojo pojo, PojoSettings settings) {
    writeJavaFile(
        settings.qualifiedBuilderName(pojo),
        PojoBuilderGenerator.pojoBuilderGenerator(),
        pojo,
        settings);
  }

  private void writeJavaFile(
      Name qualifiedClassName,
      Generator<Pojo, PojoSettings> gen,
      Pojo pojo,
      PojoSettings pojoSettings) {
    final String javaContent = gen.generate(pojo, pojoSettings, javaWriter()).asString();
    try {
      final JavaFileObject builderFile =
          processingEnv.getFiler().createSourceFile(qualifiedClassName.asString());
      try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
        out.println(javaContent);
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private boolean isNonConstantField(Element element) {
    final Set<Modifier> modifiers = element.getModifiers();
    return !modifiers.contains(Modifier.STATIC);
  }

  private boolean isNotIgnoredField(Element element) {
    final Optional<Ignore> annotation = Optional.ofNullable(element.getAnnotation(Ignore.class));
    return not(annotation.isPresent());
  }

  private PojoField convertToPojoField(Element element, DetectionSettings settings) {
    final Name fieldName = Name.fromString(element.getSimpleName().toString());
    final Type fieldType = TypeMirrorMapper.map(element.asType());

    return convertToPojoField(element, fieldName, fieldType, settings);
  }

  private PojoField convertToPojoField(
      Element element, Name name, Type type, DetectionSettings settings) {
    return PojoFieldMapper.initial()
        .or(this::mapOptionalPojoField)
        .or(this::mapNullablePojoField)
        .mapWithDefault(element, name, type, settings, () -> new PojoField(name, type, REQUIRED));
  }

  private Optional<PojoField> mapOptionalPojoField(
      Element element, Name name, Type type, DetectionSettings settings) {
    return Optional.of(type)
        .filter(
            ignore ->
                settings.getOptionalDetections().exists(OptionalDetection.OPTIONAL_CLASS::equals))
        .flatMap(this::getOptionalValueType)
        .map(typeParameter -> new PojoField(name, typeParameter, OPTIONAL));
  }

  private Optional<Type> getOptionalValueType(Type type) {
    final Function<DeclaredType, Optional<Type>> getOptionalType =
        classType ->
            Optional.of(classType)
                .filter(DeclaredType::isOptional)
                .flatMap(t -> t.getTypeParameters().headOption());
    return type.onDeclaredType(getOptionalType).flatMap(Function.identity());
  }

  private Optional<PojoField> mapNullablePojoField(
      Element element, Name name, Type type, DetectionSettings settings) {
    final PList<String> nullableAnnotationClasses =
        PList.of(
            Nullable.class.getName(), "javax.annotation.Nullable", "jakarta.annotation.Nullable");

    return PList.fromIter(element.getAnnotationMirrors())
        .map(AnnotationMirror::getAnnotationType)
        .map(javax.lang.model.type.DeclaredType::asElement)
        .map(Element::asType)
        .map(TypeMirror::toString)
        .find(annotationClassName -> nullableAnnotationClasses.exists(annotationClassName::equals))
        .filter(
            ignore ->
                settings
                    .getOptionalDetections()
                    .exists(OptionalDetection.NULLABLE_ANNOTATION::equals))
        .map(ignore -> new PojoField(name, type, OPTIONAL));
  }

  @FunctionalInterface
  private interface PojoFieldMapper {
    Optional<PojoField> map(
        Element element, Name name, Type type, DetectionSettings detectionSettings);

    default PojoFieldMapper or(PojoFieldMapper next) {
      final PojoFieldMapper self = this;
      return (element, name, type, settings) -> {
        final Optional<PojoField> result = self.map(element, name, type, settings);
        return result.isPresent() ? result : next.map(element, name, type, settings);
      };
    }

    default PojoField mapWithDefault(
        Element element, Name name, Type type, DetectionSettings settings, Supplier<PojoField> s) {
      return this.map(element, name, type, settings).orElseGet(s);
    }

    static PojoFieldMapper initial() {
      return ((element, name, type, settings) -> Optional.empty());
    }
  }

  @Value
  private static class ElementAndAnnotationPath<T extends Element> {
    T element;
    NonEmptyList<AnnotationMirror> path;
  }
}
