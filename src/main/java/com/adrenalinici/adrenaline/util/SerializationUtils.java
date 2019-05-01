package com.adrenalinici.adrenaline.util;

import java.io.*;

public class SerializationUtils {

  public static byte[] serialize(Object o) {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      ObjectOutput out = new ObjectOutputStream(bos);
      out.writeObject(o);
      out.flush();
      return bos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T deserialize(byte[] bytes) {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
      ObjectInputStream in = new ObjectInputStream(bis);
      return (T) in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

}
