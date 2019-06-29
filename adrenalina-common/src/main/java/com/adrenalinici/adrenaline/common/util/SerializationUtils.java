package com.adrenalinici.adrenaline.common.util;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerializationUtils {

  private static final Logger LOG = LogUtils.getLogger(SerializationUtils.class);

  public static byte[] serialize(Object o) {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      ObjectOutput out = new ObjectOutputStream(bos);
      out.writeObject(o);
      out.flush();
      return bos.toByteArray();
    } catch (EOFException e) {
      LOG.info("Strange EOFException thrown");
      throw new RuntimeException(e);
    } catch (IOException e) {
      LOG.log(Level.SEVERE, "Error while serializing " + o, e);
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T deserialize(byte[] bytes) {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
      ObjectInputStream in = new ObjectInputStream(bis);
      return (T) in.readObject();
    } catch (EOFException e) {
      LOG.info("Strange EOFException thrown");
      throw new RuntimeException(e);
    } catch (IOException | ClassNotFoundException e) {
      LOG.log(Level.SEVERE, "Error while deserializing", e);
      throw new RuntimeException(e);
    }
  }

}
