package com.adrenalinici.adrenaline.common.network.outbox;

import java.util.function.Consumer;

public class InfoMessage implements OutboxMessage {

  private String information;
  private InfoType infoType;

  public InfoMessage(String information, InfoType infoType) {
    this.information = information;
    this.infoType = infoType;
  }

  public String getInformation() {
    return information;
  }

  public InfoType getInfoType() {
    return infoType;
  }

  @Override
  public void onInfoMessage(Consumer<InfoMessage> c) {
    c.accept(this);
  }
}
