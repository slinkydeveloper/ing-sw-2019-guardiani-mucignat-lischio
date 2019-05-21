package com.adrenalinici.adrenaline.network.server.handlers;

import com.adrenalinici.adrenaline.network.outbox.AvailableMatchesMessage;
import com.adrenalinici.adrenaline.network.server.ServerContext;

import java.util.Map;
import java.util.stream.Collectors;

public class HandlerUtils {

  public static AvailableMatchesMessage generateAvailableMatchesMessage(ServerContext context) {
    return new AvailableMatchesMessage(
      context.getMatches().entrySet().stream()
        .filter(e -> !e.getValue().isMatchStarted())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getAvailablePlayers()))
    );
  }

}
