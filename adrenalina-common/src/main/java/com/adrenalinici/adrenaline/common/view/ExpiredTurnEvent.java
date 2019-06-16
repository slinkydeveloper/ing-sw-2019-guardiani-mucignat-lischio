package com.adrenalinici.adrenaline.common.view;

public class ExpiredTurnEvent implements ViewEvent {
  @Override
  public boolean isExpiredTurnEvent() {
    return true;
  }
}
