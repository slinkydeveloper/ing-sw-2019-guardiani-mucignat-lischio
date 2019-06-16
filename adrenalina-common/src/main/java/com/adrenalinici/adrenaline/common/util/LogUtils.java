package com.adrenalinici.adrenaline.common.util;

import java.util.logging.Logger;

public class LogUtils {

  public static Logger getLogger(Class<?> clazz) {
    if (!System.getProperties().contains("java.util.logging.config.file")) {
      if (LogUtils.class.getClassLoader().getResource("logging.properties") != null)
        System.setProperty("java.util.logging.config.file", LogUtils.class.getClassLoader().getResource("logging.properties").getFile());
    }
    return Logger.getLogger(clazz.getCanonicalName());
  }

}
