package com.adrenalinici.adrenaline.network.client.rmi;

import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameRmiClient extends Remote, Serializable {

  void acceptMessage(OutboxMessage message) throws RemoteException;

}
