package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.GameModel;

import java.io.Serializable;

public interface ModelEvent extends Serializable {

  GameModel getGameModel();

}
