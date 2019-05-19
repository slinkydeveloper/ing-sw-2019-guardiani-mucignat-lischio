package com.adrenalinici.adrenaline.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class chooseColorPlayerController {
  public void goToDashboard(ActionEvent actionEvent) {
    Pane nextNode = new Pane();
    try {
      nextNode = FXMLLoader.load(getClass().getClassLoader().getResource("fxmlFiles/dashbard1.fxml"));
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
    Scene scene = new Scene(nextNode);
    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    window.setScene(scene);
    window.show();
  }
}
