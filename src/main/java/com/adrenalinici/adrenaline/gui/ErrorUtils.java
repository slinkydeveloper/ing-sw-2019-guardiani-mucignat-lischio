package com.adrenalinici.adrenaline.gui;

import javafx.scene.control.Alert;

import java.io.IOException;

public class ErrorUtils {
  public static void showExceptionAndClose(Throwable e, String errorString){
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error Dialog");
    alert.setHeaderText(errorString);
    alert.setContentText(e.getMessage());
    alert.showAndWait();
    System.exit(1);
  }
}
