package com.adrenalinici.adrenaline.gui.controller;

import com.adrenalinici.adrenaline.common.model.*;
import com.adrenalinici.adrenaline.gui.GuiUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DashboardCellController {

  @FXML Label position;
  @FXML HBox players;
  @FXML Label content;

  public void initialize() { }

  public void setPosition(Position position) {
    this.position.setText(String.format("[%s, %s]", position.line(), position.cell()));
  }

  public void setGunsContent(Set<Gun> guns) {
    this.content.setText(
      "Armi in questa cella:\n" + guns.stream().map(Gun::getName).collect(Collectors.joining(", "))
    );
  }

  public void setAmmoCardContent(AmmoCard card) {
    if (card.isPickPowerUp()) {
      this.content.setText(
        "Carta munizioni in questa cella:\nPower Up " + card.getAmmoColor().stream().map(AmmoColor::name).collect(Collectors.joining(", "))
      );
    }
    else {
      this.content.setText(
        "Carta munizioni in questa cella:\n" + card.getAmmoColor().stream().map(AmmoColor::name).collect(Collectors.joining(", "))
      );
    }
  }

  public void setPlayers(List<PlayerColor> players) {
    this.players.getChildren().clear();
    players.stream().map(GuiUtils::createPlayerCircle).forEach(c -> this.players.getChildren().add(c));
  }

}
