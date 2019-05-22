package com.adrenalinici.adrenaline.server;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class StartServerDialogController {

  @FXML private TextField rmiPortTextField;
  @FXML private TextField socketPortTextField;
  @FXML private Button startMatchButton;

  public void initialize() {
    rmiPortTextField.setText("9001");
    socketPortTextField.setText("9000");

    startMatchButton.setOnMouseClicked(this::onStartMatchClicked);
  }

  public void onStartMatchClicked(MouseEvent e) {
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
