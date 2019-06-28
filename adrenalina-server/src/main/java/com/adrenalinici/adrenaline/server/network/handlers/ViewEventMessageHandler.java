package com.adrenalinici.adrenaline.server.network.handlers;

import com.adrenalinici.adrenaline.common.network.inbox.ViewEventMessage;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.server.network.MessageHandler;
import com.adrenalinici.adrenaline.server.network.ServerContext;

import java.util.logging.Logger;

public class ViewEventMessageHandler implements MessageHandler<ViewEventMessage> {

  private static final Logger LOG = LogUtils.getLogger(ViewEventMessageHandler.class);

  @Override
  public void handleMessage(ViewEventMessage message, String connectionId, ServerContext context) {
    if (context.getConnectionMatch(connectionId) != null) {
      LOG.info(String.format("Sending to remote view message %s", message.getViewEvent().getClass()));
      context.getConnectionMatch(connectionId).notifyViewEvent(message);
    } else {
      LOG.info(String.format("Discarding message %s because no match was found", message.getViewEvent().getClass()));
    }
  }
}
