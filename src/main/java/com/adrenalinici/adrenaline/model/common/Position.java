package com.adrenalinici.adrenaline.model.common;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {

  private final int line;
  private final int cell;

  public Position(int line, int cell) {
    this.line = line;
    this.cell = cell;
  }

  public int line() {
    return line;
  }

  public int cell() {
    return cell;
  }

  public Position north() {
    return new Position(line - 1, cell);
  }

  public Position east() {
    return new Position(line, cell + 1);
  }

  public Position south() {
    return new Position(line + 1, cell);
  }

  public Position west() {
    return new Position(line, cell - 1);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Position position = (Position) o;
    return line == position.line &&
      cell == position.cell;
  }

  @Override
  public int hashCode() {
    return Objects.hash(line, cell);
  }

  @Override
  public String toString() {
    return "Position{" +
      "line=" + line +
      ", cell=" + cell +
      '}';
  }

  public static Position of(int line, int cell) {
    return new Position(line, cell);
  }
}
