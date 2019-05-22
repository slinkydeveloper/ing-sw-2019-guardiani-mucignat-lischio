package com.adrenalinici.adrenaline.network.server.handlers;

import com.adrenalinici.adrenaline.network.inbox.DisconnectedPlayerMessage;
import com.adrenalinici.adrenaline.network.server.MessageHandler;
import com.adrenalinici.adrenaline.network.server.ServerContext;

public class DisconnectedPlayerMessageHandler implements MessageHandler<DisconnectedPlayerMessage> {
  @Override
  public void handleMessage(DisconnectedPlayerMessage message, String connectionId, ServerContext context) {
    context.onDisconnection(connectionId);
  }
}
