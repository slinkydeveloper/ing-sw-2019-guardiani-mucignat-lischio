package com.adrenalinici.adrenaline.client.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientGunLoader {

  public static final ClientGunLoader INSTANCE = new ClientGunLoader();

  private final Map<String, JsonNode> configs;
  private final ObjectMapper mapper;

  public ClientGunLoader() {
    this.configs = new HashMap<>();
    this.mapper = new ObjectMapper();
  }

  public String getGunName(String gunId) {
    return getGunJson(gunId).get("name").asText();
  }

  public Optional<String> getGunNote(String gunId) {
    return Optional.ofNullable(getGunJson(gunId).get("note")).map(JsonNode::asText);
  }

  public String getGunEffectName(String gunId, String effectId) {
    return getGunEffect(gunId, effectId).get("name").asText();
  }

  public String getGunEffectDescription(String gunId, String effectId) {
    return getGunEffect(gunId, effectId).get("description").asText();
  }

  private JsonNode getGunEffect(String gunId, String effectId) {
    JsonNode gunJson = getGunJson(gunId);

    return Optional.ofNullable(extractEffectIfIdEquals(gunJson, "baseEffect", effectId)).orElseGet(() ->
      Optional.ofNullable(extractEffectIfIdEquals(gunJson, "firstExtraEffect", effectId)).orElseGet(() ->
        Optional.ofNullable(extractEffectIfIdEquals(gunJson, "secondExtraEffect", effectId)).orElseGet(() ->
          Optional.ofNullable(extractEffectIfIdEquals(gunJson, "firstEffect", effectId)).orElseGet(() ->
            extractEffectIfIdEquals(gunJson, "secondEffect", effectId)
          )
        )
      )
    );
  }

  private JsonNode extractEffectIfIdEquals(JsonNode gunJson, String effectType, String effectId) {
    JsonNode extracted = gunJson.at("/" + effectType + "/id");
    return extracted.isMissingNode() || !extracted.asText().equals(effectId) ? null : gunJson.get(effectType);
  }

  private JsonNode getGunJson(String gunId) {
    if (configs.containsKey(gunId)) return configs.get(gunId);
    configs.put(gunId, getConfigurationJSONFromClasspath("guns/" + gunId + ".json"));
    return configs.get(gunId);
  }

  public JsonNode getConfigurationJSONFromClasspath(String filename) {
    try {
      if (ClientGunLoader.class.getResourceAsStream("/" + filename) == null)
        throw new IllegalStateException("You dumb, you miss file " + filename + " in classpath");
      return this.mapper.readTree(ClientGunLoader.class.getResourceAsStream("/" + filename));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
