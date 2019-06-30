package com.adrenalinici.adrenaline.common.model;

import java.io.Serializable;
import java.util.Objects;

public class Effect implements Serializable {

  private String gunId;
  private String id;

  public Effect(String gunId, String id) {
    this.gunId = gunId;
    this.id = id;
  }

  public String getGunId() {
    return gunId;
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Effect effect = (Effect) o;
    return Objects.equals(gunId, effect.gunId) &&
      Objects.equals(id, effect.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gunId, id);
  }
}
