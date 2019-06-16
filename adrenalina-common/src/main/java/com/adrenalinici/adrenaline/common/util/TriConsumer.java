package com.adrenalinici.adrenaline.common.util;

@FunctionalInterface
public interface TriConsumer<X, Y, Z> {

  void accept(X x, Y y, Z z);

}
