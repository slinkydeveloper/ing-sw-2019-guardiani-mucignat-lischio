package com.adrenalinici.adrenaline.util;

@FunctionalInterface
public interface Observer<T> {

  void onEvent(T newValue);

}
