package com.adrenalinici.adrenaline.gui.controller;

import com.adrenalinici.adrenaline.common.model.DashboardChoice;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PlayersChoice;
import com.adrenalinici.adrenaline.common.model.RulesChoice;
import com.adrenalinici.adrenaline.common.network.outbox.AvailableMatchesMessage;
import com.adrenalinici.adrenaline.common.network.outbox.InfoMessage;
import com.adrenalinici.adrenaline.common.network.outbox.InfoType;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.gui.GuiUtils;
import com.adrenalinici.adrenaline.gui.GuiView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectMatchController {

  private static final Logger LOG = LogUtils.getLogger(ConnectMatchController.class);

  @FXML private TextField portText;
  @FXML private TextField hostText;
  @FXML private Button socketButton;
  @FXML private Button rmiButton;
  @FXML private ListView<String> matchesListView;
  @FXML private Button newMatchButton;

  private GuiView view;
  private Map<String, Set<PlayerColor>> matches;
  private String registeredHandler;

  private Boolean startingNewMatch;

  public void initialize() {
    newMatchButton.setDisable(true);
  }

  public void onConnectClicked(MouseEvent actionEvent) {
    if (actionEvent.getSource() == socketButton)
      view = startNetwork(GuiView::createSocketGuiView);
    else
      view = startNetwork(GuiView::createRmiGuiView);
    if (view != null) {
      newMatchButton.setDisable(false);
      registeredHandler = view.getEventBus().registerEventHandler((message, eventBus, s) -> {
        message.onAvailableMatchesMessage(this::handleAvailableMatchesMessage);
        message.onInfoMessage(this::handleInfoMessage);
      });
      view.getEventBus().start();
      view.startNetworkAdapter();
    }
  }

  @SuppressWarnings("unchecked")
  public void onStartNewMatchClicked(MouseEvent event) {
    // I create an alert injection my dialog pane built with fxml
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setHeaderText("Choose match configuration");

    GridPane choicesPane;
    try {
      choicesPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/new_match_dialog.fxml"));
    } catch (IOException e) {
      LOG.log(Level.SEVERE, "Cannot find fxml", e);
      GuiUtils.showExceptionAndClose(e,"Cannot find fxml");
      return;
    }

    TextArea matchNameText = (TextArea) choicesPane.lookup("#matchNameText");

    ChoiceBox<DashboardChoice> dashboardChoiceChoiceBox = (ChoiceBox<DashboardChoice>) choicesPane.lookup("#dashboardChoiceBox");
    dashboardChoiceChoiceBox.setItems(FXCollections.observableArrayList(DashboardChoice.values()));
    dashboardChoiceChoiceBox.setValue(DashboardChoice.SMALL);

    ChoiceBox<PlayersChoice> playersNumberChoiceBox = (ChoiceBox<PlayersChoice>) choicesPane.lookup("#playersNumberChoiceBox");
    playersNumberChoiceBox.setItems(FXCollections.observableArrayList(PlayersChoice.values()));
    playersNumberChoiceBox.setValue(PlayersChoice.THREE);

    ChoiceBox<RulesChoice> ruleSetChoiceBox = (ChoiceBox<RulesChoice>) choicesPane.lookup("#ruleSetChoiceBox");
    ruleSetChoiceBox.setItems(FXCollections.observableArrayList(RulesChoice.values()));
    ruleSetChoiceBox.setValue(RulesChoice.SIMPLE);

    alert.getDialogPane().setContent(choicesPane);

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK && !matchNameText.getText().isEmpty()) {
      LOG.info("Going to create a new match");
      startingNewMatch = true;
      view.getEventBus().sendStartNewMatch(
        matchNameText.getText(),
        dashboardChoiceChoiceBox.getValue(),
        playersNumberChoiceBox.getValue(),
        ruleSetChoiceBox.getValue()
      );
    }

  }

  private GuiView startNetwork(BiFunction<String, Integer, GuiView> fn) {
    String host;
    int port;
    try {
      host = hostText.getText();
      port = Integer.parseInt(portText.getText());
    } catch (NumberFormatException e) {
      // Invalid port
      GuiUtils.showExceptionAndClose(e, "Invalid port!");
      return null;
    }

    return fn.apply(host, port);
  }

  private void handleAvailableMatchesMessage(AvailableMatchesMessage message) {
    matches = message.getMatchesRemainingPlayers();

    // Now let's render the list view
    matchesListView.setItems(FXCollections.observableArrayList(message.getMatchesRemainingPlayers().keySet()));
    matchesListView.setOnMouseClicked(this::handleChosenMatch);
  }

  private void handleInfoMessage(InfoMessage message) {
    if (startingNewMatch == null) {
      GuiUtils.showErrorAlert("Generic error", message.getInformation());
    } else if (startingNewMatch) {
      if (message.getInfoType() == InfoType.ERROR) {
        GuiUtils.showErrorAlert("Error while initalizing match", message.getInformation());
      }
    } else {
      if (message.getInfoType() != InfoType.ERROR) {
        moveToGameScene();
      } else {
        GuiUtils.showErrorAlert("Game connection error", message.getInformation());
      }
    }
  }

  private void handleChosenMatch(MouseEvent event) {
    String chosenMatch = matchesListView.getSelectionModel().getSelectedItem();
    if (chosenMatch == null) return; // Nothing was chosen
    Set<PlayerColor> availableColors = matches.get(chosenMatch);

    PlayerColor chosenPlayer = GuiUtils.showPlayersRadioButtonDialog(
      "Scegli un colore giocatore",
      "Scegli un colore giocatore",
      new ArrayList<>(availableColors),
      false
    );

    startingNewMatch = false;

    LOG.info("Trying to connect to match " + chosenMatch + " with color " + chosenPlayer);
    this.view.getEventBus().setEnqueueFilter(m -> !(m instanceof InfoMessage));
    this.view.getEventBus().sendChosenMatch(chosenMatch, chosenPlayer);
  }

  private void moveToGameScene() {
    this.view.getEventBus().stop();
    this.view.getEventBus().unregisterEventHandler(registeredHandler);
    LOG.info("Moving to game scene");
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main_game_pane.fxml"));
      Scene newScene = new Scene(loader.load());
      Stage newStage = new Stage();
      newStage.setScene(newScene);
      newStage.setMaximized(true);

      MainGamePaneController controller = loader.getController();

      controller.setView(this.view);

      ((Stage)this.newMatchButton.getScene().getWindow()).close();
      newStage.show();

      controller.start();

    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Error while loading game scene", e);
      GuiUtils.showExceptionAndClose(e,"Error while loading game scene");
      return;
    }
  }

}
