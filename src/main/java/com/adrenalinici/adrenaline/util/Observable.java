package com.adrenalinici.adrenaline.util;

import java.util.WeakHashMap;

public class Observable<T> {
  private WeakHashMap<Observer<T>, Void> listeners;

  public Observable() {
    super();
    listeners = new WeakHashMap<>();
  }

  public void registerObserver(Observer<T> observer) {
    listeners.put(observer, null);
  }

  public void unregisterObserver(Observer<T> observer) {
    listeners.remove(observer);
  }

  public void cleanObservers() {
    listeners.clear();
  }

  protected void notifyEvent(T value) {
    for (Observer<T> listener : listeners.keySet()) {
      if (listener != null) {
        listener.onEvent(value);
      }
    }
  }
}
