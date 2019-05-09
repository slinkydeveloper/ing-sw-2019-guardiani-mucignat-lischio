package com.adrenalinici.adrenaline.view;

import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.network.inbox.ChosenMyPlayerColorMessage;
import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.network.inbox.ViewEventMessage;
import com.adrenalinici.adrenaline.util.Observable;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.List;

public abstract class BaseClientGameView extends Observable<InboxMessage> implements GameView {

  public void sendChosenMyPlayer(PlayerColor color) {
    notifyEvent(new ChosenMyPlayerColorMessage(color));
  }

  public abstract void showChooseMyPlayer(List<PlayerColor> colorList);

  public void sendViewEvent(ViewEvent event) {
    notifyEvent(new ViewEventMessage(event));
  }

}
