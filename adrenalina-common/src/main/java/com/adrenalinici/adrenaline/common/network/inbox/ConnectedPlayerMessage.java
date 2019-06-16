package com.adrenalinici.adrenaline.common.network.inbox;

import java.util.function.Consumer;

public class ConnectedPlayerMessage implements InboxMessage {
  @Override
  public void onConnectedPlayerMessage(Consumer<ConnectedPlayerMessage> consumer) {
    consumer.accept(this);
  }
}
