package com.adrenalinici.adrenaline.server.network.handlers;

import com.adrenalinici.adrenaline.common.network.outbox.AvailableMatchesMessage;
import com.adrenalinici.adrenaline.server.network.ServerContext;

import java.util.Map;
import java.util.stream.Collectors;

public class HandlerUtils {

  public static AvailableMatchesMessage generateAvailableMatchesMessage(ServerContext context) {
    return new AvailableMatchesMessage(
      context
        .getMatches()
        .entrySet()
        .stream()
        .filter(e -> !e.getValue().getAvailablePlayers().isEmpty())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getAvailablePlayers()))
    );
  }

}
