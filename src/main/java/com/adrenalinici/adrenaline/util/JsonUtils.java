package com.adrenalinici.adrenaline.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {

  public static final ObjectMapper mapper = new ObjectMapper();

  public static JsonNode getConfigurationJSONFromClasspath(String filename) {
    try {
      return mapper.readTree(JsonUtils.class.getResourceAsStream(filename));
    } catch (IOException e) {
      System.err.println("You dumb, you miss file " + filename + " in classpath");
      e.printStackTrace();
      return null;
    }
  }

}
