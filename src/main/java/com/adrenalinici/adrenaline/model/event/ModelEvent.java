package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.light.LightGameModel;

import java.io.Serializable;

public interface ModelEvent extends Serializable {

  LightGameModel getGameModel();

}
