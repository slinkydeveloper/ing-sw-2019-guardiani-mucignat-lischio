package com.adrenalinici.adrenaline.model.common;

import com.adrenalinici.adrenaline.model.fat.Dashboard;

public enum DashboardChoice {
  SMALL {
    @Override
    public Dashboard generate() {
      return Dashboard.createSmallDashboard();
    }
  },
  MEDIUM_1 {
    @Override
    public Dashboard generate() {
      return Dashboard.createMedium1Dashboard();
    }
  },
  MEDIUM_2 {
    @Override
    public Dashboard generate() {
      return Dashboard.createMedium2Dashboard();
    }
  },
  BIG {
    @Override
    public Dashboard generate() {
      return Dashboard.createLargeDashboard();
    }
  };

  public abstract Dashboard generate();

}
