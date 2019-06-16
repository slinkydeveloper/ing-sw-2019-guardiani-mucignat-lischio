package com.adrenalinici.adrenaline.gui;

import javafx.scene.control.Alert;

public class ErrorUtils {

  public static void showErrorAlert(String title, String message){
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void showExceptionAndClose(Throwable e, String errorString){
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error Dialog");
    alert.setHeaderText(errorString);
    alert.setContentText(e.getMessage());
    alert.showAndWait();
    System.exit(1);
  }

}
