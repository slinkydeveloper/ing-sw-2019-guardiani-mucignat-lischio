package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Gun;

import java.util.List;

public interface DecoratedGun extends Gun {

  List<String> getPhases();

}
