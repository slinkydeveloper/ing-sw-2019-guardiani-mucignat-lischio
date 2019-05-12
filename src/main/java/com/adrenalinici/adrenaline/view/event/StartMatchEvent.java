package com.adrenalinici.adrenaline.view.event;

import java.util.function.Consumer;

public class StartMatchEvent implements ViewEvent {

  @Override
  public boolean isStartMatchEvent() {
    return true;
  }
}
