package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.network.inbox.*;
import com.adrenalinici.adrenaline.network.outbox.OutboxEntry;
import com.adrenalinici.adrenaline.network.server.handlers.*;
import com.adrenalinici.adrenaline.util.LogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMessageRouter implements Runnable {

  private static final Logger LOG = LogUtils.getLogger(ServerMessageRouter.class);

  private final BlockingQueue<InboxEntry> inbox;
  private final ServerContext context;
  private final Map<Class<? extends InboxMessage>, MessageHandler> handlers;

  public ServerMessageRouter(BlockingQueue<InboxEntry> inbox, BlockingQueue<OutboxEntry> outboxRmi, BlockingQueue<OutboxEntry> outboxSocket) {
    this.inbox = inbox;
    this.context = new ServerContext(inbox, outboxRmi, outboxSocket);
    this.handlers = new HashMap<>();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void run() {
    try {
      while (!Thread.currentThread().isInterrupted()) {
        try {
          InboxEntry e = inbox.take();
          LOG.fine(String.format("Received new message from network adapter. Connection %s, message: %s", e.getConnectionId(), e.getMessage().getClass().getSimpleName()));
          MessageHandler handler = this.handlers.get(e.getMessage().getClass());
          if (handler == null) {
            LOG.severe("Missing handler for message " + e.getMessage().getClass().getName());
            continue;
          }
          handler.handleMessage(e.getMessage(), e.getConnectionId(), context);
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
        }
      }
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Uncaught exception that fuck up everything", e);
    }
  }

  public ServerMessageRouter withHandler(Class<? extends InboxMessage> messageHandledType, Function<ServerContext, MessageHandler> factory) {
    this.handlers.put(messageHandledType, factory.apply(this.context));
    return this;
  }

  public ServerMessageRouter withHandler(Class<? extends InboxMessage> messageHandledType, MessageHandler handler) {
    this.handlers.put(messageHandledType, handler);
    return this;
  }

  public ServerContext getContext() {
    return context;
  }

  public static ServerMessageRouter createWithHandlers(BlockingQueue<InboxEntry> inbox, BlockingQueue<OutboxEntry> outboxRmi, BlockingQueue<OutboxEntry> outboxSocket, long turnTimeout) {
    return new ServerMessageRouter(inbox, outboxRmi, outboxSocket)
      .withHandler(ChosenMatchMessage.class, new ChosenMatchMessageHandler())
      .withHandler(ConnectedPlayerMessage.class, new ConnectedPlayerMessageHandler())
      .withHandler(DisconnectedPlayerMessage.class, new DisconnectedPlayerMessageHandler())
      .withHandler(NewMatchMessage.class, new NewMatchMessageHandler(turnTimeout))
      .withHandler(ViewEventMessage.class, new ViewEventMessageHandler());
  }
}
