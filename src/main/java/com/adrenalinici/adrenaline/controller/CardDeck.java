package com.adrenalinici.adrenaline.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CardDeck<T> {

  List<T> queue;

  public CardDeck(List<T> queue) {
    this.queue = queue;
    Collections.shuffle(queue);
  }

  public Optional<T> getCard() {
    T val = queue.remove(0);
    Collections.shuffle(queue);
    return Optional.ofNullable(val);
  }
}
