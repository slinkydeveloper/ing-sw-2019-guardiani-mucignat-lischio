package com.adrenalinici.adrenaline.gui.controller;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.light.LightGameModel;
import com.adrenalinici.adrenaline.common.model.light.LightPlayerDashboard;
import com.adrenalinici.adrenaline.gui.GuiUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

import java.util.List;

public class PlayerGamePaneController {

  @FXML Label playerColorLabel;
  @FXML GridPane damagesGrid;
  @FXML GridPane marksGrid;
  @FXML Label pointsLabel;
  @FXML Label timesKilledLabel;

  public void initializePlayer(PlayerColor p, LightGameModel model) {
    playerColorLabel.setText("Giocatore " + p.name());
    playerColorLabel.setTextFill(Paint.valueOf(p.name().toUpperCase()));

    LightPlayerDashboard dashboard = model.getPlayerDashboard(p);

    updatePlayerInfo(dashboard.getDamages(), dashboard.getMarks(), dashboard.getPoints(), dashboard.getSkullsNumber());
  }

  public void updatePlayerInfo(List<PlayerColor> damages, List<PlayerColor> marks, int points, int timesKilled) {
    GuiUtils.fillPlayersColorGrid(damages, damagesGrid);
    GuiUtils.fillPlayersColorGrid(marks, marksGrid);

    pointsLabel.setText("Punti: " + points);
    timesKilledLabel.setText("Morti: " + timesKilled);
  }

}
