package io.github.muehmar.pojobuilder.example.custombuildmethod;

import io.github.muehmar.pojobuilder.annotations.BuildMethod;
import io.github.muehmar.pojobuilder.annotations.PojoBuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import lombok.Value;

@Value
@PojoBuilder
public class CustomThrowingBuildMethod {
  String url;

  @BuildMethod
  static String customBuildMethod(CustomThrowingBuildMethod instance)
      throws URISyntaxException, MalformedURLException {
    new URI(instance.url);
    new URL(instance.url);
    return instance.url;
  }
}
