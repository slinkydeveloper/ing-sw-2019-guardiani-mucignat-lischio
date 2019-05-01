package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.util.SerializationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
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
      new PlayerChosenEvent(PlayerColor.YELLOW)
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
