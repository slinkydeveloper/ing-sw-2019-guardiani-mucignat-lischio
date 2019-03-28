package com.adrenalinici.adrenaline.model;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;
import static com.adrenalinici.adrenaline.model.MyAssertions.*;

public class PlayerDashboardTest {

    @Test
    public void addAmmoTest() {
        PlayerDashboard playerDashboard = new PlayerDashboard(false, Collections.emptyList());
        playerDashboard.addAmmmo(AmmoColor.RED);
        assertEquals(4, playerDashboard.getAmmos().size());
        assertContainsExactly(AmmoColor.RED, 2, playerDashboard.getAmmos());
    }

    @Test
    public void newPlayerDashboardMustContainOneAmmoForType() {
        PlayerDashboard playerDashboard = new PlayerDashboard(false, Collections.emptyList());
        assertEquals(3, playerDashboard.getAmmos().size());
        assertContainsExactly(AmmoColor.RED, 1, playerDashboard.getAmmos());
        assertContainsExactly(AmmoColor.BLUE, 1, playerDashboard.getAmmos());
        assertContainsExactly(AmmoColor.YELLOW, 1, playerDashboard.getAmmos());
    }

    @Test
    public void addAmmoMustThrowExceptionTest() {
        PlayerDashboard playerDashboard = new PlayerDashboard(false, Collections.emptyList());
        playerDashboard.addAmmmo(AmmoColor.RED);
        playerDashboard.addAmmmo(AmmoColor.RED);
        assertContainsExactly(AmmoColor.RED, 3, playerDashboard.getAmmos());
        assertThatCodeThrowsExceptionOfType(() -> playerDashboard.addAmmmo(AmmoColor.RED), IllegalStateException.class);
        assertContainsExactly(AmmoColor.RED, 3, playerDashboard.getAmmos());
    }

}
