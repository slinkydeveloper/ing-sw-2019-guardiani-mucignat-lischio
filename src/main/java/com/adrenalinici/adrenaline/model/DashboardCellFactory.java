package com.adrenalinici.adrenaline.model;

@FunctionalInterface
public interface DashboardCellFactory {

    public DashboardCell create(DashboardCellBoundType northDashboardCellBoundType, DashboardCellBoundType southDashboardCellBoundType, DashboardCellBoundType eastDashboardCellBoundType, DashboardCellBoundType westDashboardCellBoundType, int x, int y, Dashboard dashboard);

}
