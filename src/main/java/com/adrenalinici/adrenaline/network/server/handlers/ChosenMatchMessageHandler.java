package com.adrenalinici.adrenaline.network.server.handlers;

import com.adrenalinici.adrenaline.network.inbox.ChosenMatchMessage;
import com.adrenalinici.adrenaline.network.server.MessageHandler;
import com.adrenalinici.adrenaline.network.server.ServerContext;

public class ChosenMatchMessageHandler implements MessageHandler<ChosenMatchMessage> {

  @Override
  public void handleMessage(ChosenMatchMessage message, String connectionId, ServerContext context) {
    if (
      context.getMatches().containsKey(message.getMatchId()) &&
      context.getMatches().get(message.getMatchId()).notifyNewPlayer(connectionId, message.getColor())
    ) {
      context.addConnectionToMatch(connectionId, message.getMatchId());
      context.getMatches().get(message.getMatchId()).checkMatchStatus();
    } else {
      context.send(
        connectionId,
        HandlerUtils.generateAvailableMatchesMessage(context)
      );
    }
  }
}
