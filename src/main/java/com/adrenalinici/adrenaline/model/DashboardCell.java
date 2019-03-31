package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface DashboardCell {

    class Builder {
        private DashboardCellBoundType northDashboardCellBoundType = DashboardCellBoundType.WALL;
        private DashboardCellBoundType southDashboardCellBoundType = DashboardCellBoundType.WALL;
        private DashboardCellBoundType eastDashboardCellBoundType = DashboardCellBoundType.WALL;
        private DashboardCellBoundType westDashboardCellBoundType = DashboardCellBoundType.WALL;
        private final int x;
        private final int y;
        private final Dashboard.Builder dashboardBuilder;
        private DashboardCellFactory cellFactory;

        public Builder(int x, int y, Dashboard.Builder dashboardBuilder) {
            this.x = x;
            this.y = y;
            this.dashboardBuilder = dashboardBuilder;
        }

        public Builder setNorthType(DashboardCellBoundType northDashboardCellBoundType) {
            this.northDashboardCellBoundType = northDashboardCellBoundType;
            return this;
        }

        public Builder setSouthType(DashboardCellBoundType southDashboardCellBoundType) {
            this.southDashboardCellBoundType = southDashboardCellBoundType;
            return this;
        }

        public Builder setEastType(DashboardCellBoundType eastDashboardCellBoundType) {
            this.eastDashboardCellBoundType = eastDashboardCellBoundType;
            return this;
        }

        public Builder setWestType(DashboardCellBoundType westDashboardCellBoundType) {
            this.westDashboardCellBoundType = westDashboardCellBoundType;
            return this;
        }

        public Dashboard.Builder newPickupCell() {
            cellFactory = PickupDashboardCell::new;
            return this.dashboardBuilder;
        }

        public Dashboard.Builder newRespawnCell() {
            cellFactory = RespawnDashboardCell::new;
            return this.dashboardBuilder;
        }

        protected DashboardCell build(Dashboard dashboardInstance) {
            return cellFactory.create(northDashboardCellBoundType, southDashboardCellBoundType, eastDashboardCellBoundType, westDashboardCellBoundType, x, y, dashboardInstance);
        }


    }

    List<PlayerColor> getPlayersInCell();

    Optional<DashboardCellBoundType> getNorthDashboardCellBoundType();
    Optional<DashboardCellBoundType> getSouthDashboardCellBoundType();
    Optional<DashboardCellBoundType> getEastDashboardCellBoundType();
    Optional<DashboardCellBoundType> getWestDashboardCellBoundType();

    Optional<DashboardCell> getNorthDashboardCell();
    Optional<DashboardCell> getSouthDashboardCell();
    Optional<DashboardCell> getEastDashboardCell();
    Optional<DashboardCell> getWestDashboardCell();

    void visit(Consumer<RespawnDashboardCell> visitRespawnDashboardCell, Consumer<PickupDashboardCell> visitPickupDashboardCell);
}
