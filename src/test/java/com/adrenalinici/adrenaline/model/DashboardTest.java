package com.adrenalinici.adrenaline.model;

import org.junit.Test;

import static com.adrenalinici.adrenaline.model.MyAssertions.*;

public class DashboardTest {

    @Test
    public void testBuilder() {
        Dashboard d = Dashboard.newBuilder()
                .newEastCell(c ->
                    c.setEastType(DashboardCellBoundType.DOOR).newPickupCell()
                )
                .newNorthLine()
                .newEastCell(c ->
                    c.setWestType(DashboardCellBoundType.DOOR).newRespawnCell()
                ).build();
        assertPresent(d.getDashboardCell(0, 0));
        assertAbsent(d.getDashboardCell(1, 0));
        assertPresent(d.getDashboardCell(0, 1));
        assertAbsent(d.getDashboardCell(1, 1));
        assertInstanceOf(PickupDashboardCell.class, d.getDashboardCell(0, 0).get());
        assertInstanceOf(RespawnDashboardCell.class, d.getDashboardCell(0, 1).get());
    }

}
