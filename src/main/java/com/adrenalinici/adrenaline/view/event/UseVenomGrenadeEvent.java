package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.model.PowerUpCard;
import com.adrenalinici.adrenaline.view.GameView;

import java.util.List;
import java.util.function.Consumer;

public class UseVenomGrenadeEvent extends BaseViewEvent {

  private PlayerColor playerColor;
  private List<PowerUpCard> chosenCards;

  public UseVenomGrenadeEvent(GameView view, PlayerColor playerColor, List<PowerUpCard> chosenCards) {
    super(view);
    this.playerColor = playerColor;
    this.chosenCards = chosenCards;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public List<PowerUpCard> getChosenCards() {
    return chosenCards;
  }

  @Override
  public void onUseVenomGrenadeEvent(Consumer<UseVenomGrenadeEvent> consumer) {
    consumer.accept(this);
  }
}
