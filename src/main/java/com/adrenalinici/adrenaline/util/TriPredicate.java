package com.adrenalinici.adrenaline.util;

@FunctionalInterface
public interface TriPredicate<X, Y, Z> {

  boolean test(X x, Y y, Z z);

}
