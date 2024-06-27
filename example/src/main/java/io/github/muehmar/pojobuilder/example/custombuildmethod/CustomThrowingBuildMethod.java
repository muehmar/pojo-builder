package io.github.muehmar.pojobuilder.example.custombuildmethod;

import io.github.muehmar.pojobuilder.annotations.BuildMethod;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.Value;

@Value
@PojoBuilder
public class CustomThrowingBuildMethod {
  String url;

  @BuildMethod
  static URL customBuildMethod(CustomThrowingBuildMethod instance) throws MalformedURLException {
    return new URL(instance.url);
  }
}
