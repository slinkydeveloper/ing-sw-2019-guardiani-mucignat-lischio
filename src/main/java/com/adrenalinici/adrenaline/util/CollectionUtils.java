package com.adrenalinici.adrenaline.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CollectionUtils {

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

  public static <K, V> Stream<K> keys(Map<K, V> map, V value) {
    return map
      .entrySet()
      .stream()
      .filter(entry -> value.equals(entry.getValue()))
      .map(Map.Entry::getKey);
  }

}
