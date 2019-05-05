package com.adrenalinici.adrenaline.network.server.rmi;

import com.adrenalinici.adrenaline.network.client.rmi.GameRmiClient;
import com.adrenalinici.adrenaline.network.inbox.InboxMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameRmiServer extends Remote {

  void acceptMessage(InboxMessage message, GameRmiClient client) throws RemoteException;

  void startConnection(GameRmiClient client) throws RemoteException;

}
