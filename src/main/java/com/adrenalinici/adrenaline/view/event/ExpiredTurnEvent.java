package com.adrenalinici.adrenaline.view.event;

public class ExpiredTurnEvent implements ViewEvent {
  @Override
  public boolean isExpiredTurnEvent() {
    return true;
  }
}
