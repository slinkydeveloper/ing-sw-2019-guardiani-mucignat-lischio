package com.adrenalinici.adrenaline.server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck<T> {

  private List<T> deck;

  public CardDeck(List<T> deck) {
    this.deck = new ArrayList<>(deck);
    Collections.shuffle(deck);
  }

  public boolean isEmpty() {
    return deck.isEmpty();
  }

  public T getCard() {
    T val = deck.remove(0);
    Collections.shuffle(deck);
    return val;
  }

  public void addCard(T card) {
    deck.add(card);
    Collections.shuffle(deck);
  }
}
