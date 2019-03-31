package com.adrenalinici.adrenaline.model;

import java.util.Optional;
import java.util.function.Consumer;

public class PickupDashboardCell extends BaseDashboardCell {
    private AmmoCard ammoCard;

    public PickupDashboardCell(DashboardCellBoundType northDashboardCellBoundType, DashboardCellBoundType southDashboardCellBoundType, DashboardCellBoundType eastDashboardCellBoundType, DashboardCellBoundType westDashboardCellBoundType, int x, int y, Dashboard dashboard) {
        super(northDashboardCellBoundType, southDashboardCellBoundType, eastDashboardCellBoundType, westDashboardCellBoundType, x, y, dashboard);
    }

    public Optional<AmmoCard> getAmmoCard() {
        return Optional.ofNullable(ammoCard);
    }

    public void setAmmoCard(AmmoCard ammoCard) {
        this.ammoCard = ammoCard;
    }

    @Override
    public void visit(Consumer<RespawnDashboardCell> visitRespawnDashboardCell, Consumer<PickupDashboardCell> visitPickupDashboardCell) {
        if (visitPickupDashboardCell != null) {
            visitPickupDashboardCell.accept(this);
        }
    }
}
