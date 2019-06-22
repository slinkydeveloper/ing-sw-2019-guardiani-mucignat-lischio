package com.adrenalinici.adrenaline.gui.controller;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.Gun;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;
import com.adrenalinici.adrenaline.common.model.event.PlayerDashboardUpdatedEvent;
import com.adrenalinici.adrenaline.common.model.light.LightPlayerDashboard;
import com.adrenalinici.adrenaline.common.util.Bag;
import com.adrenalinici.adrenaline.gui.GuiUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

  public void initialize() {
    damagesGrid.setGridLinesVisible(true);
    marksGrid.setGridLinesVisible(true);

    updatePlayerInfo(
      Arrays.asList(PlayerColor.CYAN, PlayerColor.CYAN, PlayerColor.YELLOW),
      Collections.emptyList(),
      0,
      0,
      Collections.emptyList(),
      Collections.emptyList(),
      Collections.emptySet(),
      Collections.emptySet()
    );
  }

  public void initializePlayer(PlayerColor p) {
    thisPlayer = p;

    playerColorLabel.setText("Giocatore " + p.name());
    playerColorLabel.setTextFill(Paint.valueOf(p.name().toUpperCase()));

    updatePlayerInfo(
      Arrays.asList(PlayerColor.CYAN),
      Collections.emptyList(),
      0,
      0,
      Collections.emptyList(),
      Collections.emptyList(),
      Collections.emptySet(),
      Collections.emptySet()
    );
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

      powerupHBox.getChildren().add(
        new ImageView(new Image(url, 100, 150, true, true))
      );
    });

    gunsHBox.getChildren().clear();

    loadedGuns.forEach(gun -> {
      String url = String.format("/images/guns/%s.png", gun.getId());

      gunsHBox.getChildren().add(
        new ImageView(new Image(url, 100, 150, true, true))
      );
    });

    unloadedGuns.forEach(gun -> {
      String url = String.format("/images/guns/%s.png", gun.getId());

      gunsHBox.getChildren().add(
        new ImageView(new Image(url, 100, 150, true, true))
      );

      gunsHBox.setOpacity(0.5d);
    });

  }

}
