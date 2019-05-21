package com.adrenalinici.adrenaline.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SocketRmiControllore {

  @FXML
  private Button socketButton;
  @FXML
  private Button rmiButton;


  public void socketNetwork(ActionEvent actionEvent) {
    Pane nextNode = new Pane();
    try {
      nextNode = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/choosePlayerColor.fxml"));
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
    Scene scene = new Scene(nextNode);
    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    window.setScene(scene);
    window.show();
  }


  public void rmiNetwork(ActionEvent actionEvent) {
    Pane nextNode = new Pane();
    try {
      nextNode = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/choosePlayerColor.fxml"));
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
    Scene scene = new Scene(nextNode);
    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    window.setScene(scene);
    window.show();
  }

}
