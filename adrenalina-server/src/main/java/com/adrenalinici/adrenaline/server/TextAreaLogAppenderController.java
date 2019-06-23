package com.adrenalinici.adrenaline.server;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.logging.*;

public class TextAreaLogAppenderController extends Handler {

  @FXML private TextArea logArea;
  @FXML private Button stopButton;

  GameBootstrapper bootstrapper;

  public void initialize() {
    Logger rootLogger = LogManager.getLogManager().getLogger("");
    rootLogger.addHandler(this);
    this.setLevel(Level.FINE);

    logArea.setEditable(false);
    stopButton.setOnMouseClicked(this::onStopClicked);
  }

  public TextAreaLogAppenderController setBootstrapper(GameBootstrapper bootstrapper) {
    this.bootstrapper = bootstrapper;
    return this;
  }

  @Override
  public void publish(LogRecord record) {
   try {
    Platform.runLater(() -> {
        try {
          if (logArea != null) {
            if (logArea.getText().length() == 0) {
              logArea.setText(
                String.format("[%s] %s: %s\n", record.getLevel().getName(), record.getLoggerName(), record.getMessage())
              );
            } else {
              logArea.selectEnd();
              logArea.insertText(
                logArea.getText().length(),
                String.format("[%s] %s: %s\n", record.getLevel().getName(), record.getLoggerName(), record.getMessage())
              );
            }
          }
        } catch (final Throwable t) {
          System.out.println("Unable to append log to text area: "
            + t.getMessage());
        }
      });
    } catch (final IllegalStateException e) {}
  }

  public void onStopClicked(MouseEvent e) {
    try {
      this.bootstrapper.stop();
      Platform.exit();
      System.exit(0);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void flush() { }

  @Override
  public void close() throws SecurityException { }
}
