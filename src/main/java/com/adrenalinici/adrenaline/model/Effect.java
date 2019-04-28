package com.adrenalinici.adrenaline.model;

public class Effect {

  String id;
  String name;
  String description;

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
}
