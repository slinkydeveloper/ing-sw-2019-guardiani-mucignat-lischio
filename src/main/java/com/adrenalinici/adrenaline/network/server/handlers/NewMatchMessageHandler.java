package com.adrenalinici.adrenaline.network.server.handlers;

import com.adrenalinici.adrenaline.controller.GameController;
import com.adrenalinici.adrenaline.model.fat.Dashboard;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.fat.PlayerDashboard;
import com.adrenalinici.adrenaline.network.inbox.NewMatchMessage;
import com.adrenalinici.adrenaline.network.outbox.InfoMessage;
import com.adrenalinici.adrenaline.network.outbox.InfoType;
import com.adrenalinici.adrenaline.network.server.MessageHandler;
import com.adrenalinici.adrenaline.network.server.RemoteView;
import com.adrenalinici.adrenaline.network.server.ServerContext;

import java.util.HashSet;
import java.util.List;

public class NewMatchMessageHandler implements MessageHandler<NewMatchMessage> {

  private final long turnTimerSeconds;

  public NewMatchMessageHandler(long turnTimerSeconds) {
    this.turnTimerSeconds = turnTimerSeconds;
  }

  @Override
  public void handleMessage(NewMatchMessage message, String connectionId, ServerContext context) {
    if (!context.getMatches().containsKey(message.getMatchId())) {
      Dashboard dashboard = message.getDashboard().generate();
      List<PlayerDashboard> playerDashboards = message.getPlayers().generate();
      GameModel model = message.getRules().generate(dashboard, playerDashboards);

      RemoteView remoteView = new RemoteView(message.getMatchId(), context, new HashSet<>(model.getPlayers()), turnTimerSeconds);
      GameController controller = new GameController(model);
      remoteView.registerObserver(controller);
      model.registerObserver(remoteView);

      context.addMatch(remoteView, controller);

      context.send(
        connectionId,
        new InfoMessage("Ok, you are now connected!", InfoType.INFO)
      );

    } else {
      context.send(
        connectionId,
        new InfoMessage("Chosen Match Id is already used. Please try again", InfoType.ERROR)
      );
    }

    context.send(
      connectionId,
      HandlerUtils.generateAvailableMatchesMessage(context)
    );
  }
}
