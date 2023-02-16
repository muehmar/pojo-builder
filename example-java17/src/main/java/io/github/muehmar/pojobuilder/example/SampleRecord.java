package io.github.muehmar.pojobuilder.example;

import io.github.muehmar.pojobuilder.annotations.SafeBuilder;
import java.util.Optional;

@SafeBuilder
public record SampleRecord(long id, String name, Optional<String> data) {}
