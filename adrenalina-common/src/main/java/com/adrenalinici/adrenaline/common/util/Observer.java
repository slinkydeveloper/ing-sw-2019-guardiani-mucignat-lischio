package com.adrenalinici.adrenaline.common.util;

@FunctionalInterface
public interface Observer<T> {

  void onEvent(T newValue);

}
