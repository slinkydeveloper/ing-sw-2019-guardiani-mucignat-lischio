package com.adrenalinici.adrenaline.network.server.handlers;

import com.adrenalinici.adrenaline.network.inbox.ConnectedPlayerMessage;
import com.adrenalinici.adrenaline.network.server.MessageHandler;
import com.adrenalinici.adrenaline.network.server.ServerContext;

public class ConnectedPlayerMessageHandler implements MessageHandler<ConnectedPlayerMessage> {
  @Override
  public void handleMessage(ConnectedPlayerMessage message, String connectionId, ServerContext context) {
    context.send(
      connectionId,
      HandlerUtils.generateAvailableMatchesMessage(context)
    );
  }
}
