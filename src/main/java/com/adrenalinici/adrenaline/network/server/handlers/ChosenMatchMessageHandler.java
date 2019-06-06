package com.adrenalinici.adrenaline.network.server.handlers;

import com.adrenalinici.adrenaline.network.inbox.ChosenMatchMessage;
import com.adrenalinici.adrenaline.network.outbox.InfoMessage;
import com.adrenalinici.adrenaline.network.outbox.InfoType;
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
      context.send(
        connectionId,
        new InfoMessage("Ok, you are now connected!", InfoType.INFO)
      );
    } else {
      context.send(
        connectionId,
        new InfoMessage("Chosen match does not exist or player already chosen. Please try again", InfoType.ERROR)
      );
      context.send(
        connectionId,
        HandlerUtils.generateAvailableMatchesMessage(context)
      );
    }
  }
}
