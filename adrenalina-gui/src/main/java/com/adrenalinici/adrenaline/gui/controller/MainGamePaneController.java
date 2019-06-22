package com.adrenalinici.adrenaline.gui.controller;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.model.light.LightGameModel;
import com.adrenalinici.adrenaline.common.model.light.LightPlayerDashboard;
import com.adrenalinici.adrenaline.common.network.inbox.ViewEventMessage;
import com.adrenalinici.adrenaline.common.network.outbox.AvailablePowerUpCardsForRespawnMessage;
import com.adrenalinici.adrenaline.common.network.outbox.ModelEventMessage;
import com.adrenalinici.adrenaline.common.view.PowerUpCardChosenEvent;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.gui.GuiUtils;
import com.adrenalinici.adrenaline.gui.GuiView;
import javafx.fxml.FXML;

import java.util.List;
import java.util.stream.Collectors;

public class MainGamePaneController {

  @FXML DashboardGamePaneController dashboardController;
  @FXML MyStatusGamePaneController thisPlayerController;
  @FXML OtherPlayersGamePaneController otherPlayersController;

  private GuiView view;
  private String registeredHandler;

  private boolean firstUpdate = true;

  public void initialize() {}

  public void setView(GuiView view) {
    this.view = view;
  }

  public void start() {
    registeredHandler = view.getEventBus().registerEventHandler((message, eventBus, s) -> {
      message.onModelEventMessage(this::handleModelUpdate);
      message.onAvailablePowerUpCardsForRespawnMessage(this::handleAvailablePowerUpCardsForRespawn);
    });
    view.getEventBus().start();
  }

  public void handleModelUpdate(ModelEventMessage message) {
    if (firstUpdate) {
      firstUpdate = false;
      initializeView(message.getModelEvent().getGameModel());
    }

    ModelEvent event = message.getModelEvent();

    event.onDashboardCellUpdatedEvent(dashboardController::updateCell);
    event.onPlayerDashboardUpdatedEvent(playerDashboardUpdated -> {
      thisPlayerController.update(playerDashboardUpdated);
      otherPlayersController.update(playerDashboardUpdated);
    });
    event.onGameModelUpdatedEvent(gameModelUpdatedEvent -> dashboardController.updateGameModel(gameModelUpdatedEvent, view.getEventBus().getTurnOfPlayer()));
  }

  private void initializeView(LightGameModel gameModel) {
    dashboardController.initializeDashboard(gameModel.getDashboard().getDashboardChoice());
    dashboardController.initializeGameModel(gameModel, view.getEventBus().getTurnOfPlayer());
    thisPlayerController.initializePlayer(view.getEventBus().getMyPlayer());

    List<PlayerColor> otherPlayerList = gameModel
      .getPlayerDashboards()
      .stream()
      .map(LightPlayerDashboard::getPlayer)
      .filter(p -> p != view.getEventBus().getMyPlayer())
      .collect(Collectors.toList());

    otherPlayersController.initializePlayers(otherPlayerList);
  }

  public void handleAvailablePowerUpCardsForRespawn(AvailablePowerUpCardsForRespawnMessage message) {
    if (this.view.getEventBus().getMyPlayer() == message.getPlayer()) {
      PowerUpCard chosenCard = GuiUtils.showImagesRadioButtonDialog(
        "Scegli dove rinascere",
        "Scegli un powerup. In base al colore del powerup rinascerai.",
        message.getPowerUpCards(),
        GuiUtils::computePowerUpFilename
      );

      sendViewEvent(new PowerUpCardChosenEvent(view.getEventBus().getMyPlayer(), chosenCard));
    }
  }

  private void sendViewEvent(ViewEvent event) {
    this.view.getEventBus().notifyEvent(new ViewEventMessage(event));
  }
}
