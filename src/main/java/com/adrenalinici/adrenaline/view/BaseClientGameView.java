package com.adrenalinici.adrenaline.view;

import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.network.inbox.ChosenMyPlayerColorMessage;
import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.network.inbox.ViewEventMessage;
import com.adrenalinici.adrenaline.util.Observable;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.List;

public abstract class BaseClientGameView extends Observable<InboxMessage> implements GameView {

  private PlayerColor myPlayer;
  private PlayerColor turnOfPlayer;

  public void sendChosenMyPlayer(PlayerColor color) {
    this.myPlayer = color;
    notifyEvent(new ChosenMyPlayerColorMessage(color));
  }

  public abstract void showChooseMyPlayer(List<PlayerColor> colorList);

  public void sendViewEvent(ViewEvent event) {
    notifyEvent(new ViewEventMessage(event));
  }

  public PlayerColor getMyPlayer() {
    return myPlayer;
  }

  public BaseClientGameView setMyPlayer(PlayerColor myPlayer) {
    this.myPlayer = myPlayer;
    return this;
  }

  public PlayerColor getTurnOfPlayer() {
    return turnOfPlayer;
  }

  public BaseClientGameView setTurnOfPlayer(PlayerColor turnOfPlayer) {
    this.turnOfPlayer = turnOfPlayer;
    return this;
  }
}
