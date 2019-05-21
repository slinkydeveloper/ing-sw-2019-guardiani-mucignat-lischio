package com.adrenalinici.adrenaline.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.security.Policy;

public class DasboardController {

  @FXML
  private Polygon player;

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
      nextNode = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/otherPlayers.fxml"));
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
    Scene scene = new Scene(nextNode);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(scene);
    window.show();
  }

  @FXML
  private AnchorPane gun0;
  @FXML
  private AnchorPane gun1;
  @FXML
  private AnchorPane gun2;
  @FXML
  private Circle redAmmo0;
  @FXML
  private Circle redAmmo1;
  @FXML
  private Circle redAmmo2;
  @FXML
  private Circle cyanAmmo0;
  @FXML
  private Circle cyanAmmo1;
  @FXML
  private Circle cyanAmmo2;
  @FXML
  private Circle yellowAmmo0;
  @FXML
  private Circle yellowAmmo1;
  @FXML
  private Circle yellowAmmo2;
  @FXML
  private Sphere mark0;
  @FXML
  private Sphere killColor0;
  @FXML
  private Sphere mark1;
  @FXML
  private Sphere killColor1;
  @FXML
  private Sphere mark2;
  @FXML
  private Sphere killColor2;
  @FXML
  private Sphere mark3;
  @FXML
  private Sphere killColor3;
  @FXML
  private Sphere mark4;
  @FXML
  private Sphere killColor4;
  @FXML
  private Sphere mark5;
  @FXML
  private Sphere killColor5;
  @FXML
  private Sphere mark6;
  @FXML
  private Sphere killColor6;
  @FXML
  private Sphere mark7;
  @FXML
  private Sphere killColor7;
  @FXML
  private Sphere doubleKill0;
  @FXML
  private Sphere doubleKill1;
  @FXML
  private Sphere doubleKill2;
  @FXML
  private Sphere doubleKill3;
  @FXML
  private Sphere doubleKill4;
  @FXML
  private AnchorPane cyanCell00;
  @FXML
  private AnchorPane cyanCell20;
  @FXML
  private AnchorPane greenCell30;
  @FXML
  private AnchorPane redCell01;
  @FXML
  private AnchorPane redCell11;
  @FXML
  private AnchorPane yellowCell21;
  @FXML
  private AnchorPane bluCell31;
  @FXML
  private AnchorPane bluCell32;
  @FXML
  private AnchorPane bluCell22;
  @FXML
  private AnchorPane grayCell12;
  @FXML
  private AnchorPane redGun0;
  @FXML
  private AnchorPane redGun1;
  @FXML
  private AnchorPane redGun2;
  @FXML
  private AnchorPane yellowGun0;
  @FXML
  private AnchorPane yellowGun1;
  @FXML
  private AnchorPane yellowGun2;
  @FXML
  private AnchorPane cyanGun0;
  @FXML
  private AnchorPane cyanGun1;
  @FXML
  private AnchorPane cyanGun2;


  @FXML
  private Polygon player1;//TODO capisci come gestire i polygon

  //TODO doublekill
  //TODO kill con colorPLayer e marcatura
  //TODO come gestisco il colore ??


  @FXML
  private GridPane gridPane;
  @FXML
  private AnchorPane cyanCell10;
  @FXML
  private  AnchorPane CyanCell20;

  public void moveAction(ActionEvent actionEvent) throws NullPointerException{

  }

  public void pickUpAction(ActionEvent actionEvent) {
  }

  @FXML
  public void changeGunAction(ActionEvent actionEvent) {

  }

  public void respawnAction(ActionEvent actionEvent) {
    // cancellare il polygon e metterne uno nuovo in un'alta cella
    Polygon respawnPlayer = new Polygon();
    respawnPlayer.getPoints().addAll(new Double[]{
      30.0, 30.0,
      20.0, 10.0,
      10.0, 20.0 });
    respawnPlayer.setFill(Color.PINK);
    cyanCell20.getChildren().add(respawnPlayer);
    cyanCell00.getChildren().remove(player1);

  }

  // immetto le ammo nel controller



  public void useAmmoForGun(ActionEvent actionEvent) {
    redAmmo0.setFill(Color.BLACK);
    redAmmo1.setFill(Color.BLACK);
    redAmmo2.setFill(Color.BLACK);
    cyanAmmo0.setFill(Color.BLACK);
    cyanAmmo1.setFill(Color.BLACK);
    cyanAmmo2.setFill(Color.BLACK);
    yellowAmmo0.setFill(Color.BLACK);
    yellowAmmo1.setFill(Color.BLACK);
    yellowAmmo2.setFill(Color.BLACK);
  }
}
