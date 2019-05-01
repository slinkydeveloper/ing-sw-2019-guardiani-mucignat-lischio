package com.adrenalinici.adrenaline.network.inbox;

import java.io.Serializable;
import java.util.function.Consumer;

public interface InboxMessage extends Serializable {

  default void onConnectedPlayerMessage(Consumer<ConnectedPlayerMessage> consumer) {}

  default void onChosenMyPlayerColorMessage(Consumer<ChosenMyPlayerColorMessage> consumer) {}

  default void onViewEventMessage(Consumer<ViewEventMessage> consumer) {}

  default void onDisconnectedPlayerMessage(Consumer<DisconnectedPlayerMessage> consumer) {}

}
