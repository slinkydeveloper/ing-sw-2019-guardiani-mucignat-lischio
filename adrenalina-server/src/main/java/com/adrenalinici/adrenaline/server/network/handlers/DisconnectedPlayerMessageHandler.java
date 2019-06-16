package com.adrenalinici.adrenaline.server.network.handlers;

import com.adrenalinici.adrenaline.common.network.inbox.DisconnectedPlayerMessage;
import com.adrenalinici.adrenaline.server.network.MessageHandler;
import com.adrenalinici.adrenaline.server.network.ServerContext;

public class DisconnectedPlayerMessageHandler implements MessageHandler<DisconnectedPlayerMessage> {
  @Override
  public void handleMessage(DisconnectedPlayerMessage message, String connectionId, ServerContext context) {
    context.onDisconnection(connectionId);
  }
}
