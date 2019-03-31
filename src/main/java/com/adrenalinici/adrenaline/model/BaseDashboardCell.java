package com.adrenalinici.adrenaline.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseDashboardCell implements DashboardCell {
    private final List<PlayerColor> playersInCell;
    private final DashboardCellBoundType northDashboardCellBoundType;
    private final DashboardCellBoundType southDashboardCellBoundType;
    private final DashboardCellBoundType eastDashboardCellBoundType;
    private final DashboardCellBoundType westDashboardCellBoundType;
    private final int x;
    private final int y;
    private final Dashboard dashboard;

    public BaseDashboardCell(DashboardCellBoundType northDashboardCellBoundType, DashboardCellBoundType southDashboardCellBoundType, DashboardCellBoundType eastDashboardCellBoundType, DashboardCellBoundType westDashboardCellBoundType, int x, int y, Dashboard dashboard) {
        this.playersInCell = new ArrayList<>();
        this.northDashboardCellBoundType = northDashboardCellBoundType;
        this.southDashboardCellBoundType = southDashboardCellBoundType;
        this.eastDashboardCellBoundType = eastDashboardCellBoundType;
        this.westDashboardCellBoundType = westDashboardCellBoundType;
        this.x = x;
        this.y = y;
        this.dashboard = dashboard;
    }

    @Override
    public List<PlayerColor> getPlayersInCell() {
        return playersInCell;
    }

    @Override
    public Optional<DashboardCellBoundType> getNorthDashboardCellBoundType() {
        return Optional.ofNullable(northDashboardCellBoundType);
    }

    @Override
    public Optional<DashboardCellBoundType> getSouthDashboardCellBoundType() {
        return Optional.ofNullable(southDashboardCellBoundType);
    }

    @Override
    public Optional<DashboardCellBoundType> getEastDashboardCellBoundType() {
        return Optional.ofNullable(eastDashboardCellBoundType);
    }

    @Override
    public Optional<DashboardCellBoundType> getWestDashboardCellBoundType() {
        return Optional.ofNullable(westDashboardCellBoundType);
    }

    @Override
    public Optional<DashboardCell> getNorthDashboardCell() {
        return dashboard.getDashboardCell(x, y + 1);
    }

    @Override
    public Optional<DashboardCell> getSouthDashboardCell() {
        return dashboard.getDashboardCell(x, y - 1);
    }

    @Override
    public Optional<DashboardCell> getEastDashboardCell() {
        return dashboard.getDashboardCell(x + 1, y);
    }

    @Override
    public Optional<DashboardCell> getWestDashboardCell() {
        return dashboard.getDashboardCell(x - 1, y);
    }
}
