package io.github.muehmar.pojobuilder.example;

import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.util.Optional;

@PojoBuilder
public record SampleRecord(long id, String name, Optional<String> data) {}
