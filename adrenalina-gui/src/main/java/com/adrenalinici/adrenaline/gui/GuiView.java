package com.adrenalinici.adrenaline.gui;

import com.adrenalinici.adrenaline.client.ClientNetworkAdapter;
import com.adrenalinici.adrenaline.client.rmi.RmiClientNetworkAdapter;
import com.adrenalinici.adrenaline.client.socket.SocketClientNetworkAdapter;

public class GuiView {

  private ViewEventBus eventBus;
  private Thread networkAdapterThread;
  private ClientNetworkAdapter adapter;

  private GuiView(ViewEventBus eventBus, ClientNetworkAdapter adapter) {
    this.eventBus = eventBus;
    this.adapter = adapter;
    this.networkAdapterThread = new Thread(adapter, "client-network-adapter-thread");
  }

  public ViewEventBus getEventBus() {
    return eventBus;
  }

  public ClientNetworkAdapter getAdapter() {
    return adapter;
  }

  public void startNetworkAdapter() {
    this.networkAdapterThread.start();
  }

  public void stopNetworkAdapter() {
    this.networkAdapterThread.interrupt();
  }

  public static GuiView createRmiGuiView(String host, int port) {
    ViewEventBus eventBus = new ViewEventBus();
    ClientNetworkAdapter networkAdapter = new RmiClientNetworkAdapter(eventBus, host, port);
    return new GuiView(eventBus, networkAdapter);
  }

  public static GuiView createSocketGuiView(String host, int port) {
    ViewEventBus eventBus = new ViewEventBus();
    ClientNetworkAdapter networkAdapter = new SocketClientNetworkAdapter(eventBus, host, port);
    return new GuiView(eventBus, networkAdapter);
  }
}
