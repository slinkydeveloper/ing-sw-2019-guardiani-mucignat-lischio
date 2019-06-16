package com.adrenalinici.adrenaline.server.network.handlers;

import com.adrenalinici.adrenaline.common.network.inbox.ConnectedPlayerMessage;
import com.adrenalinici.adrenaline.server.network.MessageHandler;
import com.adrenalinici.adrenaline.server.network.ServerContext;

public class ConnectedPlayerMessageHandler implements MessageHandler<ConnectedPlayerMessage> {
  @Override
  public void handleMessage(ConnectedPlayerMessage message, String connectionId, ServerContext context) {
    context.send(
      connectionId,
      HandlerUtils.generateAvailableMatchesMessage(context)
    );
  }
}
