package com.adrenalinici.adrenaline.gui.controller;

import com.adrenalinici.adrenaline.common.model.Action;
import com.adrenalinici.adrenaline.common.model.CellColor;
import com.adrenalinici.adrenaline.common.model.Effect;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.model.light.LightGameModel;
import com.adrenalinici.adrenaline.common.model.light.LightPlayerDashboard;
import com.adrenalinici.adrenaline.common.network.inbox.ViewEventMessage;
import com.adrenalinici.adrenaline.common.network.outbox.*;
import com.adrenalinici.adrenaline.common.view.*;
import com.adrenalinici.adrenaline.gui.GuiUtils;
import com.adrenalinici.adrenaline.gui.GuiView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainGamePaneController {

  @FXML DashboardGamePaneController dashboardController;
  @FXML MyStatusGamePaneController thisPlayerController;
  @FXML OtherPlayersGamePaneController otherPlayersController;

  private GuiView view;
  private String registeredHandler;

  private boolean firstUpdate = true;
  private Dialog openedDialog;

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
      message.onAvailableEnemyMovementsMessage(this::handleAvailableEnemyMovements);
      message.onNextTurnMessage(this::handleNextTurn);
      message.onReloadableGunsMessage(this::handleReloadableGuns);
      message.onAvailableAlternativeEffectsGunMessage(this::handleAvailableAlternativeEffectsGun);
      message.onChoosePlayerToHitMessage(this::handleChoosePlayerToHit);
      message.onAvailableExtraEffectsMessage(this::handleAvailableExtraEffects);
      message.onAvailableGunsMessage(this::handleAvailableGuns);
      message.onAvailableTagbackGrenadeMessage(this::handleAvailableTagbackGrenade);
      message.onAvailableRoomsMessage(this::handleAvailableRooms);
      message.onAvailableCellsToHitMessage(this::handleAvailableCellsToHit);
      message.onChooseScopePlayerMessage(this::handleChooseScopePlayer);
      message.onRankingMessage(this::handleRanking);
    });
    view.getEventBus().start();
  }

  private void handleModelUpdate(ModelEventMessage message) {
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
    event.onGameModelUpdatedEvent(gameModelUpdatedEvent -> dashboardController.updateGameModel(gameModelUpdatedEvent));
  }

  private void initializeView(LightGameModel gameModel) {
    dashboardController.initializeView(gameModel.getDashboard().getDashboardChoice());
    dashboardController.initializeView(gameModel, view.getEventBus().getTurnOfPlayer());
    thisPlayerController.initializeView(view.getEventBus().getMyPlayer(), gameModel);
    thisPlayerController.setView(this.view);

    List<PlayerColor> otherPlayerList = gameModel
      .getPlayerDashboards()
      .stream()
      .map(LightPlayerDashboard::getPlayer)
      .filter(p -> p != view.getEventBus().getMyPlayer())
      .collect(Collectors.toList());

    otherPlayersController.initializePlayers(otherPlayerList, gameModel);
  }

  private void handleAvailablePowerUpCardsForRespawn(AvailablePowerUpCardsForRespawnMessage message) {
    if (this.view.getEventBus().getMyPlayer() == message.getPlayer()) {
      this.openedDialog = GuiUtils.showCardImagesRadioButtonDialog(
        "Scegli dove rinascere",
        "Scegli un powerup. In base al colore del powerup rinascerai.",
        message.getPowerUpCards(),
        GuiUtils::computePowerUpFilename,
        false,
        chosenCard -> sendViewEvent(new PowerUpCardChosenEvent(view.getEventBus().getMyPlayer(), chosenCard))
      );
    }
  }

  private void handleAvailableActions(AvailableActionsMessage message) {
    if (isMyTurn()) {
      this.openedDialog = GuiUtils.showLabelRadioButtonDialog(
        "Scegli una azione",
        "Azione:",
        message.getActions(),
        Action::name,
        true,
        chosen -> sendViewEvent(new ActionChosenEvent(chosen))
      );
    }
  }

  private void handleAvailableMovements(AvailableMovementsMessage message) {
    if (isMyTurn()) {
      this.openedDialog = GuiUtils.showLabelRadioButtonDialog(
        "Scegli una posizione",
        "Posizione:",
        message.getPositions(),
        p -> String.format("[%d, %d]", p.line(), p.cell()),
        true,
        chosen -> sendViewEvent(new MovementChosenEvent(chosen))
      );
    }
  }

  private void handleAvailableGunsToPickup(AvailableGunsToPickupMessage message) {
    if (isMyTurn()) {
      this.openedDialog = GuiUtils.showCardImagesRadioButtonDialog(
        "Scegli un'arma",
        "Scegli un'arma da raccogliere. L'arma verrà raccolta carica.",
        new ArrayList<>(message.getGuns()),
        GuiUtils::computeGunFilename,
        true,
        chosenCard -> sendViewEvent(new GunChosenEvent(chosenCard))
      );
    }
  }

  private void handleAvailableEnemyMovements(AvailableEnemyMovementsMessage message) {
    if (isMyTurn()) {
      this.openedDialog = GuiUtils.showLabelRadioButtonDialog(
        "Scegli una posizione dove spostare il nemico",
        "Posizione:",
        message.getPositions(),
        p -> String.format("[%d, %d]", p.line(), p.cell()),
        true,
        chosen -> sendViewEvent(new EnemyMovementChosenEvent(chosen))
      );
    }
  }

  private void handleNextTurn(NextTurnMessage message) {
    this.dashboardController.updateTurnOfPlayer(this.view.getEventBus().getTurnOfPlayer());
    if (isMyTurn())
      sendViewEvent(new NewTurnEvent());
    if (openedDialog != null && openedDialog.isShowing()) {
      openedDialog.close();
      openedDialog = null;
    }
  }

  private void handleReloadableGuns(ReloadableGunsMessage message) {
    if (isMyTurn()) {
      this.openedDialog = GuiUtils.showCardImagesRadioButtonDialog(
        "Scegli un'arma da ricaricare",
        "Scegli un'arma da ricaricare. L'arma verrà ricaricata e verranno scalate le munizioni necessarie.",
        new ArrayList<>(message.getGuns()),
        GuiUtils::computeGunFilename,
        true,
        chosenCard -> sendViewEvent(new GunChosenEvent(chosenCard))
      );
    }
  }

  private void handleAvailableAlternativeEffectsGun(AvailableAlternativeEffectsGunMessage message) {
    if (isMyTurn()) {
      this.openedDialog = GuiUtils.showLabelRadioButtonDialog(
        "Scegli un effetto dell'arma scelta",
        "Scegli un effetto. Verranno scalate le munizioni necessarie.",
        Arrays.asList(message.getFirstEffect(), message.getSecondEffect()),
        e -> String.format("%s: %s", e.getName(), e.getDescription()),
        true,
        chosen -> sendViewEvent(new AlternativeGunEffectChosenEvent(chosen.getId().equals(message.getSecondEffect().getId())))
      );
    }
  }

  private void handleChoosePlayerToHit(ChoosePlayerToHitMessage message) {
    if (isMyTurn()) {
      this.openedDialog = GuiUtils.showPlayersRadioButtonDialog(
        "Scegli un giocatore da colpire",
        "Scegli un giocatore da colpire",
        message.getPlayers(),
        true,
        color -> sendViewEvent(new PlayerChosenEvent(color))
      );
    }
  }

  private void handleAvailableExtraEffects(AvailableExtraEffectsMessage message) {
    if (isMyTurn()) {
      ArrayList<Effect> effects = new ArrayList<>();
      if (message.getFirstExtraEffect() != null) effects.add(message.getFirstExtraEffect());
      if (message.getSecondExtraEffect() != null) effects.add(message.getSecondExtraEffect());

      this.openedDialog = GuiUtils.showLabelCheckBoxDialog(
        "Scegli gli effetti da applicare",
        "Scegli gli effetti da applicare, verrano scalati i costi degli effetti",
        effects,
        e -> String.format("%s: %s", e.getName(), e.getDescription()),
        true,
        chosenEffects -> sendViewEvent(new BaseGunEffectChosenEvent(
          chosenEffects.contains(message.getFirstExtraEffect()),
          chosenEffects.contains(message.getSecondExtraEffect())
        ))
      );

    }
  }

  private void handleAvailableGuns(AvailableGunsMessage message) {
    if (isMyTurn()) {
      this.openedDialog = GuiUtils.showCardImagesRadioButtonDialog(
        "Scegli un'arma da utilizzare",
        "Scegli un'arma da utilizzare",
        new ArrayList<>(message.getGuns()),
        GuiUtils::computeGunFilename,
        true,
        chosenCard -> sendViewEvent(new GunChosenEvent(chosenCard))
      );
    }
  }

  private void handleAvailableTagbackGrenade(AvailableTagbackGrenadeMessage message) {
    if (message.getPlayer().equals(this.view.getEventBus().getMyPlayer())) {
      this.openedDialog = GuiUtils.showCardImagesRadioButtonDialog(
        "Scegli la granata venom da utilizzare",
        "Scegli la granata venom da utilizzare per marcare il nemico che ti ha sparato",
        message.getPowerUpCards(),
        GuiUtils::computePowerUpFilename,
        true,
        chosenCard -> sendViewEvent(new UseTagbackGrenadeEvent(message.getPlayer(), chosenCard))
      );
    }
  }

  private void handleAvailableRooms(AvailableRoomsMessage message) {
    if (isMyTurn()) {
      this.openedDialog = GuiUtils.showLabelRadioButtonDialog(
        "Scegli una stanza",
        "Stanza:",
        new ArrayList<>(message.getRooms()),
        CellColor::name,
        true,
        chosen -> sendViewEvent(new RoomChosenEvent(chosen))
      );
    }
  }

  private void handleAvailableCellsToHit(AvailableCellsToHitMessage message) {
    if (isMyTurn()) {
      this.openedDialog = GuiUtils.showLabelRadioButtonDialog(
        "Scegli cella dove applicare l'effetto",
        "Scegli cella dove applicare l'effetto",
        new ArrayList<>(message.getCells()),
        p -> String.format("[%d, %d]", p.line(), p.cell()),
        true,
        chosen -> sendViewEvent(new CellToHitChosenEvent(chosen))
      );
    }
  }

  private void handleChooseScopePlayer(ChooseScopePlayerMessage chooseScopePlayerMessage) {
    if (isMyTurn()) {
      this.openedDialog = GuiUtils.showPlayersRadioButtonDialog(
        "Scegli un giocatore da colpire",
        "Scegli un giocatore da colpire con lo scope",
        chooseScopePlayerMessage.getPlayers(),
        true,
        color -> {
          if (color == null) {
            sendViewEvent(new PlayerChosenEvent(null));
          } else {
            sendViewEvent(new PlayerChosenEvent(color));

            this.openedDialog = GuiUtils.showCardImagesRadioButtonDialog(
              "Scegli quale scope utilizzare",
              "Scegli quale scope utilizzare",
              chooseScopePlayerMessage.getScopes(),
              GuiUtils::computePowerUpFilename,
              true,
              chosen ->
                sendViewEvent(new PowerUpCardChosenEvent(this.view.getEventBus().getMyPlayer(), chosen))
              );
          }
        }
      );
    }
  }

  private void handleRanking(RankingMessage message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Classifica finale");
    alert.setHeaderText("La partita è finita, ecco la classifica finale");
    alert.setContentText(
      message
        .getRanking()
        .stream()
        .map(e -> "Giocatore: " + e.getKey() + " - Punti: " + e.getValue())
        .collect(Collectors.joining("\n"))
    );

    alert.showAndWait();

    this.view.stopNetworkAdapter();
    Platform.exit();
  }

  private boolean isMyTurn() {
    return this.view.getEventBus().getMyPlayer() == this.view.getEventBus().getTurnOfPlayer();
  }

  private void sendViewEvent(ViewEvent event) {
    this.view.getEventBus().notifyEvent(new ViewEventMessage(event));
  }
}
