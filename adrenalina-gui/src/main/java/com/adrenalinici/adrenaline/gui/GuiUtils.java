package com.adrenalinici.adrenaline.gui;

import com.adrenalinici.adrenaline.common.model.Gun;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GuiUtils {

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

  public static Circle createPlayerCircle(PlayerColor playerColor) {
    Circle c = new Circle(15d, Paint.valueOf(playerColor.name().toUpperCase()));
    c.setStyle("-fx-stroke: BLACK;");
    return c;
  }
  
  public static void fillPlayersColorGrid(List<PlayerColor> playerItems, GridPane grid) {
    grid.getChildren().clear();
    for (int i = 0; i < playerItems.size(); i++) {
      grid.add(GuiUtils.createPlayerCircle(playerItems.get(i)), i, 0);
    }
  }

  public static String computePowerUpFilename(PowerUpCard card) {
    return String.format("/images/powerups/%s_%s.png", card.getAmmoColor().name().toLowerCase(), card.getPowerUpType().name().toLowerCase());
  }

  public static String computeGunFilename(Gun gun) {
    return computeGunFilename(gun.getId());
  }

  public static String computeGunFilename(String gunId) {
    return String.format("/images/guns/%s.png", gunId);
  }

  @SuppressWarnings("unchecked")
  public static <T> T showImagesRadioButtonDialog(String title, String question, List<T> elements, Function<T, String> fileNameFn) {
    if (elements.isEmpty()) return null;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(question);

    // Group
    ToggleGroup group = new ToggleGroup();

    GridPane imagesGridPane = new GridPane();
    imagesGridPane.setPadding(new Insets(10, 10, 10, 10));

    for (int i = 0; i < elements.size(); i++) {
      String url = fileNameFn.apply(elements.get(i));

      ImageView imageView = new ImageView(new Image(url, 100, 150, true, true));
      RadioButton button = new RadioButton();
      button.setUserData(elements.get(i));
      button.setToggleGroup(group);

      imagesGridPane.add(imageView, i, 0);
      imagesGridPane.add(button, i, 1);
    }

    group.getToggles().get(0).setSelected(true);

    alert.getDialogPane().setContent(imagesGridPane);

    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      return (T) group.getSelectedToggle().getUserData();
    } else {
      return null;
    }
  }

  public static <T> T showChoiceDialogWithMappedValues(String title, String contentText, List<T> values, Function<T, String> fn) {
    Map<String, T> valuesMap = values.stream().collect(Collectors.toMap(fn, Function.identity()));
    List<String> choices = new ArrayList<>(valuesMap.keySet());

    ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);

    dialog.setTitle(title);
    dialog.setHeaderText(title);
    dialog.setContentText(contentText);

    Optional<String> result = dialog.showAndWait();
    dialog.getDialogPane().requestFocus();

    if (result.isPresent()) {
      return valuesMap.get(dialog.getSelectedItem());
    } else {
      return null;
    }
  }

}
