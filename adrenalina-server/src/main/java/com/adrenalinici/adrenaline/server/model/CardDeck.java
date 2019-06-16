package com.adrenalinici.adrenaline.server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck<T> {

  private List<T> queue;

  public CardDeck(List<T> queue) {
    this.queue = new ArrayList<>(queue);
    Collections.shuffle(queue);
  }

  public boolean isEmpty() {
    return queue.isEmpty();
  }

  public T getCard() {
    T val = queue.remove(0);
    Collections.shuffle(queue);
    return val;
  }

  public void addCard(T card) {
    queue.add(card);
    Collections.shuffle(queue);
  }
}
