package com.adrenalinici.adrenaline.util;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
  private List<Observer<T>> listeners;

  public Observable() {
    super();
    listeners = new ArrayList<>();
  }

  public void registerObserver(Observer<T> observer) {
    listeners.add(observer);
  }

  public void unregisterObserver(Observer<T> observer) {
    listeners.remove(observer);
  }

  public void cleanObservers() {
    listeners.clear();
  }

  protected void notifyEvent(T value) {
    for (Observer<T> listener : listeners) {
      if (listener != null) {
        listener.onEvent(value);
      }
    }
  }
}
