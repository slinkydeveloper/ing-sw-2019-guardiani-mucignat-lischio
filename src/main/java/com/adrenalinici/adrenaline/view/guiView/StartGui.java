package com.adrenalinici.adrenaline.view.guiView;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class StartGui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
      Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxmlFiles/StartGui.fxml"));
      //Parent root = FXMLLoader.load(getClass().getResource("StartGui"));
      primaryStage.setTitle("Adrenaline");
      primaryStage.setScene(new Scene(root));
      primaryStage.show();
    }
}
