package com.adrenalinici.adrenaline.gui.controller;

import com.adrenalinici.adrenaline.common.model.*;
import com.adrenalinici.adrenaline.common.model.event.PlayerDashboardUpdatedEvent;
import com.adrenalinici.adrenaline.common.model.light.LightGameModel;
import com.adrenalinici.adrenaline.common.model.light.LightPlayerDashboard;
import com.adrenalinici.adrenaline.common.network.inbox.ViewEventMessage;
import com.adrenalinici.adrenaline.common.util.Bag;
import com.adrenalinici.adrenaline.common.view.UseNewtonEvent;
import com.adrenalinici.adrenaline.common.view.UseTeleporterEvent;
import com.adrenalinici.adrenaline.gui.GuiUtils;
import com.adrenalinici.adrenaline.gui.GuiView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MyStatusGamePaneController {

  @FXML Label playerColorLabel;
  @FXML GridPane damagesGrid;
  @FXML GridPane marksGrid;
  @FXML Label pointsLabel;
  @FXML Label timesKilledLabel;
  @FXML Label yellowAmmoLabel;
  @FXML Label redAmmoLabel;
  @FXML Label blueAmmoLabel;
  @FXML HBox powerupHBox;
  @FXML HBox gunsHBox;

  private PlayerColor thisPlayer;
  private List<PlayerColor> otherPlayers;
  private List<Position> dashboardCellPositions;
  private GuiView view;

  public void initialize() {}

  public void initializeView(PlayerColor p, LightGameModel gameModel) {
    thisPlayer = p;

    this.dashboardCellPositions = gameModel
      .getDashboard()
      .stream()
      .filter(Objects::nonNull)
      .map(c -> Position.of(c.getLine(), c.getCell()))
      .collect(Collectors.toList());
    this.otherPlayers = gameModel
      .getPlayerDashboards()
      .stream()
      .map(LightPlayerDashboard::getPlayer)
      .filter(player -> player != thisPlayer)
      .collect(Collectors.toList());

    playerColorLabel.setText("Giocatore " + p.name());
    playerColorLabel.setTextFill(Paint.valueOf(p.name().toUpperCase()));

    LightPlayerDashboard playerDashboard = gameModel.getPlayerDashboard(p);
    this.updatePlayerInfo(
      playerDashboard.getDamages(),
      playerDashboard.getMarks(),
      playerDashboard.getPoints(),
      playerDashboard.getSkullsNumber(),
      playerDashboard.getAmmos(),
      playerDashboard.getPowerUpCards(),
      playerDashboard.getLoadedGuns(),
      playerDashboard.getUnloadedGuns()
    );
  }

  public void setView(GuiView view) {
    this.view = view;
  }

  public void update(PlayerDashboardUpdatedEvent event) {
    if (event.getPlayerColor() == thisPlayer) {
      LightPlayerDashboard playerDashboard = event.getGameModel().getPlayerDashboard(event.getPlayerColor());
      this.updatePlayerInfo(
        playerDashboard.getDamages(),
        playerDashboard.getMarks(),
        playerDashboard.getPoints(),
        playerDashboard.getSkullsNumber(),
        playerDashboard.getAmmos(),
        playerDashboard.getPowerUpCards(),
        playerDashboard.getLoadedGuns(),
        playerDashboard.getUnloadedGuns()
      );
    }
  }

  private void updatePlayerInfo(List<PlayerColor> damages, List<PlayerColor> marks, int points, int timesKilled, List<AmmoColor> ammos, List<PowerUpCard> powerUps, Set<Gun> loadedGuns, Set<Gun> unloadedGuns) {
    GuiUtils.fillPlayersColorGrid(damages, damagesGrid);
    GuiUtils.fillPlayersColorGrid(marks, marksGrid);

    pointsLabel.setText("Punti: " + points);
    timesKilledLabel.setText("Morti: " + timesKilled);

    Bag<AmmoColor> ammoBag = Bag.from(ammos);

    yellowAmmoLabel.setText(String.format("%d Gialle", ammoBag.get(AmmoColor.YELLOW)));
    redAmmoLabel.setText(String.format("%d Rosse", ammoBag.get(AmmoColor.RED)));
    blueAmmoLabel.setText(String.format("%d Blu", ammoBag.get(AmmoColor.BLUE)));

    powerupHBox.getChildren().clear();

    powerUps.forEach(powerup -> {
      String url = GuiUtils.computePowerUpFilename(powerup);

      ImageView imageView = GuiUtils.createCardImageView(url);
      if (powerup.getPowerUpType() == PowerUpType.TELEPORTER) {
        imageView.setOnMouseClicked(e -> handleChosenTeleporter(powerup, e));
      } else if (powerup.getPowerUpType() == PowerUpType.NEWTON) {
        imageView.setOnMouseClicked(e -> handleChosenNewton(powerup, e));
      }

      powerupHBox.getChildren().add(imageView);
    });

    gunsHBox.getChildren().clear();

    loadedGuns.forEach(gun -> {
      String url = GuiUtils.computeGunFilename(gun);

      gunsHBox.getChildren().add(
        GuiUtils.createCardImageView(url)
      );
    });

    unloadedGuns.forEach(gun -> {
      String url = GuiUtils.computeGunFilename(gun);

      ImageView image = GuiUtils.createCardImageView(url);
      image.setOpacity(0.5d);

      gunsHBox.getChildren().add(image);
    });

  }

  private void handleChosenTeleporter(PowerUpCard teleporterCard, MouseEvent e) {
    if (this.view.isMyTurn()) {
      GuiUtils.showChoiceDialogWithMappedValues(
        "Teletrasporto",
        "Dove ti vuoi teletrasportare?",
        dashboardCellPositions,
        p -> String.format("Linea %d, Cella %d", p.line(), p.cell())
      ).ifPresent(p ->
        this.view.getEventBus().notifyEvent(
          new ViewEventMessage(new UseTeleporterEvent(p, teleporterCard))
        )
      );
    }
    e.consume();
  }

  private void handleChosenNewton(PowerUpCard newtonCard, MouseEvent e) {
    if (this.view.isMyTurn()) {
      GuiUtils.showChoiceDialogWithMappedValues(
        "Raggio cinetico",
        "Su chi vuoi usare il raggio cinetico?",
        this.otherPlayers,
        PlayerColor::name
      ).ifPresent(playerColor ->
        GuiUtils.showChoiceDialogWithMappedValues(
          "Raggio cinetico",
          "Dove lo vuoi spostare?",
          dashboardCellPositions,
          p -> String.format("Linea %d, Cella %d", p.line(), p.cell())
        ).ifPresent(position ->
          this.view.getEventBus().notifyEvent(
            new ViewEventMessage(new UseNewtonEvent(newtonCard, position, playerColor))
          )
        )
      );
    }
    e.consume();
  }

}
