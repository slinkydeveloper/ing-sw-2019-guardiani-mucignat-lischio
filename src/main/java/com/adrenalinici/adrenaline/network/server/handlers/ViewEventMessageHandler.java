package com.adrenalinici.adrenaline.network.server.handlers;

import com.adrenalinici.adrenaline.network.inbox.ViewEventMessage;
import com.adrenalinici.adrenaline.network.server.MessageHandler;
import com.adrenalinici.adrenaline.network.server.ServerContext;

public class ViewEventMessageHandler implements MessageHandler<ViewEventMessage> {
  @Override
  public void handleMessage(ViewEventMessage message, String connectionId, ServerContext context) {
    context.getConnectionMatch(connectionId).notifyViewEvent(message);
  }
}
