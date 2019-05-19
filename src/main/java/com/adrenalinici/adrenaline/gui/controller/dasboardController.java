package com.adrenalinici.adrenaline.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class dasboardController {

  @FXML
  public void possibleMove(MouseEvent event){
    //TODO
  }

  public void possibleMove(javafx.scene.input.MouseEvent mouseEvent) {
    
  }

  public void chooseGun(javafx.scene.input.MouseEvent mouseEvent) {
  }

  public void pickUpAmmo(javafx.scene.input.MouseEvent mouseEvent) {
    
  }

  public void cangheGun(javafx.scene.input.MouseEvent mouseEvent) {
  }

  public void useAmmos(javafx.scene.input.MouseEvent mouseEvent) {
  }

  public void otherPlayers(javafx.scene.input.MouseEvent mouseEvent) {
    Pane nextNode = new Pane();
    try {
      nextNode = FXMLLoader.load(getClass().getClassLoader().getResource("fxmlFiles/otherPlayers.fxml"));
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
    Scene scene = new Scene(nextNode);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(scene);
    window.show();
  }
}
