package com.adrenalinici.adrenaline.util;

public interface Observable<T> {
  void registerObserver(Observer<T> observer);

  void unregisterObserver(Observer<T> observer);

  void cleanObservers();

  void notifyEvent(T value);
}
