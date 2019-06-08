package com.adrenalinici.adrenaline.gui.controller;

import com.adrenalinici.adrenaline.gui.ErrorUtils;
import com.adrenalinici.adrenaline.gui.GuiView;
import com.adrenalinici.adrenaline.model.common.DashboardChoice;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.PlayersChoice;
import com.adrenalinici.adrenaline.model.common.RulesChoice;
import com.adrenalinici.adrenaline.network.outbox.AvailableMatchesMessage;
import com.adrenalinici.adrenaline.network.outbox.InfoMessage;
import com.adrenalinici.adrenaline.network.outbox.InfoType;
import com.adrenalinici.adrenaline.util.LogUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
      ErrorUtils.showExceptionAndClose(e,"Cannot find fxml");
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
      new URI(host + ":" + port); // To check if hostname is valid
    } catch (NumberFormatException e) {
      // Invalid port
      ErrorUtils.showExceptionAndClose(e, "Invalid port!");
      return null;
    } catch (URISyntaxException e) {
      // Invalid hostname
      ErrorUtils.showExceptionAndClose(e, "Invalid hostname!");
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
    // TODO on ok go to game scene
    // TODO on fail open an alert
    if (message.getInfoType() != InfoType.ERROR) {
      //moveToGameScene();
    } else {
      ErrorUtils.showErrorAlert("Game connection error", message.getInformation());
    }
  }

  private void handleChosenMatch(MouseEvent event) {
    String chosenMatch = matchesListView.getSelectionModel().getSelectedItem();
    if (chosenMatch == null) return; // Nothing was chosen
    Set<PlayerColor> availableColors = matches.get(chosenMatch);

    ChoiceDialog<PlayerColor> dialog = new ChoiceDialog<>(availableColors.iterator().next(), availableColors);
    dialog.setTitle("Choose a Player color");
    dialog.setHeaderText("Choose a player color you want to play with");
    dialog.setContentText("Player color:");

    Optional<PlayerColor> result = dialog.showAndWait();
    if (result.isPresent()){
      LOG.info("Trying to connect to match " + chosenMatch + " with color " + result.get());
      this.view.getEventBus().sendChosenMatch(chosenMatch, result.get());
    }
  }

  private void moveToGameScene() {
    this.view.getEventBus().stop();
    this.view.getEventBus().unregisterEventHandler(registeredHandler);
    LOG.info("Moving to game scene");
  }

}
