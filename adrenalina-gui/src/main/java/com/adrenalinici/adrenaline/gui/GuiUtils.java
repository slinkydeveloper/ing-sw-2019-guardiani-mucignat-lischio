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
import javafx.stage.Modality;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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

  public static void showPlayersRadioButtonDialog(String title, String question, List<PlayerColor> elements, boolean optional, Consumer<PlayerColor> onEnd) {
    showVerticalRadioButtonDialog(title, question, elements, GuiUtils::createPlayerCircle, optional, onEnd);
  }

  public static <T> void showLabelRadioButtonDialog(String title, String question, List<T> elements, Function<T, String> descriptionFn, boolean optional, Consumer<T> onEnd) {
    showVerticalRadioButtonDialog(title, question, elements, el -> new Label(descriptionFn.apply(el)), optional, onEnd);
  }

  public static <T> void showLabelCheckBoxDialog(String title, String question, List<T> elements, Function<T, String> descriptionFn, Predicate<List<T>> acceptancePredicate, boolean optional, Consumer<List<T>> onEnd) {
    showVerticalCheckBoxDialog(title, question, elements, el -> new Label(descriptionFn.apply(el)), acceptancePredicate, optional, onEnd);
  }

  public static <T> void showCardImagesRadioButtonDialog(String title, String question, List<T> elements, Function<T, String> fileNameFn, boolean optional, Consumer<T> onEnd) {
    showHorizontalRadioButtonDialog(title, question, elements, el -> GuiUtils.createCardImageView(fileNameFn.apply(el)), optional, onEnd);
  }

  public static <T> void showHorizontalRadioButtonDialog(String title, String question, List<T> elements, Function<T, Node> nodeGenFn, boolean optional, Consumer<T> onEnd) {
    showRadioButtonDialog(false, title, question, elements, nodeGenFn, optional, onEnd);
  }

  public static <T> void showVerticalRadioButtonDialog(String title, String question, List<T> elements, Function<T, Node> nodeGenFn, boolean optional, Consumer<T> onEnd) {
    showRadioButtonDialog(true, title, question, elements, nodeGenFn, optional, onEnd);
  }

  @SuppressWarnings("unchecked")
  private static <T> void showRadioButtonDialog(boolean vertical, String title, String question, List<T> elements, Function<T, Node> nodeGenFn, boolean optional, Consumer<T> onEnd) {
    if (elements.isEmpty()) {
      onEnd.accept(null);
      return;
    }
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.initModality(Modality.NONE);
    alert.setTitle(title);
    alert.setHeaderText(question);

    ToggleGroup group = new ToggleGroup();

    GridPane innerGridPane = new GridPane();
    innerGridPane.setHgap(10);
    innerGridPane.setVgap(10);
    innerGridPane.setPadding(new Insets(10, 10, 10, 10));

    for (int i = 0; i < elements.size(); i++) {
      Node node = nodeGenFn.apply(elements.get(i));
      RadioButton button = new RadioButton();
      button.setUserData(elements.get(i));
      button.setToggleGroup(group);

      if (vertical) {
        innerGridPane.add(button, 0, i);
        innerGridPane.add(node, 1, i);
      } else {
        innerGridPane.add(node, i, 0);
        innerGridPane.add(button, i, 1);
      }
    }

    group.getToggles().get(0).setSelected(true);

    alert.getDialogPane().setContent(innerGridPane);

    Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
    okButton.setOnAction(action -> {
      onEnd.accept((T) group.getSelectedToggle().getUserData());
    });
    Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
    cancelButton.setOnAction(actionEvent -> {
      if (!optional) {
        showHorizontalRadioButtonDialog(title, question, elements, nodeGenFn, optional, onEnd);
      } else {
        onEnd.accept(null);
      }
    });

    //TODO close top right button?

    alert.show();
  }

  @SuppressWarnings("unchecked")
  public static <T> void showVerticalCheckBoxDialog(String title, String question, List<T> elements, Function<T, Node> nodeGenFn, Predicate<List<T>> acceptancePredicate, boolean optional, Consumer<List<T>> onEnd) {
    if (elements.isEmpty()) {
      onEnd.accept(null);
      return;
    }
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

    Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
    okButton.setOnAction(action -> {
      List<T> chosen = dataMapping.entrySet().stream().filter(e -> e.getKey().isSelected()).map(Map.Entry::getValue).collect(Collectors.toList());
      if (acceptancePredicate != null && !acceptancePredicate.test(chosen)) {
        showVerticalCheckBoxDialog(title, question, elements, nodeGenFn, acceptancePredicate, optional, onEnd);
      } else {
        onEnd.accept(chosen);
      }
    });
    Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
    cancelButton.setOnAction(actionEvent -> {
      if (!optional) {
        showVerticalCheckBoxDialog(title, question, elements, nodeGenFn, acceptancePredicate, optional, onEnd);
      } else {
        onEnd.accept(null);
      }
    });

    alert.show();
  }

  public static ImageView createCardImageView(String url) {
    return new ImageView(new Image(url, 200, 250, true, true));
  }

}
