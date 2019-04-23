package com.adrenalinici.adrenaline.util;

@FunctionalInterface
public interface TriConsumer<X, Y, Z> {

  void consume(X x, Y y, Z z);

}
