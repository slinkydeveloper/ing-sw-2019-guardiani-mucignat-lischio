package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.DecoratedEffect;
import com.adrenalinici.adrenaline.flow.FlowState;
import com.adrenalinici.adrenaline.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;

public class AlternativeEffectGunFlowState implements FlowState {

  private DecoratedAlternativeEffectGun chosenGun;
  private boolean firstEffect;
  private DecoratedEffect chosenEffect;
  private List<PlayerColor> chosenPlayersToHit;
  private List<PlayerColor> hitPlayers;

  public AlternativeEffectGunFlowState(DecoratedAlternativeEffectGun chosenGun) {
    this.chosenGun = chosenGun;
    this.chosenPlayersToHit = new ArrayList<>();
    this.hitPlayers = new ArrayList<>();
  }

  public DecoratedAlternativeEffectGun getChosenGun() {
    return chosenGun;
  }

  public DecoratedEffect getChosenEffect() {
    return chosenEffect;
  }

  public AlternativeEffectGunFlowState setChosenEffect(DecoratedEffect chosenEffect, boolean chosenEffectIsFirstEffect) {
    this.chosenEffect = chosenEffect;
    this.firstEffect = chosenEffectIsFirstEffect;
    return this;
  }

  public boolean isFirstEffect() {
    return firstEffect;
  }

  public List<PlayerColor> getChosenPlayersToHit() {
    return chosenPlayersToHit;
  }

  public List<PlayerColor> getHitPlayers() {
    return hitPlayers;
  }
}
