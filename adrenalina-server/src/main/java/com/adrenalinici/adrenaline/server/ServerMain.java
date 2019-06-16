package com.adrenalinici.adrenaline.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerMain extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/start_server_dialog.fxml"));
    primaryStage.setTitle("Adrenaline server starter");
    primaryStage.setScene(new Scene(root));
    primaryStage.setResizable(false);
    primaryStage.show();
  }

}
