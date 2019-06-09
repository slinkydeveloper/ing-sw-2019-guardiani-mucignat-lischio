package com.adrenalinici.adrenaline.util;

import java.util.ArrayList;
import java.util.List;

public class ObservableImpl<T> implements Observable<T> {
  private List<Observer<T>> listeners;

  public ObservableImpl() {
    super();
    listeners = new ArrayList<>();
  }

  @Override
  public void registerObserver(Observer<T> observer) {
    listeners.add(observer);
  }

  @Override
  public void unregisterObserver(Observer<T> observer) {
    listeners.remove(observer);
  }

  @Override
  public void cleanObservers() {
    listeners.clear();
  }

  @Override
  public void notifyEvent(T value) {
    for (Observer<T> listener : listeners) {
      if (listener != null) {
        listener.onEvent(value);
      }
    }
  }
}
