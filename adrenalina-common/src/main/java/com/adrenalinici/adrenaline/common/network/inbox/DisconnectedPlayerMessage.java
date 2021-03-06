package com.adrenalinici.adrenaline.common.network.inbox;

import java.util.function.Consumer;

public class DisconnectedPlayerMessage implements InboxMessage {

  @Override
  public void onDisconnectedPlayerMessage(Consumer<DisconnectedPlayerMessage> consumer) {
    consumer.accept(this);
  }
}
