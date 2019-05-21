package com.adrenalinici.adrenaline.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class OtherPlayersController {
  public void backToDashboard(MouseEvent mouseEvent) {
    Pane nextNode = new Pane();
    try {
      nextNode = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/dashbard1.fxml"));
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
    Scene scene = new Scene(nextNode);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(scene);
    window.show();
  }
}
