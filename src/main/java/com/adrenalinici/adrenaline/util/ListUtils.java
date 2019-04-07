package com.adrenalinici.adrenaline.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

  public static <T> void difference(List<T> minuend, List<T> subtractor) {
    for (T a : subtractor) {
      minuend.remove(a);
    }
  }

  public static <T> List<T> differencePure(List<T> minuend, List<T> subtractor) {
    List<T> newMinuend = new ArrayList<>(minuend);
    for (T a : subtractor) {
      newMinuend.remove(a);
    }
    return newMinuend;
  }

}
