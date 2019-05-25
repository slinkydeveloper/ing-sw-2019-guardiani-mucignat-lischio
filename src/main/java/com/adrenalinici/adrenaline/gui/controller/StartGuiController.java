package com.adrenalinici.adrenaline.gui.controller;


import com.adrenalinici.adrenaline.gui.ErrorUtils;
import com.adrenalinici.adrenaline.util.LogUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StartGuiController {

  private static final Logger LOG = LogUtils.getLogger(StartGuiController.class);

  @FXML private Button startButton;

  public void changeSceneRmiSocket(ActionEvent actionEvent) {
    Pane nextNode;
    try {
      nextNode = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/connect_match.fxml"));
    } catch (IOException e) {
      LOG.log(Level.SEVERE, "Cannot find fxml", e);
      ErrorUtils.showExceptionAndClose(e,"Cannot find fxml");
      return;
    }
    Scene scene = new Scene(nextNode);
    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    window.setScene(scene);
    window.show();
  }
}


