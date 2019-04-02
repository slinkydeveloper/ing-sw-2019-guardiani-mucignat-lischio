package com.adrenalinici.adrenaline;

import java.util.List;

public class ListUtils {

  public static <T> void difference(List<T> minuend, List<T> subtractor) {
    for (T a : subtractor) {
      minuend.remove(a);
    }
  }

}
