package com.adrenalinici.adrenaline.gui.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SocketRmi extends Application {


  public static void main(String[] args) {
    launch(args);
  }

  @Override
    public void start(Stage primaryStage) throws IOException {
      Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmlFiles/ScoketRmi.fxml"));
      primaryStage.setTitle("choose socket/rmi");
      primaryStage.setScene(new Scene(root));
      primaryStage.show();
    }
}
