package com.adrenalinici.adrenaline.model;

public class Effect {
  String name;
  String description;

  public Effect(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
