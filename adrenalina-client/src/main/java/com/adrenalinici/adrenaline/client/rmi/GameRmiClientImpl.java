package com.adrenalinici.adrenaline.client.rmi;

import com.adrenalinici.adrenaline.common.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.common.network.rmi.GameRmiClient;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;

public class GameRmiClientImpl implements GameRmiClient {

  private BlockingQueue<OutboxMessage> clientViewInbox;

  public GameRmiClientImpl(BlockingQueue<OutboxMessage> clientViewInbox) {
    this.clientViewInbox = clientViewInbox;
  }

  @Override
  public void acceptMessage(OutboxMessage message) throws RemoteException {
    this.clientViewInbox.offer(message);
  }
}
