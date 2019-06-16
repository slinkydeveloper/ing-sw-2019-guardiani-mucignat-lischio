package com.adrenalinici.adrenaline.server.network.handlers;

import com.adrenalinici.adrenaline.common.model.DashboardChoice;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PlayersChoice;
import com.adrenalinici.adrenaline.common.model.RulesChoice;
import com.adrenalinici.adrenaline.common.network.inbox.NewMatchMessage;
import com.adrenalinici.adrenaline.common.network.outbox.InfoMessage;
import com.adrenalinici.adrenaline.common.network.outbox.InfoType;
import com.adrenalinici.adrenaline.server.controller.GameController;
import com.adrenalinici.adrenaline.server.model.Dashboard;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.adrenalinici.adrenaline.server.model.PlayerDashboard;
import com.adrenalinici.adrenaline.server.network.MessageHandler;
import com.adrenalinici.adrenaline.server.network.RemoteView;
import com.adrenalinici.adrenaline.server.network.ServerContext;

import java.util.Arrays;
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
      DashboardChoice dashboardChoice = message.getDashboard();

      Dashboard dashboard =
        (dashboardChoice == DashboardChoice.SMALL) ? Dashboard.createSmallDashboard() :
          ((dashboardChoice == DashboardChoice.MEDIUM_1) ? Dashboard.createMedium1Dashboard() :
            ((dashboardChoice == DashboardChoice.MEDIUM_2) ? Dashboard.createMedium2Dashboard() : Dashboard.createLargeDashboard()));

      PlayersChoice playersChoice = message.getPlayers();

      List<PlayerDashboard> players =
        (playersChoice == PlayersChoice.THREE) ?
          Arrays.asList(
            new PlayerDashboard(PlayerColor.GREEN),
            new PlayerDashboard(PlayerColor.YELLOW),
            new PlayerDashboard(PlayerColor.PURPLE)
          ) :
          ((playersChoice == PlayersChoice.FOUR) ?
            Arrays.asList(
              new PlayerDashboard(PlayerColor.GREEN),
              new PlayerDashboard(PlayerColor.YELLOW),
              new PlayerDashboard(PlayerColor.PURPLE),
              new PlayerDashboard(PlayerColor.GRAY)
            ) :
            Arrays.asList(
              new PlayerDashboard(PlayerColor.GREEN),
              new PlayerDashboard(PlayerColor.YELLOW),
              new PlayerDashboard(PlayerColor.PURPLE),
              new PlayerDashboard(PlayerColor.GRAY),
              new PlayerDashboard(PlayerColor.CYAN)
            ));

      GameModel model = new GameModel(
        message.getRules() == RulesChoice.SIMPLE ? 5 : 8,
        dashboard,
        players,
        message.getRules() == RulesChoice.COMPLETE
      );

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
