package com.adrenalinici.adrenaline.network.client.rmi;

import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
