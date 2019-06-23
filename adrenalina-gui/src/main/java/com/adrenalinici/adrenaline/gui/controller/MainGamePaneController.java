package com.adrenalinici.adrenaline.gui.controller;

import com.adrenalinici.adrenaline.common.model.Action;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.model.light.LightGameModel;
import com.adrenalinici.adrenaline.common.model.light.LightPlayerDashboard;
import com.adrenalinici.adrenaline.common.network.inbox.ViewEventMessage;
import com.adrenalinici.adrenaline.common.network.outbox.*;
import com.adrenalinici.adrenaline.common.view.*;
import com.adrenalinici.adrenaline.gui.GuiUtils;
import com.adrenalinici.adrenaline.gui.GuiView;
import javafx.fxml.FXML;

import java.util.ArrayList;
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
      message.onAvailableActionsMessage(this::handleAvailableActions);
      message.onAvailableMovementsMessage(this::handleAvailableMovements);
      message.onAvailableGunsToPickupMessage(this::handleAvailableGunsToPickup);
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

  public void handleAvailableActions(AvailableActionsMessage message) {
    if (isMyTurn()) {
      Action chosen = GuiUtils.showChoiceDialogWithMappedValues(
        "Scegli una azione",
        "Azione:",
        message.getActions(),
        Action::toString
      );

      if (chosen != null) {
        sendViewEvent(new ActionChosenEvent(chosen));
      } else {
        sendViewEvent(new ActionChosenEvent(null));
      }
    }
  }

  public void handleAvailableMovements(AvailableMovementsMessage message) {
    if (isMyTurn()) {
      Position chosen = GuiUtils.showChoiceDialogWithMappedValues(
        "Scegli una posizione",
        "Posizione:",
        message.getPositions(),
        p -> String.format("[%d, %d]", p.line(), p.cell())
      );
      if (chosen != null) {
        sendViewEvent(new MovementChosenEvent(chosen));
      } else {
        sendViewEvent(new MovementChosenEvent(null));
      }
    }
  }

  public void handleAvailableGunsToPickup(AvailableGunsToPickupMessage message) {
    if (isMyTurn()) {
      String chosenCard = GuiUtils.showImagesRadioButtonDialog(
        "Scegli un'arma",
        "Scegli un'arma da raccogliere. L'arma verrà raccolta carica.",
        new ArrayList<>(message.getGuns()),
        GuiUtils::computeGunFilename
      );

      sendViewEvent(new GunChosenEvent(chosenCard));
    }
  }

  private boolean isMyTurn() {
    return this.view.getEventBus().getMyPlayer() == this.view.getEventBus().getTurnOfPlayer();
  }

  private void sendViewEvent(ViewEvent event) {
    this.view.getEventBus().notifyEvent(new ViewEventMessage(event));
  }
}
