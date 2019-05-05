package com.adrenalinici.adrenaline.util;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogUtils {

  public static Logger getLogger(Class<?> clazz) {
    if (!System.getProperties().contains("java.util.logging.config.file"))
      System.setProperty("java.util.logging.config.file", LogUtils.class.getClassLoader().getResource("logging.properties").getFile());
    return Logger.getLogger(clazz.getName());
  }

}
