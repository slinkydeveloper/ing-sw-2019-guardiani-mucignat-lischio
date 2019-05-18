package com.adrenalinici.adrenaline.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.stream.Stream;

public class StreamUtils {

  public static <T> Stream<T> iteratorStream(Iterator<T> t) {
    Stream.Builder<T> builder = Stream.builder();
    t.forEachRemaining(builder::add);
    return builder.build();
  }

  public static <T> Stream<T> enumerationStream(Enumeration<T> e) {
    Stream.Builder<T> builder = Stream.builder();
    while (e.hasMoreElements()) {
      builder.add(e.nextElement());
    }
    return builder.build();
  }

}
