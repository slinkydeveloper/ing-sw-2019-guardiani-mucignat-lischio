package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Effect;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerColor;

import java.util.List;
import java.util.function.BiPredicate;

public class DecoratedEffect {

  private Effect effect;
  private List<String> additionalPhases;
  private int playersToChooseToHit;
  private BiPredicate<PlayerColor, GameModel> distancePredicate;

  public DecoratedEffect(Effect effect, List<String> additionalPhases, int playersToChooseToHit, BiPredicate<PlayerColor, GameModel> distancePredicate) {
    this.effect = effect;
    this.additionalPhases = additionalPhases;
    this.playersToChooseToHit = playersToChooseToHit;
    this.distancePredicate = distancePredicate;
  }

  public Effect get() {
    return effect;
  }

  public String getId() {
    return effect.getId();
  }

  public String getName() {
    return effect.getName();
  }

  public String getDescription() {
    return effect.getDescription();
  }

  public List<String> getAdditionalPhases() {
    return additionalPhases;
  }

  public int getPlayersToChooseToHit() {
    return playersToChooseToHit;
  }

  public BiPredicate<PlayerColor, GameModel> getDistancePredicate() {
    return distancePredicate;
  }
}
