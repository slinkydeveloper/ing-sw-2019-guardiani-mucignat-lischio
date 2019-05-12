package com.adrenalinici.adrenaline.cli;

import com.adrenalinici.adrenaline.model.common.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.model.light.LightGameModel;
import com.adrenalinici.adrenaline.network.client.ClientNetworkAdapter;
import com.adrenalinici.adrenaline.network.client.ClientViewProxy;
import com.adrenalinici.adrenaline.network.client.rmi.RmiClientNetworkAdapter;
import com.adrenalinici.adrenaline.network.client.socket.SocketClientNetworkAdapter;
import com.adrenalinici.adrenaline.view.BaseClientGameView;
import org.fusesource.jansi.AnsiConsole;

import java.util.*;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

public class CliMain extends BaseClientGameView {

  Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {

    if (args.length < 3) {
      System.err.println("Usage: adrenaline-cli socket|rmi HOST PORT");
    }

    String transport = args[0];
    String host = args[1];
    int port = Integer.parseInt(args[2]);

    CliMain cliMain = new CliMain();
    ClientViewProxy proxy = new ClientViewProxy(cliMain);
    ClientNetworkAdapter networkAdapter = null;
    if (transport.equals("socket")) {
      networkAdapter = new SocketClientNetworkAdapter(proxy, host, port);
    } else if (transport.equals("rmi")) {
      networkAdapter = new RmiClientNetworkAdapter(proxy, host, port);
    } else {
      System.out.println("Wrong transport!");
      System.exit(1);
    }
    System.out.println(String.format("Transport %s to %s:%d initialized", transport, host, port));
    networkAdapter.run();
  }

  @Override
  public void showChooseMyPlayer(List<PlayerColor> colorList) {
    if (getMyPlayer() == null) {
      System.out.println(
        String.format("Choosable players: %s", colorList.stream().map(Objects::toString).collect(Collectors.joining(", ")))
      );
      String chosenPlayer = scanner.nextLine();
      setMyPlayer(PlayerColor.valueOf(chosenPlayer.trim()));
      sendChosenMyPlayer(getMyPlayer());
    }
  }

  @Override
  public void showAvailableActions(List<Action> actions) {
    if (isMyTurn()) {
      System.out.println(
        String.format("Available actions: %s", actions.stream().map(Objects::toString).collect(Collectors.joining(", ")))
      );
    }
  }

  @Override
  public void showAvailableMovements(List<Position> positions) {
    if (isMyTurn()) {
      System.out.println(
        String.format("Available movements: %s", positions.stream().map(Objects::toString).collect(Collectors.joining(", ")))
      );
    }
  }

  @Override
  public void showNextTurn(PlayerColor player) {
    this.setTurnOfPlayer(player);
    System.out.println("Turn of " + player);
  }

  @Override
  public void showReloadableGuns(Set<String> guns) {

  }

  @Override
  public void showLoadedGuns(Set<String> guns) {

  }

  @Override
  public void showBaseGunExtraEffects(List<Effect> effects) {

  }

  @Override
  public void showAvailablePowerUpCardsForRespawn(PlayerColor player, List<PowerUpCard> powerUpCards) {

  }

  @Override
  public void showAvailableAlternativeEffectsGun(Effect firstEffect, Effect secondEffect) {

  }

  @Override
  public void showChoosePlayerToHit(List<PlayerColor> players) {

  }

  @Override
  public void showChoosePlayerToMove(Map<PlayerColor, List<Position>> availableMovements) {

  }

  @Override
  public void showAvailableExtraEffects(Effect firstExtraEffect, Effect secondExtraEffect) {

  }

  @Override
  public void showAvailableVenomGranades(PlayerColor player) {

  }

  @Override
  public void showAvailableEnemyMovements(List<Position> positions) {

  }

  @Override
  public void showAvailableGuns(Set<String> guns) {

  }

  @Override
  public void showAvailableGunsToPickup(Set<String> guns) {

  }

  @Override
  public void onEvent(ModelEvent newValue) {
    showModel(newValue.getGameModel());
  }

  private void showModel(LightGameModel model) {
    System.out.println(model.toString());
  }
}
