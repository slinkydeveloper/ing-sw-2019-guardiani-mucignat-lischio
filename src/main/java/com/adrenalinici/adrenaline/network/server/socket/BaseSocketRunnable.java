package com.adrenalinici.adrenaline.network.server.socket;

import java.net.Socket;
import java.util.Map;

public abstract class BaseSocketRunnable implements Runnable {

  Map<Socket, String> connectedClients;

  public BaseSocketRunnable(Map<Socket, String> connectedClients) {
    this.connectedClients = connectedClients;
  }
}
