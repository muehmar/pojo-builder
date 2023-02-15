package io.github.muehmar.pojoextension.example;

import java.util.List;
import java.util.Map;
import lombok.Value;

@Value
public class GenericFieldClass {
  Map<String, List<Integer>> listMap;
}
