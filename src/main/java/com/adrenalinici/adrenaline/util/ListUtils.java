package com.adrenalinici.adrenaline.util;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

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

  public static <T> Predicate<T> notIn(List<T> notList) {
    return t -> !notList.contains(t);
  }

}
