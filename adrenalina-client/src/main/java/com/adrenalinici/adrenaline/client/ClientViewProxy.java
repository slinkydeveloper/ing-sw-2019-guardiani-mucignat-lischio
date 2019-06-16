package com.adrenalinici.adrenaline.client;

import com.adrenalinici.adrenaline.common.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.common.util.Observable;

public interface ClientViewProxy extends Observable<InboxMessage> {

  void handleNewServerMessage(OutboxMessage message);

}
