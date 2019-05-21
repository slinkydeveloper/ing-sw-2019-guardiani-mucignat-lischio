package com.adrenalinici.adrenaline.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class BufferedConsoleOut {

  public static BufferedConsoleOut OUT = new BufferedConsoleOut(new BufferedWriter(new OutputStreamWriter(System.out)));

  BufferedWriter writer;

  public BufferedConsoleOut(BufferedWriter writer) {
    this.writer = writer;
  }

  public void println(String line) {
    try {
      writer.append(line + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void flush() {
    try {
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
