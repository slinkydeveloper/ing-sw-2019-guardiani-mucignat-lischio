package com.adrenalinici.adrenaline.gui.controller;

import javafx.fxml.FXML;

import java.awt.*;
import java.awt.event.ActionEvent;
import javafx.scene.control.Button;

public class socketRmiControllore {

  @FXML
  private Button socketButton;
  @FXML
  private Button rmiButton;

  @FXML
  public void socketClick(javafx.event.ActionEvent actionEvent) {
    // TODO mettere la rete socket e passare alla schermata successiva
    if (actionEvent.getSource().equals(socketButton)){
      System.out.println("socket button was played");
    }
  }



  public void rmiClick(javafx.event.ActionEvent actionEvent) {
    // TODO mettere la rete socket e passare alla schermata successiva
    if (actionEvent.getSource().equals(rmiButton)){
      System.out.println("socket button was played");
    }
  }
}
