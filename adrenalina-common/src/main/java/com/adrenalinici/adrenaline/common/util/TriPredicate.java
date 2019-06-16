package com.adrenalinici.adrenaline.common.util;

@FunctionalInterface
public interface TriPredicate<X, Y, Z> {

  boolean test(X x, Y y, Z z);

}
