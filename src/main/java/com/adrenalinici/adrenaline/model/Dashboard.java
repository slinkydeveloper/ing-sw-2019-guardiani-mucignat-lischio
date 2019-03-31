package com.adrenalinici.adrenaline.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Dashboard {

    /**
     * This builder helps the construction of the cell. It builds in this direction:
     *
     * 4 -> 5 -> 6
     *
     * 1 -> 2 -> 3
     *
     */
    public static class Builder {
        List<List<DashboardCell.Builder>> lines = new ArrayList<>();
        List<DashboardCell.Builder> actualLine = new ArrayList<>();

        int actualX = -1;
        int actualY = 0;

        public Builder newEastCell(Consumer<DashboardCell.Builder> c) {
            actualX++;
            DashboardCell.Builder b = new DashboardCell.Builder(actualX, actualY, this);
            actualLine.add(b);
            c.accept(b);
            return this;
        }

        public Builder newNorthLine() {
            this.lines.add(actualLine);
            this.actualLine = new ArrayList<>();
            actualY++;
            return this;
        }

        public Dashboard build() {
            this.lines.add(actualLine);
            int width = actualLine.size();
            if (lines.stream().mapToInt(List::size).anyMatch(i -> i != width))
                throw new IllegalStateException("Cannot build board with different length lines");

            Dashboard dashboard = new Dashboard();
            DashboardCell[][] cells = new DashboardCell[width][this.lines.size()];
            for (int i = 0; i < this.actualLine.size(); i++) {
                for (int j = 0; j < this.lines.size(); j++) {
                    cells[i][j] = this.lines.get(j).get(i).build(dashboard);
                }
            }
            dashboard.setDashboardCells(cells);
            return dashboard;
        }


    }

    private DashboardCell[][] dashboardCells;

    private Dashboard() {}

    private void setDashboardCells(DashboardCell[][] dashboardCells) {
        this.dashboardCells = dashboardCells;
    }

    public Optional<DashboardCell> getDashboardCell(int x, int y) {
        try {
            return Optional.ofNullable(dashboardCells[x][y]);
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    public static Dashboard.Builder newBuilder() {
        return new Builder();
    }

}
