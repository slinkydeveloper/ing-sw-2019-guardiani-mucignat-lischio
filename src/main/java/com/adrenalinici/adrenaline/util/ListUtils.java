package com.adrenalinici.adrenaline.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

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

  public static <X, Y> BiPredicate<X, Y> combineBiPredicates(List<BiPredicate<X, Y>> predicates) {
    return (x, y) -> predicates.stream().filter(Objects::nonNull).map(p -> p.test(x, y)).reduce(true, Boolean::logicalAnd);
  }

}
