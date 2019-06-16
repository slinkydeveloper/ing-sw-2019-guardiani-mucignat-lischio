package com.adrenalinici.adrenaline.server.network.handlers;

import com.adrenalinici.adrenaline.common.network.inbox.ViewEventMessage;
import com.adrenalinici.adrenaline.server.network.MessageHandler;
import com.adrenalinici.adrenaline.server.network.ServerContext;

public class ViewEventMessageHandler implements MessageHandler<ViewEventMessage> {
  @Override
  public void handleMessage(ViewEventMessage message, String connectionId, ServerContext context) {
    context.getConnectionMatch(connectionId).notifyViewEvent(message);
  }
}
