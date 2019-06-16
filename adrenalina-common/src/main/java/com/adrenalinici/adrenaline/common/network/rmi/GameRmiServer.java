package com.adrenalinici.adrenaline.common.network.rmi;

import com.adrenalinici.adrenaline.common.network.inbox.InboxMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameRmiServer extends Remote {

  void acceptMessage(InboxMessage message, GameRmiClient client) throws RemoteException;

  void startConnection(GameRmiClient client) throws RemoteException;

}
