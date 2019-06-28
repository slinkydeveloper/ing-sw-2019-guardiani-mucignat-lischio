package com.adrenalinici.adrenaline.common.util;

import com.adrenalinici.adrenaline.common.model.*;
import com.adrenalinici.adrenaline.common.view.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ViewEventSerializationTest {

  @Parameterized.Parameter
  public ViewEvent event;

  @Parameterized.Parameters
  public static Object[] data() {
    return new ViewEvent[] {
      new ActionChosenEvent(Action.MOVE_MOVE_MOVE),
      new AlternativeGunEffectChosenEvent(false),
      new BaseGunEffectChosenEvent(false, true),
      new GunChosenEvent("zx2"),
      new MovementChosenEvent(Position.of(10, 3)),
      new PlayerChosenEvent(PlayerColor.YELLOW),
      new CellToHitChosenEvent(Position.of(1, 2)),
      new EnemyMovementChosenEvent(Position.of(1, 2)),
      new RoomChosenEvent(CellColor.CYAN),
      new UseNewtonEvent(new PowerUpCard(AmmoColor.BLUE, PowerUpType.NEWTON), Position.of(1, 2), PlayerColor.YELLOW),
      new UseTeleporterEvent(Position.of(1, 2), new PowerUpCard(AmmoColor.BLUE, PowerUpType.TELEPORTER)),
      new UseTagbackGrenadeEvent(PlayerColor.YELLOW, new PowerUpCard(AmmoColor.BLUE, PowerUpType.TAGBACK_GRENADE))
    };
  }

  @Test
  public void testSerDe() {
    byte[] bytes = SerializationUtils.serialize(event);
    ViewEvent e1 = SerializationUtils.deserialize(bytes);
    assertThat(e1.getClass()).isEqualTo(event.getClass());
    assertThat(e1).isEqualTo(event);
  }

}
