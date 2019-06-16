package com.adrenalinici.adrenaline.server.network;

import com.adrenalinici.adrenaline.common.network.inbox.InboxMessage;

public interface MessageHandler<T extends InboxMessage> {

  void handleMessage(T message, String connectionId, ServerContext context);

}
