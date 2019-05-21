package com.adrenalinici.adrenaline.server;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartServerDialogController {

  @FXML private ChoiceBox<DashboardChoice> dashboardChoiceBox;
  @FXML private ChoiceBox<PlayersChoice> playersNumberChoiceBox;
  @FXML private ChoiceBox<RulesChoice> ruleSetChoiceBox;
  @FXML private TextField rmiPortTextField;
  @FXML private TextField socketPortTextField;
  @FXML private Button startMatchButton;

  public void initialize() {
    dashboardChoiceBox.setItems(
      FXCollections.observableArrayList(DashboardChoice.values())
    );
    dashboardChoiceBox.setValue(DashboardChoice.SMALL);
    playersNumberChoiceBox.setItems(
      FXCollections.observableArrayList(PlayersChoice.values())
    );
    playersNumberChoiceBox.setValue(PlayersChoice.THREE);
    ruleSetChoiceBox.setItems(
      FXCollections.observableArrayList(RulesChoice.values())
    );
    ruleSetChoiceBox.setValue(RulesChoice.COMPLETE);

    rmiPortTextField.setText("9001");
    socketPortTextField.setText("9000");

    startMatchButton.setOnMouseClicked(this::onStartMatchClicked);
  }

  public void onStartMatchClicked(MouseEvent e) {
    if (dashboardChoiceBox.getValue() == null || playersNumberChoiceBox.getValue() == null || ruleSetChoiceBox.getValue() == null) {
      showErrorAlert();
    }
    String serializedRmiPort = rmiPortTextField.getText();
    String serializedSocktPort = socketPortTextField.getText();
    try {
      int rmiPort = Integer.valueOf(serializedRmiPort);
      int socketPort = Integer.valueOf(serializedSocktPort);

      FXMLLoader loader = new FXMLLoader(
        getClass().getResource(
          "/fxml/log_show.fxml"
        )
      );

      Stage stage = new Stage();
      stage.setScene(new Scene(loader.load()));

      TextAreaLogAppenderController controller = loader.getController();
      GameBootstrapper bootstrapper = new GameBootstrapper(
        dashboardChoiceBox.getValue(),
        playersNumberChoiceBox.getValue(),
        ruleSetChoiceBox.getValue(),
        rmiPort,
        socketPort
      );
      controller.setBootstrapper(bootstrapper);
      bootstrapper.start();
      stage.show();
      stage.setMaximized(true);

      ((Stage)this.startMatchButton.getScene().getWindow()).close();

    } catch (NumberFormatException e1) {
      showErrorAlert();
    } catch (IOException e2) {
      e2.printStackTrace();
    }
  }

  public void showErrorAlert() {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Invalid configuration");
    alert.setContentText("Make sure you added all valid data");
    alert.show();
  }

}
