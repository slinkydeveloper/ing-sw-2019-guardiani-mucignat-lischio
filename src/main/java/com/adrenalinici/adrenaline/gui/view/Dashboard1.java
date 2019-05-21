package com.adrenalinici.adrenaline.gui.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Dashboard1 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

  public void start(Stage primaryStage) throws IOException {

    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/dashbard1.fxml"));
    primaryStage.setTitle("Adrenaline");
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}
