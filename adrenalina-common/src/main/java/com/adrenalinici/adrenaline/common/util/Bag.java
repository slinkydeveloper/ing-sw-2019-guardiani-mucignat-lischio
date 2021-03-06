package com.adrenalinici.adrenaline.common.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Bag<T> {

  Map<T, Integer> bag;

  public Bag() {
    this.bag = new HashMap<>();
  }

  public Bag<T> add(T item) {
    bag.merge(item, 1, (oldValue, value) -> oldValue + 1);
    return this;
  }

  public Bag<T> addAll(Collection<T> collection) {
    collection.forEach(this::add);
    return this;
  }

  public Bag<T> removeAll(Collection<T> collection) {
    collection.forEach(this::remove);
    return this;
  }

  public int get(T item) {
    return bag.getOrDefault(item, 0);
  }

  public Bag<T> remove(T item) {
    bag.merge(item, 0, (oldValue, value) -> (oldValue - 1 == 0) ? null : oldValue - 1);
    return this;
  }

  public boolean contains(Bag<T> otherBag) {
    return otherBag.stream()
      .map(e -> e.getValue() <= get(e.getKey()))
      .reduce(true, Boolean::logicalAnd);
  }

  public boolean contains(Collection<T> collection) {
    return contains(from(collection));
  }

  public Stream<T> streamItems() {
    return bag
      .entrySet()
      .stream()
      .flatMap(e -> IntStream.range(0, e.getValue()).mapToObj(i -> e.getKey()));
  }

  public List<T> toList() {
    return streamItems().collect(Collectors.toList());
  }

  public int differentItems() {
    return this.bag.size();
  }

  public int totalItems() {
    return stream().map(Map.Entry::getValue).mapToInt(Integer::intValue).sum();
  }

  public Stream<Map.Entry<T, Integer>> stream() {
    return bag.entrySet().stream();
  }

  public static <T> Bag<T> from(Collection<T> collection) {
    Bag<T> b = new Bag<>();
    collection.forEach(b::add);
    return b;
  }

  public static <T> Bag<T> sum(Collection<T> c1, Collection<T> c2) {
    return Bag.from(c1).addAll(c2);
  }

  public static <T> Bag<T> difference(Collection<T> c1, Collection<T> c2) {
    return Bag.from(c1).removeAll(c2);
  }

}
