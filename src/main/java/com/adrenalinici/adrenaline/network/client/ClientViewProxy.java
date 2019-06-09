package com.adrenalinici.adrenaline.network.client;

import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.util.Observable;

public interface ClientViewProxy extends Observable<InboxMessage> {

  void handleNewServerMessage(OutboxMessage message);

}
