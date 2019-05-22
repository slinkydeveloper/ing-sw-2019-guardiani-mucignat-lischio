package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.network.inbox.InboxMessage;

public interface MessageHandler<T extends InboxMessage> {

  void handleMessage(T message, String connectionId, ServerContext context);

}
