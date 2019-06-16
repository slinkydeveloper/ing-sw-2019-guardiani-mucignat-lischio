package com.adrenalinici.adrenaline.common.model.event;

import com.adrenalinici.adrenaline.common.model.light.LightGameModel;

import java.io.Serializable;

public interface ModelEvent extends Serializable {

  LightGameModel getGameModel();

}
