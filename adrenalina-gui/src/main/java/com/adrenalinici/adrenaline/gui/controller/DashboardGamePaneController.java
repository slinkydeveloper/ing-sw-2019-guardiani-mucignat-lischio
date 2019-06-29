package com.adrenalinici.adrenaline.gui.controller;

import com.adrenalinici.adrenaline.common.model.DashboardChoice;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.event.DashboardCellUpdatedEvent;
import com.adrenalinici.adrenaline.common.model.event.GameModelUpdatedEvent;
import com.adrenalinici.adrenaline.common.model.light.LightDashboardCell;
import com.adrenalinici.adrenaline.common.model.light.LightGameModel;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.gui.GuiUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.adrenalinici.adrenaline.common.model.DashboardChoice.*;

public class DashboardGamePaneController {

  private static final Logger LOG = LogUtils.getLogger(DashboardGamePaneController.class);

  @FXML GridPane dashboardGridPane;
  @FXML Label remainingSkullsLabel;
  @FXML Label playerTurnLabel;
  @FXML Label frenzyModeLabel;
  @FXML HBox playersKillTrackHBox;

  private DashboardCellController[][] cellControllers;

  public void initialize() {}

  public void initializeView(DashboardChoice dashboard) {
    this.dashboardGridPane
      .setStyle(String.format("-fx-background-image: url(/images/dashboards/%s.png); -fx-background-size: stretch", dashboard.name().toLowerCase()));

    this.cellControllers = new DashboardCellController[3][4];

    // Build the overlay
    for (int l = 0; l < 3; l++) {
      for (int c = 0; c < 4; c++) {
        try {
          FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/dashboard_cell.fxml"));
          this.dashboardGridPane.add(loader.load(), c, l);
          DashboardCellController controller = loader.getController();
          this.cellControllers[l][c] = controller;
        } catch (IOException e) {
          LOG.log(Level.SEVERE, "Cannot find fxml", e);
          GuiUtils.showExceptionAndClose(e,"Cannot find fxml");
          return;
        }
      }
    }

    // Draw the position
    for (int l = 0; l < 3; l++) {
      for (int c = 0; c < 4; c++) {
        if ((dashboard == SMALL && ((l == 0 && c == 3) || (l == 2 && c == 0))) ||
          (dashboard == MEDIUM_1 && l == 2 && c == 0) ||
          (dashboard == MEDIUM_2 && l == 0 && c == 3)) continue;
        cellControllers[l][c].setPosition(Position.of(l, c));
      }
    }
  }

  public void initializeView(LightGameModel model, PlayerColor playerTurn) {
    showGameInfo(model);
    updateTurnOfPlayer(playerTurn);
  }

  public void updateCell(DashboardCellUpdatedEvent cellUpdatedEvent) {
    LightDashboardCell dashboardCell = cellUpdatedEvent.getGameModel().getDashboard().getDashboardCell(cellUpdatedEvent.getCellPosition());
    DashboardCellController controller = cellControllers[cellUpdatedEvent.getCellPosition().line()][cellUpdatedEvent.getCellPosition().cell()];

    controller.setPlayers(dashboardCell.getPlayersInCell());

    dashboardCell.visit(respawnCell -> {
      controller.setGunsContent(respawnCell.getAvailableGuns());
    }, pickupCell -> {
      controller.setAmmoCardContent(pickupCell.getAmmoCard());
    });
  }

  public void updateGameModel(GameModelUpdatedEvent gameModelUpdatedEvent) {
    showGameInfo(gameModelUpdatedEvent.getGameModel());
  }

  public void updateTurnOfPlayer(PlayerColor playerTurn) {
    if (playerTurn != null) playerTurnLabel.setText("Turno del giocatore: " + playerTurn.name());
  }

  public void showGameInfo(LightGameModel gameModel) {
    int remainingSkulls = gameModel.getRemainingSkulls();
    boolean isFrenzyMode = gameModel.getRemainingSkulls() == 0;
    List<Map.Entry<PlayerColor, Boolean>> killScore = gameModel.getKillScore();
    List<PlayerColor> doubleKillScore = gameModel.getDoubleKillScore();

    remainingSkullsLabel.setText("Teschi rimanenti: " + remainingSkulls);
    if (isFrenzyMode) frenzyModeLabel.setText("Modalità frenesia attiva!");

    Map<PlayerColor, Integer> pointsMap = new HashMap<>();
    killScore.forEach(e -> pointsMap.merge(e.getKey(), e.getValue() ? 2 : 1, Integer::sum));
    doubleKillScore.forEach(p -> pointsMap.merge(p, 1, Integer::sum));

    playersKillTrackHBox.getChildren().clear();
    pointsMap.forEach((player, points) -> {
      Label label = new Label();
      label.setText(String.format("%s: %d", player.name(), points));
      label.setTextFill(Paint.valueOf(player.name().toUpperCase()));
      playersKillTrackHBox.getChildren().add(label);
    });

  }

}
