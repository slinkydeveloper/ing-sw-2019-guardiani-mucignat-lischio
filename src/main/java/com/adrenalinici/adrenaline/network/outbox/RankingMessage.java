package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.common.PlayerColor;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RankingMessage implements OutboxMessage {

  private List<Map.Entry<PlayerColor, Integer>> ranking;

  public RankingMessage(List<Map.Entry<PlayerColor, Integer>> ranking) {
    this.ranking = ranking;
  }

  public List<Map.Entry<PlayerColor, Integer>> getRanking() { return this.ranking; }

  @Override
  public void onRankingMessage(Consumer<RankingMessage> c) { c.accept(this); }

}
