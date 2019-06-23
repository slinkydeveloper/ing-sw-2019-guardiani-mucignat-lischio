package com.adrenalinici.adrenaline.gui;

import com.adrenalinici.adrenaline.common.model.Gun;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GuiUtils {

  public static void showErrorAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void showExceptionAndClose(Throwable e, String errorString) {
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

  public static <T> T showChoiceDialogWithMappedValues(String title, String contentText, List<T> values, Function<T, String> fn, boolean optional) {
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
    } else if (!optional) {
      return showChoiceDialogWithMappedValues(title, contentText, values, fn, optional);
    } else {
      return null;
    }
  }

  public static PlayerColor showPlayersRadioButtonDialog(String title, String question, List<PlayerColor> elements, boolean optional) {
    return showVerticalRadioButtonDialog(title, question, elements, GuiUtils::createPlayerCircle, optional);
  }

  public static <T> T showLabelRadioButtonDialog(String title, String question, List<T> elements, Function<T, String> descriptionFn, boolean optional) {
    return showVerticalRadioButtonDialog(title, question, elements, el -> new Label(descriptionFn.apply(el)), optional);
  }

  public static <T> List<T> showLabelCheckBoxDialog(String title, String question, List<T> elements, Function<T, String> descriptionFn, Predicate<List<T>> acceptancePredicate, boolean optional) {
    return showVerticalCheckBoxDialog(title, question, elements, el -> new Label(descriptionFn.apply(el)), acceptancePredicate, optional);
  }

  @SuppressWarnings("unchecked")
  public static <T> T showCardImagesRadioButtonDialog(String title, String question, List<T> elements, Function<T, String> fileNameFn, boolean optional) {
    return showHorizontalRadioButtonDialog(title, question, elements, el -> GuiUtils.createCardImageView(fileNameFn.apply(el)), optional);
  }

  @SuppressWarnings("unchecked")
  public static <T> T showHorizontalRadioButtonDialog(String title, String question, List<T> elements, Function<T, Node> nodeGenFn, boolean optional) {
    if (elements.isEmpty()) return null;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(question);

    ToggleGroup group = new ToggleGroup();

    GridPane imagesGridPane = new GridPane();
    imagesGridPane.setPadding(new Insets(10, 10, 10, 10));

    for (int i = 0; i < elements.size(); i++) {
      Node node = nodeGenFn.apply(elements.get(i));
      RadioButton button = new RadioButton();
      button.setUserData(elements.get(i));
      button.setToggleGroup(group);

      imagesGridPane.add(node, i, 0);
      imagesGridPane.add(button, i, 1);
    }

    group.getToggles().get(0).setSelected(true);

    alert.getDialogPane().setContent(imagesGridPane);

    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      return (T) group.getSelectedToggle().getUserData();
    } else if (!optional) {
      return showHorizontalRadioButtonDialog(title, question, elements, nodeGenFn, optional);
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T showVerticalRadioButtonDialog(String title, String question, List<T> elements, Function<T, Node> nodeGenFn, boolean optional) {
    if (elements.isEmpty()) return null;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(question);

    ToggleGroup group = new ToggleGroup();

    GridPane descriptionsGridPane = new GridPane();
    descriptionsGridPane.setPadding(new Insets(10, 10, 10, 10));

    for (int i = 0; i < elements.size(); i++) {
      Node node = nodeGenFn.apply(elements.get(i));
      RadioButton button = new RadioButton();
      button.setUserData(elements.get(i));
      button.setToggleGroup(group);

      descriptionsGridPane.add(button, 0, i);
      descriptionsGridPane.add(node, 1, i);
    }

    group.getToggles().get(0).setSelected(true);

    alert.getDialogPane().setContent(descriptionsGridPane);

    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      return (T) group.getSelectedToggle().getUserData();
    } else if (!optional) {
      return showVerticalRadioButtonDialog(title, question, elements, nodeGenFn, optional);
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> showVerticalCheckBoxDialog(String title, String question, List<T> elements, Function<T, Node> nodeGenFn, Predicate<List<T>> acceptancePredicate, boolean optional) {
    if (elements.isEmpty()) return null;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(question);

    Map<CheckBox, T> dataMapping = new HashMap<>();

    GridPane descriptionsGridPane = new GridPane();
    descriptionsGridPane.setPadding(new Insets(10, 10, 10, 10));

    for (int i = 0; i < elements.size(); i++) {
      T element = elements.get(i);

      Node node = nodeGenFn.apply(element);
      CheckBox checkBox = new CheckBox();

      descriptionsGridPane.add(checkBox, 0, i);
      descriptionsGridPane.add(node, 1, i);

      dataMapping.put(checkBox, element);
    }

    alert.getDialogPane().setContent(descriptionsGridPane);

    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      List<T> chosen = dataMapping.entrySet().stream().filter(e -> e.getKey().isSelected()).map(Map.Entry::getValue).collect(Collectors.toList());
      if (acceptancePredicate != null && !acceptancePredicate.test(chosen)) {
        return showVerticalCheckBoxDialog(title, question, elements, nodeGenFn, acceptancePredicate, optional);
      } else {
        return chosen;
      }
    } else if (!optional) {
      return showVerticalCheckBoxDialog(title, question, elements, nodeGenFn, acceptancePredicate, optional);
    } else {
      return null;
    }
  }

  public static ImageView createCardImageView(String url) {
    return new ImageView(new Image(url, 200, 250, true, true));
  }

}
