package com.adrenalinici.adrenaline.common.network.rmi;

import com.adrenalinici.adrenaline.common.network.outbox.OutboxMessage;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameRmiClient extends Remote, Serializable {

  void acceptMessage(OutboxMessage message) throws RemoteException;

}
