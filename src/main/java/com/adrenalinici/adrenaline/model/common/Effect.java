package com.adrenalinici.adrenaline.model.common;

import java.io.Serializable;
import java.util.Objects;

public class Effect implements Serializable {

  private String id;
  private String name;
  private String description;

  public Effect(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Effect effect = (Effect) o;
    return Objects.equals(id, effect.id) &&
      Objects.equals(name, effect.name) &&
      Objects.equals(description, effect.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description);
  }
}
