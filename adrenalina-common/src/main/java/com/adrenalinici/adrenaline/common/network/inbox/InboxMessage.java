package com.adrenalinici.adrenaline.common.network.inbox;

import java.io.Serializable;
import java.util.function.Consumer;

public interface InboxMessage extends Serializable {

  default void onConnectedPlayerMessage(Consumer<ConnectedPlayerMessage> consumer) {}

  default void onChosenMatchMessage(Consumer<ChosenMatchMessage> consumer) {}

  default void onViewEventMessage(Consumer<ViewEventMessage> consumer) {}

  default void onDisconnectedPlayerMessage(Consumer<DisconnectedPlayerMessage> consumer) {}

  default void onNewMatchMessage(Consumer<NewMatchMessage> consumer) {}

}
