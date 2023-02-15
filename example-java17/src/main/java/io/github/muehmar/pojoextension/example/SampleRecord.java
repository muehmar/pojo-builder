package io.github.muehmar.pojoextension.example;

import io.github.muehmar.pojoextension.annotations.SafeBuilder;
import java.util.Optional;

@SafeBuilder
public record SampleRecord(long id, String name, Optional<String> data) {}
