package com.adrenalinici.adrenaline.gui.controller;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.event.PlayerDashboardUpdatedEvent;
import com.adrenalinici.adrenaline.common.model.light.LightPlayerDashboard;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.gui.GuiUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OtherPlayersGamePaneController {

  private static final Logger LOG = LogUtils.getLogger(OtherPlayersGamePaneController.class);

  @FXML VBox otherPlayersVBox;

  private Map<PlayerColor, PlayerGamePaneController> playerGamePaneControllers;

  public void initialize() {}

  public void initializePlayers(List<PlayerColor> players) {
    this.playerGamePaneControllers = new HashMap<>();
    players.forEach(p -> {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/player_game_pane.fxml"));
        otherPlayersVBox.getChildren().add(loader.load());

        PlayerGamePaneController controller = loader.getController();
        this.playerGamePaneControllers.put(p, controller);

        controller.initializePlayer(p);
      } catch (IOException e) {
        LOG.log(Level.SEVERE, "Cannot find fxml", e);
        GuiUtils.showExceptionAndClose(e,"Cannot find fxml");
        return;
      }
    });
  }

  public void update(PlayerDashboardUpdatedEvent event) {
    Optional.ofNullable(playerGamePaneControllers.get(event.getPlayerColor()))
      .ifPresent(controller -> {
        LightPlayerDashboard playerDashboard = event.getGameModel().getPlayerDashboard(event.getPlayerColor());
        controller.updatePlayerInfo(
          playerDashboard.getDamages(),
          playerDashboard.getMarks(),
          playerDashboard.getPoints(),
          playerDashboard.getSkullsNumber()
        );
      });
  }

}
